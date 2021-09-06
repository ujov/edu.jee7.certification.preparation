# 1Z0-900 - JEE 7 Professional Preparation

Sources:

* [Oracle JEE 7 Tutorial](https://docs.oracle.com/javaee/7/tutorial/index.html)
* [Java EE Essentials by Arun-Gupta](https://www.amazon.de/Java-EE-Essentials-Arun-Gupta/dp/1449370179)

## Servlets 

Coding examples `sample-servlet`
Sources:
* [Java Servlet Technology] (https://docs.oracle.com/javaee/7/tutorial/servlets.htm#BNAFD)

### Servlet Lifecycle

The lifecycle of a servlet is controlled by the container in which the servlet has been deployed. When a request is mapped to a servlet, the container performs the following steps.

* If an instance of the servlet does not exist, the web container:
    * Loads the servlet class
    * Creates an instance of the servlet class
    * Initializes the servlet instance by calling the init method (initialization is covered in Creating and Initializing a Servlet)
* The container invokes the service method, passing request and response objects. Service methods are discussed in Writing Service Methods.

### Handling Servlet Lifecycle Events

<table border="1">
<tr>
<th>Object</th>
<th>Event</th>
<th> Listener Interface and Event Class</th>
</tr>
<tr>
<td>Web context </td>
<td>Initialization and destruction </td>
<td>javax.servlet.ServletContextListener and ServletContextEvent</td>
</tr>
<tr>
<td>Web context </td>
<td>Attribute added, removed, or replaced</td>
<td>javax.servlet.ServletRequestListener and</td>
</tr>
<tr>
<td>Session</td>
<td>Creation, invalidation, activation, passivation, and timeout</td>
<td>javax.servlet.http.HttpSessionListener, javax.servlet.http.HttpSessionActivationListener, and HttpSessionEvent</td>
</tr>
<tr>
<td>Session</td>
<td>Attribute added, removed, or replaced</td>
<td>javax.servlet.http.HttpSessionAttributeListener and HttpSessionBindingEvent</td>
</tr>
<tr>
<td>Request</td>
<td>A servlet request has started being processed by web component</td>
<td>javax.servlet.ServletRequestListener and ServletRequestEvent</td>
</tr>
<tr>
<td>Request</td>
<td>Attribute added, removed, or replaced</td>
<td>javax.servlet.ServletRequestAttributeListener and ServletRequestAttributeEvent</td>
</tr>
</table>

Just implement one of the above interfaces with `@WebListner` annotation.  

### WebServlet

```java
@WebServlet(urlPatterns = {"/account", "/accountNg"}, initParams = {@WebInitParam(name=AccountServlet.PARAMTER_ACCOUNT_NUMBER, value="1")})
public class AccountServlet extends HttpServlet {
```

* Annotation vs. servlet declaration in the deployment desciptor (`web.xml`). 

```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1"
         metadata-complete="true">
    <servlet>
        <servlet-name>AccountServlet</servlet-name>
        <servlet-class>jee7.certification.perparation.sample.servlets.AccountServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>account</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
</web-app>
```

The `metadata-complete` element defines that annotations in the class are not processed. 


### Servlet Filer 

Servlet filter may be used to update the request and response payload. There are three ways to define a filter:

1. By annotation `@WebFilter("/*")`. It's possible to use `@WebInitParameter` here as well.
2. Using `<filter>` and `<filter-mapping>` in the deployment descriptor. 
3. Programmatically using `ServletContext.addFiler`

```java 
public class Initializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		FilterRegistration.Dynamic reg = ctx.addFilter(ResponseLoggingFilter.class.getSimpleName(), ResponseLoggingFilter.class.getName());
		reg.addMappingForUrlPatterns(null, false, "/*");
	}
}
```

Requires `services/javax.servlet.ServletContainerInitializer` file under `META-INF`.

### Asynchronous Support

Returns the thread to the container.

```java
@WebServlet(urlPatterns = "/async", asyncSupported = true)
public class AsyncServlet extends HttpServlet {

protected void doGet(HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		AsyncContext asyncContext = req.startAsync();
		asyncContext.addListener(new AsyncListener() {

			@Override
			public void onComplete(AsyncEvent event) throws IOException {
				LOG.info(String.format("onComplete %s", event));
				resp.getWriter().append("req completed");
			}

			@Override
			public void onTimeout(AsyncEvent event) throws IOException {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(AsyncEvent event) throws IOException {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStartAsync(AsyncEvent event) throws IOException {
				LOG.info(String.format("onStartAsync %s", event));
			}
		});

		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
		executor.execute(new AsyncService(asyncContext));
	}

```

### Nonblocking I/O

Servlet 3.1 achieves nonblocking I/O by introducing two new interfaces: `ReadListener` and `WriteListener`.

Invoking setXXXListner methods indicates hat monblocking I/O is used instead of traditional. 

```java 
final AsyncContext asyncContext = req.startAsync();
	ServletInputStream inputStream = req.getInputStream();
	inputStream.setReadListener(new ReadListener() {
		
		@Override
		public void onError(Throwable t) {
			// TODO Auto-generated method stub

		}
		
		@Override
		public void onDataAvailable() throws IOException {
			//read input stream
		}
		
		@Override
		public void onAllDataRead() throws IOException {
			asyncContext.complete();
			
		}
	});
```

### Web Fragments

Applications can be broken down into various modules, now the web fragments allows us to modularize the deployment descriptors as well. 
In META-INF directory. 

`web-fragment.xml` can use define relative order `<ordering>` whereas the `web.xml` can define `<absolute-ordering>`. 
The two orders are mutually exclusive, and absolute ordering overrides relative.  If  `metadata-complete` set to true `true`, then `web-fragment.xml` is not processed. 

If neither `web.xml` nor `web-fragment.xml` have order elements, the resources are assumed to not have any ordering dependency. 

### Security  

Via Annotation or `web.xml`. 

```java 
@ServletSecurity(value = @HttpConstraint(rolesAllowed = { "R1" }), httpMethodConstraints = {
		@HttpMethodConstraint(value = "GET", rolesAllowed = "*"),
		@HttpMethodConstraint(value = "POST", rolesAllowed = "R2") })
```

* `@ServletSecurity` is used to specify security constrains for all methods
* `@HttpConstraint` for all methods
* `@HttpMethodConstraint` method specific 
* `rolesAllowed = "*"` is equivalent to an empty assignment and means that there is no restriction.   

The security constrains can also be specified using the `<security-constrains>` element in the `web.xml`. 

```xml
<security-constraint>
    <web-resource-collection>
        <web-resource-name>wholesale</web-resource-name>
        <url-pattern>/acme/wholesale/*</url-pattern>
        <http-method>GET</http-method>
        <http-method>POST</http-method>
    </web-resource-collection>
    <auth-constraint>
        <role-name>PARTNER</role-name>
    </auth-constraint>
    <user-data-constraint>
        <transport-guarantee>CONFIDENTIAL</transport-guarantee>
    </user-data-constraint>
<\security-constraint>
```

All other methods than GET and POST are unprotected. If HTTP methods are not enumerated within a security-constraint, the protection apply to the complete set of HTTP methods. 

```xml
<security-constraint>
    <web-resource-collection>
        <web-resource-name>wholesale</web-resource-name>
        <url-pattern>/acme/wholesale/*</url-pattern>
    </web-resource-collection>
    ...
<\security-constraint>
```

So all methods are protected. Servlet 3.1 defines HTTP methods that are not listed in the `<security-constraint>` as uncovered. `<http-method-omission>` can be used to define methods that are not protected. 

```xml
<security-constraint>
    <web-resource-collection>
        <url-pattern>/acme/wholesale/*</url-pattern>
        <http-method-omission>GET</http-method-omission>
    </web-resource-collection>
    ...
<\security-constraint>
```

All other methods than GET are protected.
The `<deny-uncovered-http-methods>` can be used used to deny an HTTP methods request for uncovered HTTP method. 


## JavaServer Faces 

## RESTful Web Services 

### Resouces 

Defintion via `@Path` Annotation. `@PathParam` to bind template parameter. 

```
@ApplicationPath("api")
public class ApplicationConfig extends Application {

}
```

`@QueryParam` can be used to map request parameters. 

`@Suspended` can be used to define an aysnc response. The return type is `void`.

`CompletionCallback` and `ConectionCallback` Listener can be registered. 


### Bind HTTP Methods  

`@GET`, `@POST`, `@PUT`, `@DELETE`, `@HEAD` and `@OPTIONS`.

`@FormParam` can be used to bind the value of an HTML form parameter to a resource method or a field. 

`@Produces` (Accept Header) and `@Consumes` (Content-Type Header).

`@HEAD` is typically to `@GET` without content. 

HTTP OPTIONS method request the communications options available on the request/response identified by the URI. 
Without annotation the JAX-RS runtime generates an automatic response.  

### Multiple Resource Representation

```java 
@Produces({ "application/json", "application/xml" })	
```

The exact return type is determined by the HTTP Accept Header in the request. Wildcards are allowed.  

```java 
@Produces({ "application/*" })	
```
If a client requests with no preference for a particular representation or with Accept Header `application/*` qs ("quality on server") can be used to chose the correct representation.    

### Binding a Request to a Resource

`@PathParam`
`@QueryParam`
`@CookieParam`
`@HeaderParam`
`@FormParam`
`@MatrixParam` (http://127.0.0.1:8080/sample-rest/api/orders;start=400;end=70)

For more details the `@Context` annotation can be used. 
For example: 

`@Context Application app;`
`@Context UriInfo uri;`
`@Context HttpHeaders headers;`
`@Context Request request;`
`@Context SecurityContext security;`
`@Context Providers providers;`

### Entity Providers

`@Provider` annotation and the implementation of `MessageBodyWriter` (to produce) or `MessageBodyReader` (to consume) can be used to provide a own mapping. 

### Mapping Exceptions 

`@Provider` annotation and the implementation of `ExceptionMapper

### Filters and Entity Interceptors

`ClientRequestFilter`
`ClientResponseFilter`
`ContainerRequestFilter`
`ContainerResponseFilter`

```java 
client.register(ClientLoggingFilter.class) // client side registration  
```

```java 
@Provider // server side registration
ServerLoggingFilter implements ContainerRequestFilter
```
An server side registration alternative is via the `Application`.

`ContainerRequestFilter` can be pre-match and post-match. Pre-match filter is applied globally (`@PreMatching`). On the server side the filter can be registered in four different ways: 

* Globally-bound to all resources and all methods in them 
* Globally-bound to all resources and all methods in them via the meta-annotation @NameBinding
* Statically bound to a specific resource/method via the meta-annotation @NameBinding  
* Dynamically bound to a specific resource/method via DynaicFeature.   

`@Priority` ~ lowest number means highest order

`ReaderInterceptor` and `WriterInterceptor` are mainly concerned with marshaling of HTTP-Message Bodies. 

### Client API 

```java 
			Client client = ClientBuilder.newClient();
			client.target("http://127.0.0.1:8080/sample-rest/api/orders").request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(new Order(-1), MediaType.APPLICATION_JSON), Order.class);
```
### Validation of Resources 

`@Valid` can be used to trigger a validation. 


## SOAP-Based Web Services 

### Web Service Endpoints 

`@WebService`

* is called a Service Endpoint Interface (an interface is not required)
* code first approach vs. contract first approach 
* all public methods of the class are exposed as web service operations 

`@WebService` - attributes to override the defaults 
* endpointInterface ~ fully qualified  class name of the service endpoint interface 
* name ~ name of the service (wsdl:portType)
* portName ~ port name of he web service (wsdl:port)
* serviceName ~ service name of the web service (wsdl:service) 
* targetNamespace ~ namespace for the web service (targetNamespace)
* wsdlLocation ~ location of a predefined WSDL describing the service   

`@WebMethod`
* can be used to override the defaults 
* if one method is annotated all other not available at the SEI endpoint 
* `@WebMethod(operationName='hello')`
* `@WebMethod(exclude=true)`
* `@WebParam`
* `@WebResult`
* `@XmlRootElement` ~ converted to XML and vice versa 
* unchecked exceptions are mapped to `SOAPFaultException`
* `@WebFault` may be used to customize the mapping of wsdl:fault in the generated WSDL
* `@OneWay` no response to the client, the method has to be void 
* `WebServiceContext` may be injected in an endpoint implementation class  

WSDL 
* by default document/literal style 
* `@SOAPBinding(style = SOAPBinding.Style.RPC)`

### Provider-Based Dynamic Endpoints 

* provider based endpoints provides y dynamic alternative to the SEI-based endpoint 
* the endpoint needs to implement the `Provider<Source>`, `Provider<SOAPMessage>` or `Provider<DataSource>` 

```java 
@WebServiceProvider
public class MyProvider implements Provider<Source> {

	@Override
	public Source invoke(Source request) {
		//...
	}
}
```

* by default only the message payload 

### Endpoint-Based Endpoints 

* lightweight alternative for creating and publishing an endpoint 
* conventient way of deploying a JAX-WS based web service and point from SE applications 


```java 
@WebService
public class SimpleWebService {

}

Endpoint endpoint = Endpoint.publish("http://localhost:8080/example/SimpleWebService", new SimpleWebService());

```

* contract first endpoint by packing the WSDL 

### Web Service Client

* contract between the web service endpoint and a client is defined through WSDL
* `@WebServiceClient`

### Dispatch-Based Dynamic Client 

* dispatch-based endpoint provides a dynamic alternative to the generated proxy-based client
* the client can be implemented via `Dispatch<Source>, Dispatch<SOAPMeassage>, Dispatch<DataSource>, Dispatch<JAXB Object>`

### Handler

* Logical handler ~ cannot change any protocol-specific parts of a message 
* implements `LogicalHandler` (handleMessage, handleFault, close)
* Protocol handler ~ may access or change the protocol specific aspects of a message 
* Protocol handler, specific to the SOAP protocol, are called by the SOAP handler 
* implements `SOAPHandler` (handleMessage, handleFault, close)

## JSON Processing 

JSON Processing in JSR 353, goals:

* Produce/consume JSON text 
* Build a Java object model for text

### Streaming API

#### Consuming JSON Using the Streaming API 

```Java 
JsonParser parser = Json.createParser(new StringReader("{}"));

enum Event {
    /**
     * Start of a JSON array. The position of the parser is after '['.
     */
    START_ARRAY,
    /**
     * Start of a JSON object. The position of the parser is after '{'.
     */
    START_OBJECT,
    /**
     * Name in a name/value pair of a JSON object. The position of the parser
     * is after the key name. The method {@link #getString} returns the key
     * name.
     */
    KEY_NAME,
    /**
     * String value in a JSON array or object. The position of the parser is
     * after the string value. The method {@link #getString}
     * returns the string value.
     */
    VALUE_STRING,
    /**
     * Number value in a JSON array or object. The position of the parser is
     * after the number value. {@code JsonParser} provides the following
     * methods to access the number value: {@link #getInt},
     * {@link #getLong}, and {@link #getBigDecimal}.
     */
    VALUE_NUMBER,
    /**
     * {@code true} value in a JSON array or object. The position of the
     * parser is after the {@code true} value. 
     */
    VALUE_TRUE,
    /**
     * {@code false} value in a JSON array or object. The position of the
     * parser is after the {@code false} value.
     */
    VALUE_FALSE,
    /**
     * {@code null} value in a JSON array or object. The position of the
     * parser is after the {@code null} value.
     */
    VALUE_NULL,
    /**
     * End of a JSON object. The position of the parser is after '}'.
     */
    END_OBJECT,
    /**
     * End of a JSON array. The position of the parser is after ']'.
     */
    END_ARRAY
}
```
#### Producing JSON Using the Streaming API 

```Java 
		// #### Producing JSON Using the Streaming API
		JsonGeneratorFactory createGeneratorFactory = Json.createGeneratorFactory(null);
		JsonGenerator createGenerator = createGeneratorFactory.createGenerator(System.out);
		createGenerator.writeStartObject().write("title", "The Matrix").write("year", "1999").writeStartArray("cast")
				.write("Keanu Reeves").write("Laurence Fishburne").writeEnd().writeEnd().close();

		// {"title":"The Matrix","year":"1999","cast":["Keanu Reeves","Laurence
		// Fishburne"]}
```


### Object Model API

Needs more memory than the streaming api. 

#### Consuming JSON Using the Object Model API 

```Java 
		// Consuming JSON Using the Object Model API

		JsonReader createReader = Json.createReader(new StringReader("{\"apple\":\"red\"}"));
		JsonObject readObject = createReader.readObject();
		String value = readObject.getString("apple");
		System.out.println(value); // --> red
```

#### Producing JSON Using the Object Model API

```Java 
// Producing JSON Using the Object Model API

		JsonBuilderFactory createBuilderFactory = Json.createBuilderFactory(null);
		JsonArrayBuilder createArrayBuilder = createBuilderFactory.createArrayBuilder();
		JsonObjectBuilder createObjectBuilder = createBuilderFactory.createObjectBuilder();

		JsonObject jsonObject = createObjectBuilder.add("apple", "red").build();
		JsonWriter createWriter = Json.createWriter(System.out);
		createWriter.writeObject(jsonObject);
		createWriter.close();

		JsonArray jsonArray = createArrayBuilder.add(Json.createObjectBuilder().add("apple", "red"))
				.add(Json.createObjectBuilder().add("banana", "yellow")).build();
		
		createWriter = Json.createWriter(System.out);
		createWriter.writeArray(jsonArray);
		createWriter.close();
```

## WebSocket

WebSockets provides a full-duplex and bidirectional communication protocol over a single TCP connection. Full-duplex means a client and server can send messages independent from each other. Bidirectional means a client can send a message to the server and vice versa.

The Java API for WebSockets defines a standard API for building WebSocket applications and will provide support for: 

* Creating a WebSocket client and server endpoint using annotations and an interface
* creating and consuming WebSocket text, binary, and control messages 
* initiating and intercepting WebSocket life-cycle events 
* configuring and managing WebSocktes sessions, like timeouts, retries, cookies, and connection pooling
* specifying how the WebSocket application will work within the java ee security model 

### Annotated Server Endpoint

```Java
@ServerEndpoint(value = "/chat")
public class ChatServer {
	
	public String receiveMessage(String message) {
		return String.format("Response to %s", message);
	}
}
```

The annotated class must have a public no-arg constructor. The annotation can the following attributes: 
* value ~ required, URI or URI template where the endpoint will be deployed
* encoders ~ optional ordered array of encoders 
* decoders ~ optional ordered array of decoders  
* subprotocols ~ optional ordered array of WebSocket protocols supported by the endpoint
* configurator ~ optional custom configurator

`@OnMessage` ~ the message can process text, binary, and pong messages.


```Java
public void processPong(PongMessage pong) {
	...
}
```

`@PathParam`s can be used

```Java
@ServerEndpoint(value = "/chat/{room}")
public class ChatServer {
	
	public String receiveMessage(String message, @PathParam("room") String room) {
		...
	}
}
```

An optional Session parameter can be used. 

```Java	
public void receiveMessage(String message, Session session) {
	session.getBasicRemote().sendText(...);
}
```

The `maxMessageSize` attribute may be used to define the maximum size of the message in bytes. 

```Java
@Message(maxMessageSize=6)	
public void receiveMessage(String message) {

}
``` 

An optional configurator can be used to specify a custom configurator. 

```Java	
public class MyConfigurator extends ServerEndpointConfig.Configurator {

	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		//...
	}
	
}

@ServerEndpoint(value="/webstocket", configurator = MyConfigurator.class)
public class MyEndpoint {

}
```

* @OnOpen ~ optional Session parameter
* @OnClose ~ `CloseReason` parameter 
* @OnError ~ `Throwable` parameter 

* full dependency injection is available 
* the WebSocket annotation behaviors are not passed down!! --> they applay only to the java class on which they are marked

### Programmatic Server Endpoint

A WebSocket can be created by extending the the `Endpoint` class. 

```Java	
public class MyProgmmaticEndpoint extends Endpoint {

	@Override
	public void onOpen(Session session, EndpointConfig config) {

		session.addMessageHandler(new MessageHandler.Whole<String>() {

			@Override
			public void onMessage(String message) {

				Future<Void> sendText = session.getAsyncRemote().sendText(message);
			}
		});
	}
}
```

* a response can be send synchronously and asynchronously 
* onClose and onError can be overridden  
* you receive a multiple message by overriding `MessageHandler.Partial<String>`
* the configuration of programmatic endpoints is done by the implementation of `ServerApplicationConfig`

```Java
public class MyApplicationConfig implements ServerApplicationConfig  {

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
		HashSet<ServerEndpointConfig> config = new HashSet<ServerEndpointConfig>();
		config.add(ServerEndpointConfig.Builder.create(MyProgmmaticEndpoint.class, "/chat2").build());
		return config;
	}

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
		// TODO Auto-generated method stub
		return null;
	}

}
```

* the ServerEndpointConfig.Builder can be used to register the configuarator, decoders etc. 


### Annotated Client Endpoint

You can convert a POJO to a WebSocket client endpoint by using `@ClientEndpoint`

`@ClientEndpoint` can has the following attributes: 

* configurator 
* encoders 
* decoders 
* subprotocols 

* `@OnOpen`, `@OnClose` and `@OnError` can intercept life-cycle events
* `@OnMessage` can be used to receive messages
* the client can connect to the endpoint via `ContainerProvider`
* you can use a custom configurator as well `ClientEndpointConfig.Configurator`

```Java
WebSocketContainer container = ContainerProvider.getWebSocketContainer();
String uri = "ws://127.0.0.1:8080/sample-websocket/chat2";
container.connectToServer(MyClientEndpoint.class, URI.create(uri));
```

### Programmatic Client Endpoint

* By implementing the interface `Endpoint`
* configuration via `MeassageHandlers`
* `session.getBasicRemote` vs. `session.getAsyncRemote`
* `MeassageHandler.Whole` vs. `MeassageHandler.Partial`
* connection via ContainerProvider
* you can use a custom configurator as well `ClientEndpointConfig.Configurator`

### Encoders and Decoders 

* JSON is a typical format for a text format
* you can specify multiple decoders and encoders on am annotated endpoint
* --> the first encoder that matches the given type is used 
* --> the first decode where the `willdecode` methode returns true is used 
*  ServerEndpointConfig.Builder can be used for programmatic endpoints to register decoders and encoders
*  ClientEndpointConfig.Builder can be used for programmatic client endpoints to register decoders and encoders

### Integration with Java EE Security 

* protected in the deployment descriptor 
* a WebSocket that requires authentication must rely on the handshake request that seeks to initiate a connection to be previously authenticated.
* --> typically this will be performed by a http authentication  
* Transport guarantee of NONE allows unencypted ws://
* Transport guarantee of CONFIDENTIAL only allows wss://


## Enterprise Java Beans 

* Session beans 
* Message 

### Stateful Session Beans 

* `@Stateful`
* `@Remove` client can call annotated method to remove the instance from the container 
* no-interface view ~ bean is onlny locally accessible to clients packaged in the same archive 
* `@Remote` to access the bean remotely  
* `@Local` default 
* if one of the interfaces is marked `@Local` or `@Remote`, the each interface needs to be exposed must be marked explicitly
* `@PostConstruct` and `@PreDestroy` lifecycle callbacks are available
* `@PrePassivate` and `@PostActivate`

### Stateless Session Beans

* all instances are equivalent, so the container can choose to delegate a client-invoke to any available instance
* they don't need to be passivated because of no stat
* `@Statelass`  
* `@EJB` to inject
* no-interface, `@Remote` and `@Local` see Stateful Session Beans
* `@PostConstruct` and `@PreDestroy` are supported 

### Singelton Session Beans 

* `@Singelton`
* `@Startup` for eager initialization 
* one instance per application 
* `@PostConstruct` will be executed before the bean is available 
* `@DepandsOn` define dependencies between singeltons
* `@PostConstruct` and `@PreDestroy`
* by default container managed concurrency 
* `@Lock(LockType.WRITE)` (exclusive)  is default vs. `@Lock(LockType.READ)` (shared)

### Life-Cycle Event Callbacks

* `@AroundConstruct` ~ only on interceptor class 

```Java
@MyAroundConstruct
@Interceptor
public class MyAroundConstructInterceptor {


	@AroundConstruct
	public void validateConstructor(InvocationContext context) {
	
	}
}

// -->

@MyAroundConstruct
@Stateful
public class MyBean {

}
```

* the `validateConstructor` method is called every time the Beans constructor is called
* `@PostConstruct` method needs to be executed after dependency injection, only one method can be annotated
* `@PreDestroy` is called before the instance is removed by the container 
* `@PrePassivate` only for stateful session beans, may throw system runtime exception but not application exceptions 
* `@PostActivate` only for stateful session beans, may throw system runtime exception but not application exceptions 

* transaction contexts for life-cycle callbacks: 
* for a statelass session bean, it executes in an unspecified transaction context 
* for a statefull session bean, it executes in a transaction context determined by the life-cycle callback method's transaction attribute 
*  for a singelton session bean, it executes in a transaction context determined by the bean's transaction mamagement type and any applicable transaction attribute   

### Meassage-Driven Beans

* most commonly used to process Java Meassage Service (JMS)
* `@MeassageDriven(mappedName = "myDestination")`
* mappedName ~ defines specifies the JNDI name of the JMS destination
* `MessageListener` Interface with `onMessage` method
* MDB can not invoked by other session beans 
* `ActivationConfigProperty` can be set via the `@MeassageDriven`

* `ActivationConfigProperty` properties:
* achnowledgeMode ~
* messageSelector ~
* destinationType ~ Queue or Topic 
* subscriptionDurabillity ~ if MDB is used with a Topic, specifies whether a durable or nondurable subscription is used. Supported values are Durable or NonDurable.
* single message-driven bean can process messages from multiple clients concurrently 
* all operations within the `onMessage` are part of a single transaction 
* `MessageDrivenContext` provides access to the runtime message-drivem context 

```Java
@Resource
MessageDrivenContext mdc;
```

### Portable Global JNDI Names

* you cam access a ejb using a portable global JNDI name with the following syntax: 

```
java:global[/<app-name>]/<module-name>/<bean-name>[!<fully-qualified-interface-name]
```

* app-name ~ only if the session bean is packged with an .ear file 
* module-name ~ name of the module in whisch the session bean is packaged 
* bean-name ~ ejb-name 

### Transactions 

* bean-managed transaction vs. container-managed transaction (default)
* `@TransactionManagement` is used to declare transaction handling (`CONTAINER` (default) vs `BEAN`)
* bean-managed transaction requires `@TransactionManagement(BEAN)` and the use of `UserTransaction`

```Java
@Statelass
@TransactionManagement(BEAN)
public class AccountSessionBean {
@Resource
UserTransaction tx;

	public float deposit() {
		// ...
		tx.begin();
		// ...
		tx.commit();
		// ...
	}
}
```

* a sateless session bean using CMT can use `@TransactionAttribute` (REQUIRED = default)
* `@TransactionAttribute` values an meanings: 
* MANDATORY ~ without `EJBTransactionRequiredException`
* REQUIRED ~ default 
* REQUIRES_NEW ~ always starts a new transaction, if the client calls with a transaction context, then the suspended transaction is resumed after the new transaction has commited 
* SUPPORTS ~ if the client calls with a transaction context, then it behaves as REQUIRED, if the client without a context it behaves as NOT_SUPPORTED 
* NOT_SUPPORTED ~ if the client calls with a transaction context, then the container suspends and resumes the association of the transaction context before and after the business method is invoked. If the client calls without a transaction context, then **no new transaction** context is created. 
* NEVER ~ client is required to call without a transaction context. If not `EJBTransactionRequiredException`. If the client calls without a transaction context, then it behaves as NOT_SUPPORTED.

* only REQUIRED and NOT_SUPPORTED transaction attributes may be used for message-driven beans. A JMS message is delivered to its final destination after the transaction is committed, so the client will not receive the reply within the same transaction.

### Asynchronous Invocation 

* `@Asynchronous` (method or class level)

```Java
@Statelass
@Asynchronous
public class AsyncBean {

	public Future<Integer> add(...) {
	
		return new AsyncResult(...);
	}
}
```

### Timers

Time-based events can be scheduled in multiple ways:
* automatic based upon `@Schedule` and `@Schedules`
* programmatically using `TimerService`
* methods marked with `@Timeout`
* Deployment descriptors 

```Java
@Singelton
@StartUp
public class MyTimer implements TimedObject {

	@Rescource TimerService timerService; 
	
	@PostConstruct
	public void initTimer() {
		timerService.createCalendarTimer(new ScheduleExpression().hour("*").minute("*").second("*/10"), new TimerConfig("myTimer", true));
	}

	@Override
	public void ejbTimeout(Timer timer) {
		//...
	}
}
```

* When a programmatic timer expires (goes off), the container calls the method annotated @Timeout in the bean’s implementation class. The @Timeout method contains the business logic that handles the timed event.
* Methods annotated @Timeout in the enterprise bean class must return void and optionally take a javax.ejb.Timer object as the only parameter. They may not throw application exceptions

* timers are not available for stateful session beans 
* timers are persistent by default `new TimerConfig("myTimer", true)` or `@Schedule(..., persistent="false")`


### Embeddable API

```Java
EJBContainer ejbC = EJBContainer.createEJBContainer();
Context ctx = ejbC.getContext();
ctx.lookup(...)
```

### EJB Lite 

Page 166

## Context and Dependency Injection 

* "strong typing, loose coupling"
* the injected bean is also called a *contextual instance*

* **bean discovery mode values:**
* all ~ all types are considered for injection
* annotated ~ only types with bean-defining annotations
* none ~ disable cdi

* beans.xml
* implicit bean archive is possible 
* <scan><exclude> to exclude beans / packages 
* <scan><exclude><if-class-available name="..." /> vs <scan><exclude><if-class-not-available name="..." /> vs. <scan><exclude><if-system-property name="..." value="" />
* `@Vetoed` to prevent  from injection (packages as well)

### Injection Points 

* field, method, or constructor (at most one, has to be public)
* bean initialization sequence:
* default constructor or the one annotated with `@Inject`
* all fields with `@Inject`
* all methods with `@Inject`
* the `@PostConstruct` method, if any

### Qualifier and Alternative 

* custom annotation based on `@Qualifier`
* usage for example: `@Inject @Fancy Greeting gretting`
* build in CDI qualifiers:
* `@Named` ~ string based, required for usage in EL
* `@Default` ~ default qualifier on all beans **without an explicit qualifier**, except `@Named`
* `@Any` ~ default qualifier on all beans except `@New` 
* `@New` ~ allows the application to obtain a new instance independent of the declared scope; deprecated in CDI 1.1; and injecting @Dependent scoped beans is encouraged instead 
* use of `@Named` not recommended 
* `@Alternative` unavailable for injection, need to explicit enable them (beans.xml) 


### Poducer and Disposer

* `@Inject` and `@Qualifier` provide static injection of a bean ...
* `@Produces` ~ factory like creation 
* `@Disposes` ~ for explicit destruction 

### Interceptors 

* `@InterceptorBinding` for example Logging annotation 
* `@Interceptor`
* by default all interceptors are disabled 
* need to be enabled and ordered via `@Priority`
* can be enabled in the beans.xml as well; invoked in the order in which they are specified inside the <interceptors> element

```Java
@Interceptor
@Logging // custom annotation
public class LoggingInterceptor {
	@AroundInvoke
	public Object log(InvocationContext context) throws Exception {
	
	}
}
```

### Decoraters

* used to implement business concerns 
* `@Decorator` implements the bean it decorates 
* disabled by default 
* need to be enabled and ordered via `@Priority`
* can be enabled in the beans.xml as well <decoraters> 
* decoraters enabled via `@Priority` called before beans.xml
* interceptor for a method is called before the decorater for this method

### Scopes and Contexts

* a bean is said to be in a scope and is associated with a context
* predefined scopes in CDI:
* `@RequestScoped`
* `@SessionScoped`
* `@ApplicationScoped`
* `@ConversationScoped`
* `@Dependent` ~ dependent pseudoscope

* `@Contextual, @ContextualContext, and @Contex` can be used to create a custom scope

### Sterotypes

* a stereotye encapsulates *architectural patterns or common metadata*
* encapsulates scope, interceptor bindings, qualifiers 
* meta-annotation annotated with `@Sterotype` 
* declare at most one scope 
* zero to multiple interceptors
* adding `@Alternative` is allowed 
* `@Decorator`, `@Interceptor` and `@Model` are predefined sterotypes

### Events 

* producers raise events 
* consumed by obsevers 

```Java
@Inject @Any @Added Event<Customer> event; 

void onCustomer(@Observers @Added Customer event) {

}
```
* `@Observers(notifyObserves = Reception.IF_EXISTS)` ~ to prevent creation if not exists 
* `@Observers(during = TransactionPhase.AFTER_SUCCESS)` 

### Portable Extensions 

* CDI exposes an Service Provider Intercae (SPI) allowing portable extensions to extend the functionally of the container. 
* implements `Extention`
* can listen to a verity of container life-cycle events

### Built-in Beans
* `@Inject UserTransaction` (JEE container)
* `@Inject Principal` (JEE container)
* `@Inject HttpServletRequest` (Servlet container)
* `@Inject HttpSession` (Servlet container)
* `@Inject ServletContext` (Servlet container)