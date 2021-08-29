package jee7.certification.preparation.sample.rest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.Suspended;

@Path("/async/orders")
@Produces({ "application/json" })
public class AsyncOrderResource {

	ExecutorService executor = Executors.newFixedThreadPool(15);

	@GET
	public void getOrders(@Suspended final AsyncResponse ar) {
		Runnable task = () -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ar.resume(new Order(999));
		};
		
		ar.register(new CompletionCallback() {
			@Override
			public void onComplete(Throwable throwable) {
				System.out.println("completed");
			}
		});
		executor.submit(task);
	}
}
