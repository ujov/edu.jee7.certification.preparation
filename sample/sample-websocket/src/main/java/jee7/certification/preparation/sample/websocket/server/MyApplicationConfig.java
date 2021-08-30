package jee7.certification.preparation.sample.websocket.server;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class MyApplicationConfig implements ServerApplicationConfig  {

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
		HashSet<ServerEndpointConfig> config = new HashSet<ServerEndpointConfig>();
		config.add(ServerEndpointConfig.Builder.create(MyProgmmaticEndpoint.class, "/chat2").build());
		return config;
	}

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
		HashSet<Class<?>> hashSet = new HashSet<>();
		hashSet.add(ChatServer.class);
		return hashSet;
	}

}
