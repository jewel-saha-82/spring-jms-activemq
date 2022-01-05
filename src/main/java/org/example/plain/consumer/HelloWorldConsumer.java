package org.example.plain.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class HelloWorldConsumer {

    public static void main(String[] args) throws JMSException {
        String brokerURL = "tcp://EPINPUNW0264:61616";
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("HelloWorld.TEST");

        MessageConsumer consumer = session.createConsumer(destination);

        Message message = consumer.receive(1000);

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println("Received: " + text);
        } else {
            System.out.println("Received: " + message);
        }

        consumer.close();
        session.close();
        connection.close();
    }
}
