package org.example.plain;

import org.example.plain.consumer.HelloWorldConsumer;
import org.example.plain.producer.HelloWorldProducer;

import javax.jms.JMSException;

public class JSMTestMain {

    public static void main(String[] args) throws JMSException {
        HelloWorldProducer.main(null);
        HelloWorldConsumer.main(null);
    }
}
