package com.linuxacademy.ccdak.testing;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class MyProducerTest {

    MockProducer<Integer, String> mockProducer;
    MyProducer myProducer;

    // Contains data sent to System.out during the test.
    private ByteArrayOutputStream systemOutContent;

    // Contains data sent to System.err during the test.
    private ByteArrayOutputStream systemErrContent;
    private final PrintStream originalSystemOut = System.out;
    private final PrintStream originalSystemErr = System.err;

    @Before
    public void setUp() {
        mockProducer = new MockProducer<>(false, new IntegerSerializer(), new StringSerializer());
        myProducer = new MyProducer();
        myProducer.producer = mockProducer;
    }

    @Before
    public void setUpStreams(){
        systemErrContent = new ByteArrayOutputStream();
        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));
        System.setErr(new PrintStream(systemErrContent));
    }

    @After
    public void restoreStreams(){
        System.setOut(originalSystemOut);
        System.setErr(originalSystemErr);
    }

    @Test
    public void testHandleMemberSignup_sent_data() {
        // Perform a simple test to verify that the producer sends the correct data to the correct topic when handleMemberSignup is called.
        // Verify that the published record has the memberId as the key and the uppercased name as the value.
        // Verify that the records is sent to the member_signups topic.
        myProducer.handleMemberSignup(10, "Hello World");
        mockProducer.completeNext();

        List<ProducerRecord<Integer, String>> records = mockProducer.history();
        ProducerRecord<Integer, String> record = records.get(0);
        Assert.assertEquals("member_signups", record.topic());
        Assert.assertEquals("HELLO WORLD", record.value());
        Assert.assertEquals(Integer.valueOf(10), record.key());

    }

    @Test
    public void testHandleMemberSignup_partitioning() {
        // Verify that records with a value starting with A-M are assigned to partition 0, and that others are assigned to partition 1.
        // You can send two records in this test, one with a value that begins with A-M and the other that begins with N-Z.
        myProducer.handleMemberSignup(3, "Attempt");
        myProducer.handleMemberSignup(4, "Zen");
        mockProducer.completeNext();

        List<ProducerRecord<Integer, String>> records = mockProducer.history();
        Assert.assertEquals(2, records.size());
        Assert.assertEquals(Integer.valueOf(0), records.get(0).partition());
        Assert.assertEquals(Integer.valueOf(1), records.get(1).partition());

    }

    @Test
    public void testHandleMemberSignup_output() {
        // Verify that the producer logs the record data to System.out.
        // A text fixture called systemOutContent has already been set up in this class to capture System.out data.
        myProducer.handleMemberSignup(1, "Zen");
        mockProducer.completeNext();
        Assert.assertEquals("key=1, value=ZEN\n", systemOutContent.toString());
    }

    @Test
    public void testHandleMemberSignup_error() {
        // Verify that the producer logs the error message to System.err if an error occurs when sending a record.
        // A text fixture called systemErrContent has already been set up in this class to capture System.err data.
        myProducer.handleMemberSignup(1, "Zen");
        mockProducer.errorNext(new RuntimeException("test error"));
        Assert.assertEquals("test error\n", systemErrContent.toString());
    }

}