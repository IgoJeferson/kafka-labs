package com.linuxacademy.ccdak.testing;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

/**
 *
 * @author will
 */
public class MyConsumerTest {

    MockConsumer<Integer, String> mockConsumer;
    MyConsumer myConsumer;

    // Contains data sent to System.out during the test.
    private ByteArrayOutputStream systemOutContent;
    private final PrintStream originalSystemOut = System.out;

    @Before
    public void setUp() {
        mockConsumer = new MockConsumer(OffsetResetStrategy.EARLIEST);
        myConsumer = new MyConsumer();
        myConsumer.consumer = mockConsumer;
    }

    @Before
    public void setUpStreams(){
        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));
    }

    @After
    public void restoreStreams(){
        System.setOut(originalSystemOut);
    }

    @Test
    public void testHandleRecords_output() {
        // Verify that the testHandleRecords writes the correct data to System.out
        // A text fixture called systemOutContent has already been set up in this class to capture System.out data.
        String topic = "member_signups";
        ConsumerRecord<Integer, String> record = new ConsumerRecord<>(topic, 0, 1, 2, "HEISENBerg");
        Map<TopicPartition, List<ConsumerRecord<Integer, String>>> records = new LinkedHashMap<>();
        records.put(new TopicPartition(topic, 0), Arrays.asList(record));
        ConsumerRecords<Integer, String> consumerRecords = new ConsumerRecords<>(records);

        myConsumer.handleRecords(consumerRecords);
        Assert.assertEquals("key=2, value=HEISENBerg, topic=member_signups, partition=0, offset=1\n", systemOutContent.toString());
    }

    @Test
    public void testHandleRecords_none() {
        // Verify that testHandleRecords behaves correctly when processing no records.
        // A text fixture called systemOutContent has already been set up in this class to capture System.out data.
        String topic = "member_signups";
        Map<TopicPartition, List<ConsumerRecord<Integer, String>>> records = new LinkedHashMap<>();
        records.put(new TopicPartition(topic, 0), Collections.EMPTY_LIST);
        ConsumerRecords<Integer, String> consumerRecords = new ConsumerRecords<>(records);

        myConsumer.handleRecords(consumerRecords);
        Assert.assertEquals("", systemOutContent.toString());
    }

    @Test
    public void testHandleRecords_multiple() {
        // Verify that testHandleRecords behaves correctly when processing multiple records.
        // A text fixture called systemOutContent has already been set up in this class to capture System.out data.
        String topic = "member_signups";
        ConsumerRecord<Integer, String> record = new ConsumerRecord<>(topic, 0, 1, 2, "HEISENBerg");
        ConsumerRecord<Integer, String> record2 = new ConsumerRecord<>(topic, 0, 1, 3, "walter");
        Map<TopicPartition, List<ConsumerRecord<Integer, String>>> records = new LinkedHashMap<>();
        records.put(new TopicPartition(topic, 0), Arrays.asList(record,record2));
        ConsumerRecords<Integer, String> consumerRecords = new ConsumerRecords<>(records);

        myConsumer.handleRecords(consumerRecords);
        Assert.assertEquals("key=2, value=HEISENBerg, topic=member_signups, partition=0, offset=1\n" +
                "key=3, value=walter, topic=member_signups, partition=0, offset=1\n", systemOutContent.toString());
    }
    
}