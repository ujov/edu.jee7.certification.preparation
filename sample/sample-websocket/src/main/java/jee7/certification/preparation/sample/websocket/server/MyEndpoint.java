package jee7.certification.preparation.sample.websocket.server;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/webstocket", configurator = MyConfigurator.class)
public class MyEndpoint {

}
