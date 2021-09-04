package jee7.certification.preparation.sample.websocket.client;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

public class ClientApp {

	public static void main(String[] args) throws DeploymentException, IOException {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		String uri = "ws://127.0.0.1:8080/sample-websocket/chat";
		container.connectToServer(MyClientEndpoint.class, URI.create(uri));
	}

}
