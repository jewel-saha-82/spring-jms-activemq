package org.example.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class HelloWorldProducer {

    public static void main(String[] args) throws JMSException {
        String brokerURL = "tcp://EPINPUNW0264:61616";
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("HelloWorld.TEST");

        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        String text = "Hello world! From: " + Thread.currentThread().getName();
        TextMessage message = session.createTextMessage(text);

        System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
        producer.send(message);

        session.close();
        connection.close();
    }
}
