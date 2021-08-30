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


sample: Example Using Multiple Java EE 7 Technologies Deployed as an EAR
==============================================================================================
Author: Pete Muir
Level: Intermediate
Technologies: EAR, JPA
Summary: Based on kitchensink, but deployed as an EAR
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 7 on JBoss WildFly.

This project is setup to allow you to create a compliant Java EE 7 application using JSF 2.2, CDI 1.1, EJB 3.2, JPA 2.1 and Bean Validation 1.1. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java.

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md) before testing the quickstarts.


Start JBoss WildFly with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-eap-quickstarts#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/sample.ear` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/sample-web>.

1. Enter a name, email address, and Phone nubmer in the input field and click the _Register_ button.
2. If the data entered is valid, the new member will be registered and added to the _Members_ display list.
3. If the data is not valid, you must fix the validation errors and try again.
4. When the registration is successful, you will see a log message in the server console:

        Registering _the_name_you_entered_


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-wildfly-remote


Investigate the Console Output
---------------------
You should see the following console output when you run the tests:

    Results :
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0


Investigate the Server Console Output
---------------------
You should see messages similar to the following:

    INFO  [org.jboss.as.server] (management-handler-thread - 9) JBAS018559: Deployed "test.war"
    INFO  [jee7.certification.preparation.sample.controller.MemberRegistration] (http--127.0.0.1-8080-2) Registering Jane Doe
    INFO  [jee7.certification.preparation.sample.test.MemberRegistrationTest] (http--127.0.0.1-8080-2) Jane Doe was persisted with id 1
    INFO  [org.jboss.weld.deployer] (MSC service thread 1-6) JBAS016009: Stopping weld service for deployment test.war
    INFO  [org.jboss.as.jpa] (MSC service thread 1-1) JBAS011403: Stopping Persistence Unit Service 'test.war#primary'
    INFO  [org.hibernate.tool.hbm2ddl.SchemaExport] (MSC service thread 1-1) HHH000227: Running hbm2ddl schema export
    INFO  [org.hibernate.tool.hbm2ddl.SchemaExport] (MSC service thread 1-1) HHH000230: Schema export complete
    INFO  [org.jboss.as.connector.subsystems.datasources] (MSC service thread 1-5) JBAS010409: Unbound data source [jboss/datasources/sampleTestDS]
    INFO  [org.jboss.as.server.deployment] (MSC service thread 1-6) JBAS015877: Stopped deployment test.war in 19ms
    INFO  [org.jboss.as.server] (management-handler-thread - 10) JBAS018558: Undeployed "test.war"


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md) 


Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
