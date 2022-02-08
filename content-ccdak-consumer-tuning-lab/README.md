# Tuning a Kafka Consumer

Make configuration changes to address the following issues:


- This consumer does not have a high need for real-time data since it is merely a logging utility that provides data for later analysis. Increase the minimum fetch size to 1 K (1024 bytes) to allow the consumer to fetch more data in a single request.
- Changes in consumer status (such as consumers joining or leaving the group) are not being detected quickly enough. Configure the consumer to send a heartbeat every two seconds (2000ms).
- Last week, someone tried to run this consumer against a new cluster. The consumer failed with the following error message:

```Exception in thread "main" org.apache.kafka.clients.consumer.NoOffsetForPartitionException: Undefined offset with no reset policy for partitions: [member_signups-0]```

Ensure the consumer has an offset reset policy that will allow the consumer to read from the beginning of the log when reading from a partition for the first time.
