package com.leo.gbmtrading;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leo.gbmtrading.model.InitialBalance;
import com.leo.gbmtrading.service.InputReader;
import com.leo.gbmtrading.service.TradingService;

@SpringBootTest(classes = TestApplicationConfiguration.class)
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
class BuyStockTests {
	{
		InputReader.setInputFilename("input");
	}
	
	@Autowired
	TradingService tradingService;

	@Test
	void test() throws JsonProcessingException, InterruptedException {
		String r = tradingService.processOrders();
		var initialBalance = new ObjectMapper().readValue(r, InitialBalance.class);
		assertEquals(initialBalance.getInitialBalances().getCash(), 950);
		assertEquals(initialBalance.getInitialBalances().getIssuers().get(0).getTotalShares(), 15);
		assertEquals(initialBalance.getBusinessErrors().size(), 0);
	}
}
