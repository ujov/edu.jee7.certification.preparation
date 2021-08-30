package jee7.certification.preparation.sample.websocket.server;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat")
public class ChatServer {
	
	
	@OnMessage
	public String receiveMessage(String message) {
		return String.format("Response to %s", message);
	}
}
