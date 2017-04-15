# ReadMe


Technology used -

Apache Kafka, Consumer and producer API 

Architecture - 

The KafkaProducer produces sales receipt at topic "sale-data". This topic has 4 partitions and 2 Consumers. These consumers consume the receipt level data and aggregate them store wise and every 5 seconds they spit them to topic "print" which has 1 partition and 1 consumer. This consumer runs and aggregates the data of topic "print" and aggregates them store wise. And prints them every 30 seconds using a scheduledExecutorService.

Reason for 4 partition and 2 consumers for "sale-data"- for horizontal scalability, more consumer threads or processes can be fired up ( upto consumer_count == partition_count) 

Reason for 1 partition and 1 consumer for "print" - this is final aggregator. It catches all store data from different consumers and aggregates them. Spits them out at 30 seconds interval.

Reason for use - 

Kafka Consumer API provides easy way of doing online stream processing. 
Other techs like map reduce are not fit for online stream processing.

There are some other techs e.g. kafka stream and Apache Storm , but for this simple usecase Kafka Consumer API suffices.

For purpose of clarity currently final consumer spits data every 5 seconds and middle consumers spit data at every 2 seconds.
