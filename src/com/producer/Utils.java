package com.producer;

import java.util.Properties;

public class Utils {

	public static class Constants{
		public static String PRINT_TOPIC = "print";
		public static String SALES_TOPIC = "sale-data";
		public static String SALES_GROUP = "sales-group";
		public static String PRINT_GROUP  = "print-group";
	}
	
	public static Properties getConsumerProperties(String group){
		Properties props = new Properties();
		props.put("group.id", group);
	    props.put("bootstrap.servers", "localhost:9092");
	    props.put("enable.auto.commit", "true");
	    props.put("auto.commit.interval.ms", "1000");
	    props.put("session.timeout.ms", "30000");
	    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    return props;
	}
	
	public static Properties getProducerProperties(){
		Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "all");
        //properties.put("producer.type" , "async");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("auto.create.topics.enable",true);
        return properties;
	}
}
