package com.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aggregator.PrintConsumer;
import com.aggregator.SalesDataConsumer;
import com.producer.SalesDataProducer;

public class DemoApp {
	public static void main(String[] args){
		ExecutorService service = Executors.newFixedThreadPool(4);
		
		// 1 source producer for store wise sales data
		// sales-data has 4 partitions in kafka server
		service.submit(new SalesDataProducer());
		
		// 2 middle consumers for store wise sales data, and producer for store wise print data,
		// sales-data has 4 partitions and 2 consumers, these may be varied for horizontal scaling
		service.submit(new SalesDataConsumer(1));
		service.submit(new SalesDataConsumer(2));
		
		// 1 sink consumer for store wise sales data printing
		// print data has 1 partition and 1 consumer
		service.submit(new PrintConsumer(1));
	}
}
