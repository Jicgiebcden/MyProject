
package jms.activemq.myexample.spring;
 
import java.util.Date;
 
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
 
import org.springframework.jms.core.MessageCreator;
 
public class MyMessageCreator implements MessageCreator {
 
    /**
     * 消息序号
     */
    private int msgNo;
 
    public MyMessageCreator(int no) {
        this.msgNo = no;
    }
 
    public Message createMessage(Session session) throws JMSException {
        TextMessage textMsg = session.createTextMessage();
        textMsg.setText(new Date() + "第" + this.msgNo + "条消息发出");
 
        return textMsg;
    }
 
}