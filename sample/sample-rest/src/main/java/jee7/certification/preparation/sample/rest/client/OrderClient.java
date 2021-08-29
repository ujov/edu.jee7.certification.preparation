package jee7.certification.preparation.sample.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import jee7.certification.preparation.sample.rest.Order;

public class OrderClient {

	public void createOrder() {
		try {
			Thread.sleep(3000);
			Client client = ClientBuilder.newClient();
			client.target("http://127.0.0.1:8080/sample-rest/api/orders").request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(new Order(-1), MediaType.APPLICATION_JSON), Order.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
