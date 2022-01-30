# Schema Registry Lab

Your supermarket company is using Kafka to manage updates to inventory as purchases are made in real-time. In the past, data was published to a topic in a basic format, but the company now wants to use a more complex data structure with multiple data points in each record. This is a good use case for Confluent Schema Registry. Create a schema to represent the data and then build a simple producer to publish some sample data using the schema. Finally, create a consumer to consume this data and output it to a data file.

There is a starter project on GitHub at https://github.com/linuxacademy/content-ccdak-schema-registry-lab. Clone this project to the broker and edit its files to implement the solution.

Use the following specification to build a schema called Purchase. You can place the schema file in src/main/avro/com/linuxacademy/ccdak/schemaregistry/.

- Field id with type int. This will contain the purchase id.
- Field product with type string. This will contain the name of the product purchased.
- Field quantity with type int. This will contain the quantity of the product purchased.
- Create a publisher that publishes some sample records using this schema to the inventory_purchases topic.

Then, create a consumer to read these records and output them to a file located at /home/cloud_user/output/output.txt.

You can run the producer in the starter project with the command ./gradlew runProducer. The consumer can be run with ./gradlew runConsumer. Run both the producer and consumer to verify that everything works.

Good luck!

## Steps to validate

1. Run the producer:

``` ./gradlew runProducer```
2. Run the consumer:

``` ./gradlew runConsumer```
3. Verify the data in the output file:

``` cat /home/cloud_user/output/output.txt``` 