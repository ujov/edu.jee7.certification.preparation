package jee7.certification.preparation.sample.rest.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener  {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
//		OrderClient orderClient = new OrderClient();
//		orderClient.createOrder();
		System.out.println("contextInitialized");
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
