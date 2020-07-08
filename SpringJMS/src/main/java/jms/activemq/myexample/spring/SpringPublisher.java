package jms.activemq.myexample.spring;

import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

public class SpringPublisher {

	private JmsTemplate template;
	
	private Destination topic;

	public JmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public Destination getTopic() {
		return topic;
	}

	public void setTopic(Destination topic) {
		this.topic = topic;
	}
	
	protected void sendMessage(int msgNO) {
		System.out.println("正在发送第" + msgNO + "条消息...");
		this.template.send(topic, new MyMessageCreator(msgNO));
	}
	
	public void start() throws InterruptedException {
		int messageCount = 10;
		while ((--messageCount) > 0) {
			sendMessage(messageCount);
			Thread.sleep(1000);
		}
	}
}
