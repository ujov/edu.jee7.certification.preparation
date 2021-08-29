package jee7.certification.preparation.sample.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import jee7.certification.preparation.sample.rest.client.OrderClient;

@Path("/orders")
@Produces({ "application/json" })
@Consumes({ "application/json" })
public class OrderResource {

	private @Inject OrderService orderService;
	
	private @Inject OrderClient orderClient;

	@GET
	public Response getOrders() {
		List<Order> orders = orderService.getOrders();
		return Response.ok(orders).build();
	}
	
	//@GET
	public Response getOrders(@MatrixParam("start") long from, @MatrixParam("end") long to) {
		List<Order> orders = orderService.getOrders(from, to);
		return Response.ok(orders).build();
	}

	@GET
	@Path("/{oid}")
	public Response getOrder(@PathParam("oid") long orderId) {
		Order order = orderService.getOrder(orderId);
		return Response.ok(order).build();
	}

	@POST
	public Response createOrder() {
		Order createOrder = orderService.createOrder();
		return Response.status(Status.CREATED).header("X-ORDER-ID", createOrder.getId()).build();
	}
	
	@POST
	@Path("/client")
	public Response createOrderViaClient() {
		orderClient.createOrder();
		
		return Response.status(Status.CREATED).build();
	}


}
