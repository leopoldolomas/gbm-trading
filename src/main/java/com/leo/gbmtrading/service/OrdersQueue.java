package com.leo.gbmtrading.service;

import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leo.gbmtrading.model.Order;

@Component
public class OrdersQueue implements Iterable<Order> {
	
	@Autowired
	private Queue<Order> ordersQueue;

	@Override
	public Iterator<Order> iterator() {
		return new Iterator<Order>() {
			@Override
			public boolean hasNext() {
				return !ordersQueue.isEmpty();
			}

			@Override
			public Order next() {
				return ordersQueue.poll();
			}
		};
	}
}
