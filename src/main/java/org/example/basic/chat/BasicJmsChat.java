package org.example.basic.chat;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BasicJmsChat implements MessageListener {

    private JmsTemplate chatJmsTemplate;
    private Topic chatTopic;
    private static String userId;

    public static void main(String[] args) throws JMSException, IOException {
        if (args.length < 1)
            System.out.println("User name is required...");
        else {
            userId = args[0];
            ApplicationContext cxt = new ClassPathXmlApplicationContext("META-INF/spring/chat-beans.xml");
            BasicJmsChat basicJmsChat = (BasicJmsChat) cxt.getBean("basicJmsChat");
            TopicConnectionFactory cf = (TopicConnectionFactory) basicJmsChat.chatJmsTemplate.getConnectionFactory();
            TopicConnection tc = cf.createTopicConnection();
            basicJmsChat.publish(tc, basicJmsChat.chatTopic, userId);
            basicJmsChat.subscribe(tc, basicJmsChat.chatTopic, basicJmsChat);
            //System.exit(0);
        }
    }

    private void publish(TopicConnection tc, Topic chatTopic, String userId) throws JMSException, IOException {
        TopicSession session = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicPublisher publisher = session.createPublisher(chatTopic);
        tc.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String msgToSend = reader.readLine();
            if (msgToSend.equalsIgnoreCase("exit")) {
                tc.close();
                System.exit(0);
            } else {
                TextMessage message = (TextMessage) session.createTextMessage();
                message.setText("\n[" + userId + " : " + msgToSend + "]");
                publisher.publish(message);
            }
        }
    }

    private void subscribe(TopicConnection tc, Topic chatTopic, BasicJmsChat basicJmsChat) throws JMSException {
        TopicSession session = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicSubscriber subscriber = session.createSubscriber(chatTopic);
        subscriber.setMessageListener(basicJmsChat);
    }

    public JmsTemplate getChatJmsTemplate() {
        return chatJmsTemplate;
    }

    public void setChatJmsTemplate(JmsTemplate chatJmsTemplate) {
        this.chatJmsTemplate = chatJmsTemplate;
    }

    public Topic getChatTopic() {
        return chatTopic;
    }

    public void setChatTopic(Topic chatTopic) {
        this.chatTopic = chatTopic;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        BasicJmsChat.userId = userId;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String msgText = ((TextMessage) message).getText();
                if (!msgText.startsWith("["+userId))
                    System.out.println(msgText);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
