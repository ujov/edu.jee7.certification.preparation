package jee7.certification.perparation.sample.servlet;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jee7.certification.perparation.sample.servlet.service.AsyncService;

@WebServlet(urlPatterns = "/async", asyncSupported = false)
public class AsyncServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LogManager.getLogger(AsyncServlet.class); 

	protected void doGet(HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
//		AsyncContext asyncContext = req.startAsync();
//		asyncContext.addListener(new AsyncListener() {
//
//			@Override
//			public void onComplete(AsyncEvent event) throws IOException {
//				LOG.info(String.format("onComplete %s", event));
//				resp.getWriter().append("req completed");
//			}
//
//			@Override
//			public void onTimeout(AsyncEvent event) throws IOException {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onError(AsyncEvent event) throws IOException {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onStartAsync(AsyncEvent event) throws IOException {
//				LOG.info(String.format("onStartAsync %s", event));
//			}
//		});
//		
//		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
//		executor.execute(new AsyncService(asyncContext));
	}
}
