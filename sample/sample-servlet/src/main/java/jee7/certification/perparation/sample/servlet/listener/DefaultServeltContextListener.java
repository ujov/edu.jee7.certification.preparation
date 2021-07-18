package jee7.certification.perparation.sample.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DefaultServeltContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println(sce.getSource());

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println(sce.getSource());
	}

}
