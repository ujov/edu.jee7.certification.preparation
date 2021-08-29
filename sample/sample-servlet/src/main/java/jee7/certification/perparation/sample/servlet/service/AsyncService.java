package jee7.certification.perparation.sample.servlet.service;

import javax.servlet.AsyncContext;

public class AsyncService implements Runnable {

	private AsyncContext asyncContext;

	public AsyncService(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2500);
			asyncContext.complete();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
