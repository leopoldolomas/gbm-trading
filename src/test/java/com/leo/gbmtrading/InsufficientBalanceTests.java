package com.leo.gbmtrading;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leo.gbmtrading.service.InputReader;
import com.leo.gbmtrading.service.TradingService;

@SpringBootTest(classes = TestApplicationConfiguration.class)
class InsufficientBalanceTests {
	
	{
		InputReader.setInputFilename("insufficientbalance");
	}
	
	@Autowired
	TradingService tradingService;

	@Test
	void test() throws JsonProcessingException, InterruptedException {
		assertTrue(tradingService.processOrders().contains("insufficient balance"));
	}
}
