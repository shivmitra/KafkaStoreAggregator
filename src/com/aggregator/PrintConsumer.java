package com.aggregator;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.producer.Utils;

public class PrintConsumer implements Runnable{
	private final KafkaConsumer<Integer, Double> consumer;
    private final ConcurrentHashMap<Integer,Double> salesPerStore;
    private final int id;

    public PrintConsumer(int id){
    	this.id = id;
    	Properties props = Utils.getConsumerProperties(Utils.Constants.PRINT_GROUP);
	    props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
	    props.put("value.deserializer", "org.apache.kafka.common.serialization.DoubleDeserializer");
    	consumer = new KafkaConsumer<>(props);
 	   	consumer.subscribe(Arrays.asList(Utils.Constants.PRINT_TOPIC)); 
 	   	
 	   	salesPerStore = new ConcurrentHashMap<>();
      
 	   	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
 	   	service.scheduleAtFixedRate(new Thread(this::summary), 0, 5, TimeUnit.SECONDS);  
    }
    
    @Override
	public void run() {
		while(true){
	        ConsumerRecords<Integer, Double> records = consumer.poll(100);
	        for (ConsumerRecord<Integer, Double> record : records){
				int store_id = record.key();
				Double total_Sales = record.value();
				Double total_till_now =  salesPerStore.get(store_id);
				if(total_till_now == null)
					total_till_now = 0.0;
				total_till_now = total_till_now + total_Sales;
				salesPerStore.put(store_id, total_till_now);
			
	        }
		}
	}
    
    public void summary(){
		 for(Entry<Integer, Double> entry : salesPerStore.entrySet()){
				int store_id = entry.getKey();
				Double total_sales = entry.getValue();
				System.out.println(" store " + store_id + " sales " + total_sales);
			}
       
		 salesPerStore.clear();
		 System.out.println("-------------------------------------");
	}
	
}
