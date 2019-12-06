package com.leo.gbmtrading.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leo.gbmtrading.model.InitialBalance;
import com.leo.gbmtrading.model.InitialBalances;
import com.leo.gbmtrading.model.Issuer;
import com.leo.gbmtrading.model.Order;
import com.leo.gbmtrading.model.Order.OrderType;

@Component
public class TradingService {
	
	private static Logger LOG = LoggerFactory.getLogger(TradingService.class);

	@Autowired
	private OrdersQueue orders;

	@Autowired
	private InitialBalances balance;

	@Autowired
	private Map<String, Issuer> issuers;
	
	private Map<Order, Order> processedOrders = new ConcurrentHashMap<>();

	/**
	 * Process all existing orders
	 * @return JSON response containing final balance and business errors (if any)
	 * @throws InterruptedException
	 * @throws JsonProcessingException 
	 */
	public String processOrders() throws InterruptedException, JsonProcessingException {
		var executor = Executors.newSingleThreadExecutor(); //Executors.newCachedThreadPool();
		var callables = new ArrayList<Callable<String>>();

		for (Order order : orders) {
			callables.add(() -> {
				// business rules validations
				
				// duplicated operation? no operations for the same stock at the same amount
				// must happen within a 5 minutes interval, as they are considered duplicates
				if (processedOrders.containsKey(order)) {
					var processedOrder = processedOrders.get(order);
					if (Math.abs(order.getTimestamp() - processedOrder.getTimestamp()) < 5 * 60) {
						return "duplicated operation, unable to process order with timestamp: " + order.getTimestamp();
					}
				}
				
				var creationDateTime = 
						LocalDateTime.ofInstant(Instant.ofEpochSecond(order.getTimestamp()),
					                               TimeZone.getDefault().toZoneId());
				
				// all operations must happen between 6am and 3pm
				if (creationDateTime.getHour() < 6 || creationDateTime.getHour() > 15) {
					return "closed market, unable to process order with timestamp: " + order.getTimestamp();
				}
				
				var issuer = getIssuers().get(order.getIssuerName());

				if (order.getOrderType() == OrderType.BUY) {
					// insufficient balance?
					var cost = order.getTotalShares() * order.getSharePrice();
					if (cost > balance.getCash()) {
						return "insufficient balance for order with timestamp: " + order.getTimestamp();
					}
				} else {
					// insufficient stocks?
					if (issuer.getTotalShares() < order.getTotalShares()) {
						return "insufficient stocks for order with timestamp: " + order.getTimestamp();
					}
				}
				// end of validations
				
		        var shares = order.getOrderType() == OrderType.BUY ? order.getTotalShares() : order.getTotalShares() * -1;

		        // update no of owned shares
		        issuer.updateNoOfShares(shares);

		        // update current balance
		        getInitialBalances().updateCash(shares * issuer.getSharePrice() * -1);
		        
		        // add order to processedOrders
		        processedOrders.put(order, order);
				return "";
			});
		}
		
		var futures = executor.invokeAll(callables);
		executor.shutdown();
		
		// update issuers
        getInitialBalances().setIssuers(new ArrayList<>(issuers.values()));
		
		var businessErrors = fetchResults(futures);		
		return generateJSONResponse(businessErrors);
	}

	/**
	 * @return Final representation of InitialBalance in JSON format
	 * @throws JsonProcessingException
	 */
	private String generateJSONResponse(List<String> businessErrors) throws JsonProcessingException {
		var balanceResults = new InitialBalance();
        balanceResults.setInitialBalances(balance);
        balanceResults.setBusinessErrors(businessErrors);
        return new ObjectMapper().writeValueAsString(balanceResults);
	}

	/**
	 * Fetches error msgs from "futures"
	 * @param futures list of Future<String> objects returned by the invokeAll(...) method
	 * @return a list containing all error msgs found in "futures"
	 */
	private List<String> fetchResults(List<Future<String>> futures) {
		return futures.parallelStream().map(f -> {
			String r = "";
			try {
				r = f.get();
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
			return r;
		}).filter(s -> !s.isEmpty()).collect(Collectors.toList());
	}

	private InitialBalances getInitialBalances() {
		return balance;
	}

	private Map<String, Issuer> getIssuers() {
		return issuers;
	}
}