package jee7.certification.preparation.sample.websocket.server;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class MyConfigurator extends ServerEndpointConfig.Configurator {

	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		//...
	}
	
}
