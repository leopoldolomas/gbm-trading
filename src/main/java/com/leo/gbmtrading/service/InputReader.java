package com.leo.gbmtrading.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leo.gbmtrading.model.InitialBalance;
import com.leo.gbmtrading.model.InitialBalances;
import com.leo.gbmtrading.model.Issuer;
import com.leo.gbmtrading.model.Order;

/**
 * Class used to read contents from the input file
 */
@Configuration
public class InputReader {
	
	private static String inputFilename = "input";
	
	private InitialBalances initialBalances;
	private Map<String, Issuer> issuersMap;
	private List<Order> orderList;
	private ObjectMapper objectMapper = new ObjectMapper();

	public InputReader() throws IOException {
		try (var inputStreamReader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(inputFilename));
			 var bufferedReader = new BufferedReader(inputStreamReader)) {

			readInitialBalances(bufferedReader);
			populateIssuersMap();
			readOrdersList(bufferedReader);
		}
	}

	private void readOrdersList(BufferedReader bufferedReader) throws IOException {
		var ordersStr = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			ordersStr.append(line);
		}

		var ordersArray = ordersStr.toString().split("}");
		orderList = Stream.of(ordersArray).parallel().map(o -> {
			try {
				// TODO avoid append operation (slooooow)
				return objectMapper.readValue(o + "}", Order.class);
			} catch (JsonProcessingException e) {
				// log error
			}
			return null;
		}).collect(Collectors.toList());
	}

	private void populateIssuersMap() {
		// given the list of Issuers, populate the Issuers hash map for faster
		// read/write operations
		issuersMap = new ConcurrentHashMap<>();
		for (var issuer : initialBalances.getIssuers()) {
			issuersMap.put(issuer.getIssuerName(), issuer);
		}
	}

	private void readInitialBalances(BufferedReader bufferedReader)
			throws JsonProcessingException, JsonMappingException {
		var initialBalance = bufferedReader.lines().takeWhile(s -> !s.equals("}"))
				.reduce("", (s1, s2) -> s1 + s2)
				.concat("}");
		initialBalances = new ObjectMapper().readValue(initialBalance, InitialBalance.class).getInitialBalances();
	}

	/**
	 * @return the Orders queue as read from the "input" file
	 */
	@Bean
	public Queue<Order> getOrdersQueue() {
		return new ConcurrentLinkedQueue<Order>(orderList);
	}

	/**
	 * @return the initial balances as read from the "input" file
	 */
	@Bean
	public InitialBalances getInitialBalances() {
		return initialBalances;
	}

	/**
	 * @return a ConcurrentHashMap containing Issuers
	 */
	@Bean
	public Map<String, Issuer> getIssuersMap() {
		return issuersMap;
	}
	
	/**
	 * TODO This is an ugly way to specify the filename, ideally it should be
	 * defined in application.properties
	 * @param filename
	 */
	public static void setInputFilename(String filename) {
		Objects.requireNonNull(filename);
		inputFilename = filename;
	}
}
