package com.producer;

import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Item;
import com.model.Product;

public class SalesDataProducer implements Runnable{
    private Producer<String, String> producer;
    private final ObjectMapper mapper;
    
    public SalesDataProducer() {
    	
        producer = new KafkaProducer<>(Utils.getProducerProperties());
        mapper = new ObjectMapper();
    }
        
    public void sendData() throws JsonProcessingException{
    	Item i1 = new Item(3,4,new Random().nextInt(50));
    	Item i2 = new Item(4,5,new Random().nextInt(40));
    	Item[] item = {i1,i2};
    	
    	Product p1 = new Product(new Random().nextInt(10),4,5,item);
    	Product p2 = new Product(new Random().nextInt(10),4,6,item);

    	ProducerRecord<String, String> data = new ProducerRecord<>(Utils.Constants.SALES_TOPIC,p1.toString(),mapper.writeValueAsString(p1));
        producer.send(data);
        data = new ProducerRecord<>(Utils.Constants.SALES_TOPIC,p2.toString(),mapper.writeValueAsString(p2));
        producer.send(data); 
    }

	@Override
	public void run() {
		
        while(true){
        	try {
				sendData();
				Thread.sleep(1000);
			} catch (JsonProcessingException | InterruptedException e) {
				e.printStackTrace();
			} 
        }
	}
    
}

