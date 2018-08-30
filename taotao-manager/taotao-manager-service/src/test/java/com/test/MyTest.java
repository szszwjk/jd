package com.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.io.IOException;

public class MyTest {
    /*
    * 点对点 一个发布者 一个接收者
    * 默认缓存到服务器
    * */
   /* @Test
    public void demo1() throws JMSException {
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.122:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
       *//* 1是否开启分布式
        2消息应答模式 自动应答 手动应答*//*
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建点对点对象
        Queue test_queu = session.createQueue("test_queu");
        //创建发布在
        MessageProducer producer = session.createProducer(test_queu);
        TextMessage textMessage=new ActiveMQTextMessage();
        textMessage.setText("point to point");
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void demo2() throws JMSException {
        // 第一步：创建一个ConnectionFactory对象。
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.168:61616");
        // 第二步：从ConnectionFactory对象中获得一个Connection对象。
        Connection connection = connectionFactory.createConnection();
        // 第三步：开启连接。调用Connection对象的start方法。
        connection.start();
        // 第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
        Queue queue = session.createQueue("test-queue");
        // 第六步：使用Session对象创建一个Consumer对象。
        MessageConsumer consumer = session.createConsumer(queue);
        // 第七步：接收消息。
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    String text = null;
                    //取消息的内容
                    text = textMessage.getText();
                    // 第八步：打印消息。
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //等待键盘输入
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 第九步：关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
    @Test
    public void demo3() throws JMSException {
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.122:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic test_topic = session.createTopic("Test_topic");

        MessageProducer producer = session.createProducer(test_topic);
        TextMessage textMessage=new ActiveMQTextMessage();
        textMessage.setText("point to point");
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void demo4() throws JMSException {
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.122:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic test_topic = session.createTopic("Test_topic");

        MessageConsumer consumer = session.createConsumer(test_topic);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof  TextMessage)
                {
                    TextMessage textMessage = (TextMessage) message;
                    String text = null;
                    try {
                        text = textMessage.getText();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    System.out.println(text);

                }
            }
        });
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        consumer.close();
        session.close();
        connection.close();
    }
*/
    /*@Test
    public void demo5()
    {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        Destination destination = (Destination) applicationContext.getBean("queueDestination");
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("test queue");
                return textMessage;
            }
        });

    }*/


}
