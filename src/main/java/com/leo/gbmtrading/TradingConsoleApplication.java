package com.leo.gbmtrading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.leo.gbmtrading.service.TradingService;

@SpringBootApplication
@EnableAutoConfiguration
public class TradingConsoleApplication implements CommandLineRunner {
	
	private static Logger LOG = LoggerFactory.getLogger(TradingConsoleApplication.class);
	
	@Autowired
	private TradingService tradingService;

	public static void main(String[] args) {
		SpringApplication.run(TradingConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("EXECUTING : Trading Service");		
		LOG.info(tradingService.processOrders());
		LOG.info("DONE");	
	}
}
