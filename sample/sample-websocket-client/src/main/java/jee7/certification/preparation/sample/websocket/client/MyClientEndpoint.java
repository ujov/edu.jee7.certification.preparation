package jee7.certification.preparation.sample.websocket.client;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class MyClientEndpoint {

	@OnOpen
	public void onOpen(Session session) {
		try {
			session.getBasicRemote().sendText("Duke");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnMessage
	public void processMessage(String message, Session session) {
		System.out.println(message);
	}
}
