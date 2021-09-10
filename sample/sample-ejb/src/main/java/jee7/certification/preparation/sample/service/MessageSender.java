package jee7.certification.preparation.sample.service;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Session Bean implementation class MessageSender
 */
@Stateless
@LocalBean 
public class MessageSender {

	/**
	 * Default constructor.
	 */
	public MessageSender() {
		System.out.println("MessageSender init");
		try {
			Object doLookup = InitialContext.doLookup("java:/jms/queue/example");
			System.out.println("doLookup");
			System.out.println(doLookup);
		} catch (NamingException e) {
			log.warning(e.getMessage());
		}
	}

	@Inject
	private Logger log;

	@Inject
	JMSContext context;

	@Resource(lookup = "java:/jms/queue/example", type= Queue.class)
	Destination exampleQueue;

	@Schedule(hour = "*", minute = "*", second = "*/30")
	public void sendMessage() {
		log.info("sendMessage");
		System.out.println("sendMessage");
		context.createProducer().send(exampleQueue, "Hello World");
	}
}
