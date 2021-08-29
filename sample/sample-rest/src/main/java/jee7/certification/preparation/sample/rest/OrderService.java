package jee7.certification.preparation.sample.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrderService {

	private final static Map<Long, Order> SIMPLE_ORDER_REPOSITORY;

	static {
		SIMPLE_ORDER_REPOSITORY = new ConcurrentHashMap<Long, Order>();
		SIMPLE_ORDER_REPOSITORY.put(1L, new Order(1));
		SIMPLE_ORDER_REPOSITORY.put(2L, new Order(2));
		SIMPLE_ORDER_REPOSITORY.put(3L, new Order(3));
	}

	public Order getOrder(long orderId) {
		return SIMPLE_ORDER_REPOSITORY.get(orderId);
	}

	public List<Order> getOrders() {
		List<Order> orders = new ArrayList<>(SIMPLE_ORDER_REPOSITORY.values());
		return orders;
	}

	public List<Order> getOrders(long from, long to) {
		return SIMPLE_ORDER_REPOSITORY.entrySet().stream()
				.filter(entity -> entity.getKey() >= from && entity.getKey() <= to).map(entity -> entity.getValue())
				.collect(Collectors.toList());
	}

	public Order createOrder() {
		Long maxId = SIMPLE_ORDER_REPOSITORY.keySet().stream().sorted((o1, o2) -> Long.compare(o2, o1)).findFirst()
				.orElse(0L);
		Long nexId = maxId + 1;
		Order order = new Order(nexId);
		SIMPLE_ORDER_REPOSITORY.put(nexId, order);
		return order;
	}

}
