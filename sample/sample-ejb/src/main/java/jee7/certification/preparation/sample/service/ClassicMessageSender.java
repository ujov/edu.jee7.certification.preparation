package jee7.certification.preparation.sample.service;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Session Bean implementation class ClassicMessageSender
 */
@Stateless
@LocalBean
public class ClassicMessageSender {

    /**
     * Default constructor. 
     */
    public ClassicMessageSender() {
        // TODO Auto-generated constructor stub
    }
    
    @Resource(mappedName = "java:jboss/DefaultJMSConnectionFactory")
    ConnectionFactory connectionFactory;
    
	@Resource(lookup = "java:/jms/queue/example", type= Queue.class)
	Destination exampleQueue;
	
	@Schedule(hour = "*", minute = "*", second = "*/20")
	public void sendMessage() throws JMSException {
		Connection createConnection = connectionFactory.createConnection();
		Session session = createConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(exampleQueue);
		TextMessage textMessage = session.createTextMessage("Another Hello JMS");
		producer.send(textMessage);
	}

}
