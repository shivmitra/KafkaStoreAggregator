package com.aggregator;

import java.io.IOException;
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
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Item;
import com.model.Product;
import com.producer.Utils;

public class SalesDataConsumer implements Runnable{
	
	private final KafkaConsumer<String, String> consumer;
    private Producer<Integer, Double> producer;
    
    private final ObjectMapper mapper;
    private final ConcurrentHashMap<Integer,Double> salesPerStore;
    private final int id;
    
	public SalesDataConsumer(int id) {
		this.id = id;
		
	    consumer = new KafkaConsumer<String,String>(Utils.getConsumerProperties(Utils.Constants.SALES_GROUP));
	    consumer.subscribe(Arrays.asList(Utils.Constants.SALES_TOPIC)); 
        
	    mapper = new ObjectMapper();
        salesPerStore = new ConcurrentHashMap<>();
        
        Properties props = Utils.getProducerProperties();
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.DoubleSerializer");
        producer = new KafkaProducer<>(props);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Thread(this::summary), 0, 2, TimeUnit.SECONDS);        
	}

	@Override
	public void run() {
		while(true){
	        ConsumerRecords<String, String> records = consumer.poll(100);
	        for (ConsumerRecord<String, String> record : records){
	        	try {
					Product value = mapper.readValue(record.value(), Product.class);
					int key = value.getStore_id();
					for(Item item : value.getItems()){
						Double sales = salesPerStore.get(key);
						if(sales == null)
							sales = 0.0;
						sales = sales + item.getTotal_price_paid();
						salesPerStore.put(key, sales);
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		}
	}
	
	public void summary(){
		 for(Entry<Integer, Double> entry : salesPerStore.entrySet()){
				int store_id = entry.getKey();
				Double total_sales = entry.getValue();
		    	ProducerRecord<Integer, Double> data = new ProducerRecord<>(Utils.Constants.PRINT_TOPIC,store_id,total_sales);
				producer.send(data);
				//System.out.println("sent from consumer salesData " + id + " data " + data);
			}
        
		 salesPerStore.clear();
		 //System.out.println("-------------------------------------");
	}
	
	
}
