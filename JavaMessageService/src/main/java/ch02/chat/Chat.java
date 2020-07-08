package ch02.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

import org.apache.activemq.command.ActiveMQTopic;

public class Chat implements MessageListener {

	private TopicSession pubSession;
	private TopicPublisher publisher;
	private TopicConnection connection;
	private String username;
	
	public Chat(String topicFactory, String topicName, String username) throws Exception {
		Properties env = new Properties();
		env.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		env.put("java.naming.provider.url", "tcp://localhost:61616");
		env.put("java.naming.security.principal", "system");
		env.put("java.naming.security.credentials", "manager");
		env.put("connectionFactoryNames", "TopicCF");
		//env.put("topic.topic1", "jms.topic1");
		
		InitialContext ctx = new InitialContext(env);
		TopicConnectionFactory conFactory = (TopicConnectionFactory) ctx.lookup(topicFactory);
		TopicConnection connection = conFactory.createTopicConnection();
		
		TopicSession pubSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicSession subSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//Topic chatTopic = (Topic) ctx.lookup(topicName);
		Topic chatTopic = new ActiveMQTopic("jms.topic1");
		
		TopicPublisher publisher = pubSession.createPublisher(chatTopic);
		TopicSubscriber subscriber = subSession.createSubscriber(chatTopic, null, true);
		
		subscriber.setMessageListener(this);
		
		this.connection = connection;
		this.publisher = publisher;
		this.pubSession = pubSession;
		this.username = username;
		
		connection.start();
	}
	
	public void onMessage(Message arg0) {
		TextMessage textMessage = (TextMessage) arg0;
		try {
			System.out.println(textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	protected void writeMessage(String text) throws JMSException {
		TextMessage textMessage = pubSession.createTextMessage();
		textMessage.setText(username + ":" + text);
		publisher.publish(textMessage);
	}
	
	public void close() throws JMSException {
		connection.close();
	}
	
	public static void main(String [] args) {
		try {
			String temp1, temp2, temp3;
			if (args.length != 3) {
				System.out.println("Factory,Topic,or username missing");
				temp1 = "TopicCF";
				temp2 = "topic.topic1";
				temp3 = "username";
			} else {
				temp1 = args[0];
				temp2 = args[1];
				temp3 = args[2];
			}
			Chat chat = new Chat(temp1, temp2, temp3);
			BufferedReader commandLine = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String s = commandLine.readLine();
				if (s.equalsIgnoreCase("exit")) {
					chat.close();
					break;
				} else {
					chat.writeMessage(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
