# 1Z0-900 - JEE 7 Professional Preparation

Sources:

* [Oracle JEE 7 Tutorial](https://docs.oracle.com/javaee/7/tutorial/index.html)
* [Java EE Essentials by Arun-Gupta](https://www.amazon.de/Java-EE-Essentials-Arun-Gupta/dp/1449370179)

## Examen Topics 

### Understand Java EE Architecture

* Describe Java EE 7 standards, containers, APIs, and services
* Differentiate between application component functionalities as they apply to different tiers and containers, including Java EE Web Container, Business Logic implementation and WebServices
* Create, package and deploy Java EE application 
* Demonstrate understanding of Enterprise JavaBeans and CDI beans, their lifecycle and memory scopes
* Demonstrate understanding of the relationship between bean components, annotations, injections, and JNDI

### Manage Persistence using JPA Entities and BeanValidation

* Create JPA Entity and Relationship Object-Relational Mappings (ORM)
* Use Entity Manager to perform database operations, transactions and locking with JPA entities
* Handle entity data with conversions, validations, and key generation
* Create and execute JPQL statements

### Implement Business Logic by Using EJBs

* Create session EJB components containing synchronous and asynchronous business methods, manage the life cycle container callbacks and use interceptors
* Demonstrate understanding of how to control EJB transactions, distinguish Container Managed (CMT) and Bean Managed (BMT) transactions
* Create EJB timers

### Use Java Message Service API

* Describe the Java Message Service (JMS) messaging models and implement Java SE and Java EE message producers and consumers, including Message-Driven beans
* Use transactions with JMS API 

### Implement SOAP Services by Using JAX-WS and JAXB APIs

* Create SOAP Web Services and Clients using JAX-WS API 
* Define Java to XML Schema mappings to marshall and unmarshall Java Objects by using JAXB API


### Create Java Web Applications using Servlets

* Create Java Servlets, describe how they are mapped to urls and use HTTP methods
* Handle HTTP headers, parameters, cookies
* Manage servlet life cycle with container callback methods and WebFilters


### Create Java Web Applications using JSPs

* Describe JSP life cycle
* Describe JSP syntax, use tag libraries and  Expression Language (EL) 
* Handle errors using Servlets and Java Server Pages

### Implement REST Services using JAX-RS API

* Understand and Apply REST service conventions 
* Create REST Services and clients using JAX-RS API

### Create Java Applications using WebSockets

* Understand and utilise WebSockets communication style and lifecycle
* Create WebSocket Server and Client Endpoint Handlers using JSR 356 API and JavaScript
* Produce and consume, encode and decode WebSocket messages 

### Develop Web Applications using JSFs

* Describe JSF arcitecture, lifecycle and  navigation
* Understand JSF syntax and use JSF Tag Libraries
* Handle localization and produce messages
* Use Expression Language (EL) and interact with CDI beans

### Secure Java EE 7 Applications

* Describe Java EE declarative and programmatic security and configure authentication using application roles and security constraints and Login Modules
* Describe WebServices security standards

### Use Concurrency API in Java EE 7 Applications

* Demonstrate understanding of Java Concurrency Utilities and use Managed Executors

### Use Batch API in Java EE 7 Applications

* Describe batch jobs using JSL XML documents and JSR 352 API

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
* The container invokes the `service` method, passing request and response objects. Service methods are discussed in Writing Service Methods.

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

### Locali

## JSP

[docu](https://www.tutorialspoint.com/jsp/jsp_quick_guide.htm)

### Elements of JSP

#### The Scriptlet

A scriptlet can contain any number of JAVA language statements, variable or method declarations, or expressions that are valid in the page scripting language.

```XML
<% code fragment %>
```

```XML
<html>
   <head><title>Hello World</title></head>
   
   <body>
      Hello World!<br/>
      <%
         out.println("Your IP address is " + request.getRemoteAddr());
      %>
   </body>
</html>
```

#### JSP Declarations

A declaration declares one or more variables or methods that you can use in Java code later in the JSP file. You must declare the variable or method before you use it in the JSP file.

```XML
<%! declaration; [ declaration; ]+ ... %>
```

```XML
<%! int i = 0; %> 
<%! int a, b, c; %> 
<%! Circle a = new Circle(2.0); %> 
```

#### JSP Expression

A JSP expression element contains a scripting language expression that is evaluated, converted to a String, and inserted where the expression appears in the JSP file.

Because the value of an expression is converted to a String, you can use an expression within a line of text, whether or not it is tagged with HTML, in a JSP file.

The expression element can contain any expression that is valid according to the Java Language Specification but you cannot use a semicolon to end an expression.

```XML
<%= expression %>
```

```XML
<html> 
   <head><title>A Comment Test</title></head> 
   
   <body>
      <p>Today's date: <%= (new java.util.Date()).toLocaleString()%></p>
   </body> 
</html> 
```

### Taglibs	
	
 
Note that the value of the uri attribute is just a name that must be mapped somehow with either the actual TLD file or the jar file containing the TLD file.  Even if the value of the uri attribute is an absolute url (beginning with http:// ), the container does not download anything.

Usually, the <taglib> element of web.xml ties the uri given in the jsp page with either the actual TLD file or the jar file containing the TLD file. In this case, <taglib-uri> should be same as the value of the uri attribute of the taglib directive while the <taglib-location> should point to the TLD file or the jar file.

However, a taglib jar file can also specify the URIs in the tag library descriptor for the tag library contains using a <uri> element. If that is the case, you do not have to explicitly specify the <taglib> element in the web.xml. You can import this library in the jsp pages using the following taglib directory:
<%@ taglib prefix="anyprefix" uri="http://www.xyzcorp.com/htmlLib" %>. Note that the uri must be same as the one given in the tld file.

### Error Handling

* Global Error Page

```XML
<web-app ...>
    <error-page>
        <location>/general-error.html</location>
    </error-page>
</web-app>
```

## Secure JAVA EE 7 Applications

### Servlet

* `HttpServletRequest.getUserPrincipal()`

### EJBs

* `EJBContext.getCallerPrincipal`

### SOAP 

Web Services Security (WS-Security, WSS) is an extension to SOAP to apply security to Web services.

The protocol specifies how integrity and confidentiality can be enforced on messages and allows the communication of various security token formats, such as Security Assertion Markup Language (SAML), Kerberos, and X.509. Its main focus is the use of XML Signature and XML Encryption to provide end-to-end security.

The following summarize the Web service security requirements:

The use of transport security to protect the communication channel between the Web service consumer and Web service provider.

Message-level security to ensure confidentiality by digitally encrypting message parts; integrity using digital signatures; and authentication by requiring username, **X.509, or SAML tokens.**


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
* `@WebServiceRef`

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
* `@PrePassivate` only for stateful session beans, may throw **system runtime exception** but not application exceptions 
* `@PostActivate` only for stateful session beans, may throw **system runtime exception** but not application exceptions 

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
* Only the NOT_SUPPORTED and REQUIRED transaction attributes may be used for message-driven bean message listener methods. The use of the other transaction attributes is not meaningful for message driven bean message listener methods because there is no pre-existing client transaction context (REQUIRES_NEW, SUPPORTS) and no client to handle exceptions (MANDATORY, NEVER).

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

### Session synchronization  

* enables a stateful session bean to be notified of container boundaries 

```Java
public interface SessionSynchronization {

void afterBegin()

void beforeCompletion()

afterCompletion(boolean committed)
}
```

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

* `@Asynchronous` has to be in the bean class

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


* `@AccessTimeout`

### Embeddable API

```Java
EJBContainer ejbC = EJBContainer.createEJBContainer();
Context ctx = ejbC.getContext();
ctx.lookup(...)
```

### EJB Lite 

The following technologies are required components of the Web Profile:
* Servlet 3.1
* JavaServer Pages (JSP) 2.3
* Expression Language (EL) 3.0
* Debugging Support for Other Languages (JSR-45) 1.0
* Standard Tag Library for JavaServer Pages (JSTL) 1.2
* JavaServer Faces (JSF) 2.2
* Java API for RESTful Web Services (JAX-RS) 2.0
* Java API for WebSocket (WebSocket) 1.0
* Java API for JSON Processing (JSON-P) 1.0
* Common Annotations for the Java Platform (JSR-250) 1.2
* Enterprise JavaBeans (EJB) 3.2 Lite
* Java Transaction API (JTA) 1.2
* Java Persistence API (JPA) 2.1
* Bean Validation 1.1
* Managed Beans 1.0
* Interceptors 1.2
* Contexts and Dependency Injection for the Java EE Platform 1.1
* Dependency Injection for Java 1.0

Page 166

### Exceptions 

* `@ApplicationException(rollback=true)` ~ rollback default is false
* by default application exceptions don't cause a CMT to rollback
* application exception is a exception that ejb client expected to handle 
* by default all checked exceptions expected of `java.rmi.RemoteException` are assumed to be application exceptions 
* all exceptions inherited from `java.rmi.RemoteException` or `java.lang.RuntimeException` are system exceptions 
* system exceptions arem't passed to the client but they are wrapped in an `javax.ejb.EJBException`
* if the container system exception that isn't caught it still issue a rollback and destroy the bean
* `java.util.concurrent.ExecutionException` for exceptions in async calls

```Java 
\\ change default behavior
@ApplicationException(rollback=true)
public class MyException extends RuntimeException {

}
```

### JNDI

* EJBs has only read access to their environment variables  

```Java 
ctx.rebind("java:comp/env/edu.SomeBean/maxValue", new Integer(100));
```

### Security 

* `isCallerInRole`

```Java 
@RolesAllowed("BeanUser")
     public BigDecimal dollarToYen(BigDecimal dollars) {
        BigDecimal result = new BigDecimal("0.0");
        Principal callerPrincipal = ctx.getCallerPrincipal();
        if (ctx.isCallerInRole("BeanUser")) {
            result = dollars.multiply(yenRate);
            return result.setScale(2, BigDecimal.ROUND_UP);
        }else{
            return result.setScale(2, BigDecimal.ROUND_UP);
        }
      }

```

* `@RunAs`

### Packaging 

* ejb-jar.xml
* The ejb-jar.xml is packaged as WEB-INF/META-INF/ejb-jar.xml
* The ejb-jar.xml is packaged as WEB-INF/ejb-jar.xml. (**in a war file**)
* If the EJB classes have appropriate component defining annotations, the classes must be packaged in WEB-INF/ejb/classes or in a jar file WEB-INF/ejb/lib/.

### EJBContext

[EJBContext](https://docs.oracle.com/javaee/7/api/javax/ejb/EJBContext.html)

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

Injectable fields:
1. are annotated with @Inject.
2. are not final.
3. may have any otherwise valid name.

Injectable methods:
1. are annotated with @Inject.
2. are not abstract.
3. do not declare type parameters of their own.
4. may return a result
5. may have any otherwise valid name.
6. accept zero or more dependencies as arguments.

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

## Concurrency Utilities 

### Asynchronous Tasks 

* `javax.enterprise.concurrent.ManagedExecutorService` instead of `java.util.concurrent.ExecutorService`
* JNDI lookup "java:comp/DefaultMangedExecutorService"
* unit of work by the implementation of `Runnable` or `Callable`
* a Callable task can return a result, whereas a Runnable task does not 
* a Callable task can throw checked exceptions, whereas a Runnable task cannot
* submit task instances to a MangedExecutorService using any of the `sumit (Callable), execute (Runnable), invokeAll, invokeAny` method
* a task runs within the application component context that submitted the task
* the security context is propagated to the tasks 
* the transaction context is not propagated to the tasks
* interface `ManagedTask` provides notification about life-cycle events 
* interface `ManagedTaskListener` to receive life-cycle events

### Schedule Task  

* `javax.enterprise.concurrent.MangedExecutorService` provides a managed version of `ScheduledExecutorService`
* `ScheduledExecutorService` can be used to schedule tasks 
* JNDI lookup "java:comp/DefaultMangedScheduledExecutorService"
* methods `submit, execute, invokeAll, invokeAny, schedule, scheduleAtFixedRate, scheduleWithFixedDelay`
* `ScheduledFuture`

### Managed Threads 

* `javax.enterprise.concurrent.ManagedThreadFactory` can be used to create managed threads for execution in a JEE environment 
* JNDI lookup "java:comp/DefaultMangedThreadFactory"
* `factory.newThread(new MyTask())` ~ thread creation 
* --> `thread.start()`

### Dynamic Contextual Object 

* `ContextService`
* JNDI lookup "java:comp/DefaultContextService"
* `createContextualProxy` method to create a contextual object proxy 

## Bean Validation 

* annotation and xml (META-INF/validation.xml)

### Built-in Constraints 

* defined in the javax.validation.constraints package
* `@Size` ~ String, Collection, Map and Array are supported types 
* `@Digits`
* each constraint declaration can also override the `message, group, and payload` fields
* `payload` ~ is used to associate meta data 
* `validationAppliesTo` new in JEE7

### Defining a Custom Constraint 

* defining a annotation `@Constraint(validateBy=ZipCodeValidator.class)`
* implement `ConstraintValidator` interface with two methods (`initialize, and isValid(String value, ConstraintValidatorContext context)`)
* if a bean X contains a field of type Y, by default, the validation of type X does nor trigger the validation of type Y
* the validation of Y can achieved with `@Valid`
* `@Valid` provides polymorphic validation 

### Validation Groups

* by default all validation groups are executed in no particular order 
* a constrain may be defined in an explicitly created validation group in order to perform partial validation of the bean or control the order 
* a validation group is defined as an interface 
* `@ZipCode(groups=ZipCodeGroup.class)` --> excludes default group
* `@ZipCode(groups={Default.class,ZipCodeGroup.class})`
* groups can inherit other groups through interface inheritance 
* partial validation can be achieved by multiple groups 
* the validationGroup can be passed to jsf 

```Xml
<h:inputText value="#{person.name}" id="name">
	<f:validateBean validationGroups="org.sample.Page1Group" />
</h:inputText>
```
* `@GroupSequence`

### Method and Constructor Constraint 

* declared constraints need to be explicitly triggered 
* cross-parameter constraints can be declared
* by default only constructors and nongetter methods are validated, this can be changed via `@ValidateExecutable`

## Java Transaction 

### User-Managed Transactions 

* The `UserTransaction` interface enables the application to control transaction boundaries programmatically 
* Typically used in EJBs with bean-managed transactions (BMT)
* `@Inject UserTransaction ut` vs. JNDI `java.comp/UserTransaction`
* `ut.begin() ... ut.commit() ... ut.rollback()`
* after commit or rollback the thread is no langer associated with a transaction 

### Container-Managed Transactions 

* `@Transactional` to define transaction boundaries on CDI-managed beans, as well as classes defined as managed beans 
* class and method level 
* life-cycle methods are invoked in an unspecified transaction context unlass the method is annotated explicitly with `@Transactional` the `TxType` element of the annotation provides the semantic quivalent of the transaction attributes in EB 
* REQUIRED ~ default
* REQUIRED_NEW ~
* MANDATORY ~ outside a transaction `TransactionalException` with nested `TransactionalRequiredException`
* SUPPORTS ~
* NOT_SUPPORTED ~
* NEVER ~ inside a transaction context `TransactionalException` with nested `InvalidTransactionalException`
* by default checked exceptions do not result in the transactional interceptor marking the transaction for rollback
* instances of `RuntimeException` and its subclasses do 
* `rollbackOn` can be used to set Exceptions that cause a rollback 
* `dontRollbackOn`

### @TransactionScoped

* new CDI scope javax.transaction.TransactionScope ~ defines a bean instances whose life-cycle is scoped to the currentky active JTA transaction

## Java Persistence 

### Entities 
* no-arg public constructor 
* `@Entity`
* ...
* a undirectional relationship requires the owning side to specify the annotation 

### Persistence Unit, Persistence Context, and Entity Manager 

* entity is managed within a *persistence context*
* within the context the entity instance is managed by the *entity manager* 
* the entity manger may be *container-managed* (JEE) vs *application-managed* (SE)

```Java
@PersitenceContext
EntityManager em;
```

* via JNDI `@PersitenceContext(name="persitence/myJNDI")`

```Java
@PersitenceUnit
EntityManagerFactory emf;

EntityManager em = emf.createEntityManager();
```

* entity manager and persitence context are not required to be threadsafe --> servlets (not threadsafe) should use the factory to obtain an entity manager 
* *persistence unit* is defined by a *persistence.xml*
* transaction-type JTA means that a JTA data source is provided 
* JEE7 defines java:comp/DefaultDataSource
* by default, a container-managed persistence context is scoped to a single transaction --> entities detached at the end of the transaction
* an *extended persitence context*  for stateful session beans is possible 
* `@PersitenceContext(type=PersitenceContextType.EXTENDED)` 
* default `SynchronizedTYPE.SYNCHRONIZED` ~ Context is joined to the current JTA transaction
* `SynchronizedTYPE.UNSYNCHRONIZED` can be enlisted in a JTA transaction via `EntityManager.joinTransaction`
 
 
 ### Schema Generation 
 
 * via properties in the persistence.xml*
 
 ### Create, Read, Update, and Delete Entities 
 
 * jpql 
 * EntityManager.createNamedXXX
 * crtieria API (since JPA 2.1 updating and deleting is supported)
 * native SQL statement  `@SQLResultSetMapping` is used to specify the mapping of the result
 * `em.persist()`
 * Named Queries vs dynamic queries
 * update via em.nerge, jql or criteria (2.1) 
 * delete via em.remove, jql or criteria (2.1)
 
  ### Entity Listeners
  * `@PostLoad`
  * `@PrePersist`
  * `@PostPersist`
  * `@PreUpdate`
  * `@PostUpdate`
  * `@PreRemove`
  * `@PostRemove`
  * callback methods can be public, private, protected, or package-level access (not static or final)
  
  ```Java
@Entity
@EntityListeners(Listener.class) // place to implement callback methods as well 
```

* can be defined using the XML descriptors bundled in META-INF/orm.xml 
* default entity listeners can be defined by xml

### Stored Procedures 

* `@NamedStoredProcedureQuery` vs dynamic 

```Java 
@Entity 
@NamedStoredProcedureQuery(name="...", produreName="...")

```

* if you do not define the stored procedure via NamedStoredProcedureQuery you must provide the parameter an result information dynamically 

### Validating Entities 

* `validation-mode` element in the persitence.xml
* by default,all entities in a web application are in the default validation group 
* default group is targeted in pre-persist and pre-update events 
* thsi can be overriden by setting validation properties  

### Transactions and Locking

* default *optimistic concurrency control* ~ anyone can read and update an entity 
* `@Version` enables optimistic locking (only the persistence provider is permitted to update that field)
* `OptimsticLockException` has to be handled by the application
* LockModeType.PESSIMISTIC_WRITE --> DB Lock but limits concurrent access

### Caching 

* first level by entity manager in the persistence context 
* second level caching by the persistence provider can be enabled by the `shared-cache-mode` in the persistence.xml:
* ALL ~ all entities ...
* NONE ~ 
* ENABLE_SELECTIVE ~ only cached entities marked with `@Cachable(true)`
* DISABLE_SELECTIVE ~ `@Cachable(false)`
* UNSPECIFIED ~  persistence provider defaults  

* `emf.getCache()`
* `cache.contains(Student.class, 1234)`
* `cache.evict(Student.class, 1234)`
* `cache.evict(Student.class)`
* `cache.evictAll()`

## Java Message Service

* MOM allows sending and receiving messages between distributed systems
* JMS ~ MON that allows Java programs to create, send, receive, and read an enterprise messaging systsm's messages

JMS defines the following concepts:
* *JMS provider* ~ implementation of the JMS interfaces 
* *JMS message* ~ object the contains the data, JMS producer/publisher creates and sends whereas JMS consumer/subscriber receives  
* *Administered objects* ~ typically refer to `ConnectionFactory` and `Destination` and are identified by a JNDI name, `ConnectionFactory` used to create a connection with the provider, `Destination` is the object used by the client to specify the destination of messages it is sending and the source of messages it is receiving 

TWO messaging models:
* *Point-to-Point* (Queue) only on subscriber but one and more publisher
* *Publish-Subscribe* (Topic) n to m 

### JMS Messages 

* composed of three parts: 

1. *Header* ~ standard header fields: (JMSDestination, JMSDeliveryMode, JMSMeassageID, JMSTimestamp, JMSCorrelationID, JMSReplyTo, JMSRedelivered, JMSType, JMSExpiration, JMSPriority)
2. *Properties* ~ optional header fields added by the client
3. *Body* ~ actual payload of the message, different types of body messages are (`SteamMessage`, `MapMessage`, `TextMessage`, `ObjectMessage`, `ByteMessage`) 

### Sending a Message


```Java
/**
 * Session Bean implementation class MessageSender
 */
@Stateless
@LocalBean
@JMSDestinationDefinitions({
	@JMSDestinationDefinition(name = "java:/jms/queue/example", interfaceName = "javax.jms.Queue") })
public class MessageSender {

	/**
	 * Default constructor.
	 */
	public MessageSender() {

	}

	@Inject
	private Logger log;

	@Inject
	JMSContext context;

	@Resource(lookup = "java:/jms/queue/example", type = Queue.class)
	Destination exampleQueue;

	@Schedule(hour = "*", minute = "*", second = "*/30")
	public void sendMessage() {
		log.info("sendMessage");
		context.createProducer().send(exampleQueue, "Hello World");
	}
}
```

* `@JMSDestinationDefinitions`
* `@JMSDestinationDefinition` ~  * An application may use this annotation to specify a JMS {@code Destination} resource that it requires in its operational  environment. This provides information that can be used at the application's deployment to provision the required resource and allows an application to be deployed into a Java EE environment with more minimal administrative configuration.
* `JMSContext` ~ combines `Connection` and `Session`, container managed JMSContext is injected via `@Inject` --> context created and closed by the container
* `JMSConnectionFactory` may be used, predefined factory under `java:comp/DefaultJMSConnectionFactory`

```Java
@Inject
@JMSConnectionFactory("jms/myConnectionFactory")
private JMSContext context;
```

* `@JMSPasswordCredential`
* `Destination` ~ encapsulates provider specific address (Topic or Queue may be injected)
* `JMSProducer` ~ created via `createProducer`
* `context.createProducer().setAsync(new CompletionListener() {})` ~ default synch sending 

Usage of classic API

* @Resource(mappedName = "java:jboss/DefaultJMSConnectionFactory")
* @Resource(lookup = "java:/jms/queue/example", type= Queue.class)
* `createConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);` if the first parameter is true you have to commit the session explicitly  

JMS message acknowledgment modes: 
* AUTO_ACKNOWLEDGE ~ With this session mode, the JMSContext's session automatically acknowledges a client's receipt of a message either when the session has successfully returned from a call to {@code receive} or when the message listener the session has called to process the message successfully returns.
* CLIENT_ACKNOWLEDGE ~ 
* DUPS_OK_ACKNOWLEDGE ~  This session mode instructs the JMSContext's session to lazily acknowledge the delivery of messages. This is likely to result in the delivery of some duplicate messages if the JMS provider fails, so it should only be used by consumers that can tolerate duplicate messages. Use of this mode can reduce session overhead by minimizing the work the session does to prevent duplicates. 
* SESSION_TRANSACTED ~ This session mode instructs the JMSContext's session to deliver and consume messages in a local transaction which will be subsequently committed by calling {@code commit} or rolled back by calling {@code rollback}.

### Receiving a Message Synchronously 

* `context.createConsumer(exampleQueue).receiveBody(String.class, 1000)`
* if this method is called within a transaction, the JMSConsumer retains (behält) the message until the transaction commits 

Usage of classic API

* `session.createConsumer(...)`
* `connection.start()` --> `Message m = connection.receive()` (while loop)
* `TopicConnectionFactory`
* creation of durable subcribers is possible 
* `session.createBrowser(inboundQueue)` ~ look at messages without removing them 

### Receiving a Message Asynchronously 

* via MDB 

```Java 
@MessageDriven(mappedName = "exampleQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/example"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MyMessageBean implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println(String.format("%s receives %s", this.getClass().getSimpleName(), textMessage.getText()));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
```

### Quality of Service 

* by default *durable publish/producer*
* but NON_PERSISTENT support `context.createProducer().setDeliveryMode(DeliveryMode.NON_PERSISTENT)`
* message priority from 0 - 9 default = 4
* by default message never expires 

### Temporary Destinations 

* `context.createTemporaryQueue()` and `context.createTemporaryTopic()`
* closed an lost when the connection is lost but their is a delete method as well 
* JMS 1.1 : `session.createTemporaryQueue()` and `session.createTemporaryTopic()`

### ...
```Java 
public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException
```
Parameters:
transacted - if true, the session is transacted.
acknowledgeMode - indicates whether the consumer or the client will acknowledge any messages it receives. This parameter will be ignored if the session is transacted. Legal values are Session.AUTO_ACKNOWLEDGE, Session.CLIENT_ACKNOWLEDGE and Session.DUPS_OK_ACKNOWLEDGE.
Returns:
a newly created queue session.

## Batch Processing 

* [Batch-Docu](https://docs.oracle.com/javaee/7/tutorial/batch-processing004.html)

### Chunk-Oriented Processing 

* Reader ~ ItemReader and AbstractItemReader
* Processor ~ ItemProcessor 
* Writer ~ ItemWriter and AbstractItemWriter, `writeItems(List list)`

```XML
<job id="myJob" ...
	<step id="myStep">
		<chunk item-count="3">
			<reader ref="myItemReader"/>
			<processor ref="myItemProcessor"/>
			<writer ref="myItemWriter"/>
	
```

* job may contain any number of steps 
* chunk ~ defines a chunk type step which is periodically checkpointed
* default *checkpoint policy* is item which means checkpoint after an number of items (item-count) (default is 10)
* *checkpoint policy* can have the value *custom* 
* myItemReader ~ CDI bean (`@Named`)
* myItemProcessor ~ CDI bean (`@Named`), **optional**
* myItemWriter ~ CDI bean (`@Named`)
* job xml is defined and packaged in the `META-INF/batch-jobs` directory 
* the name of the file has to be the job id --> `myJob.xml`

```Java 
JobOperator jo = BatchRuntime.getJobOperator();
long jid = jo.start("myJob", new Properties());
```

* `jo.restart(jid, props)` ~ restart
* `jo.abondon(jid)` ~ cancel 
* `JobExceution je = jo.getJobExceution(jid)` ~  
* `jo.getCreateTime()` ~  
* `jo.getStartTime()` ~  
* `jo.getJobInstanceCount("myJob")` ~ returns the number of instances
* `Set<String> jobs = jo.getJonNames()` ~ all known job names

### Custom Checkpointing 

* checkpoints enables restart from the last point of consistency 

```XML
<chunk item-count="3" checkpoint-policy="custom">
	<reader ref="myItemReader"/>
	<processor ref="myItemProcessor"/>
	<writer ref="myItemWriter"/>
	<checkpoint-algorithm ref="myCheckpointAlgorithm" />
```

* CDI bean name implementing the `CheckpointAlgorithm` or extending `AbstractCheckpointAlgorithm` (method `isReadyToCheckpoint`)

### Exception Handling 

* by default, if any part of a chunk steps throws an exception the job execution ends with a batch status of FAILED
* default behavior can be overridden for reader, processor and writer 

```XML
<chunk item-count="3" ship-limit="3">
	<reader />
	<processor />
	<writer />
	<skippable-exception-classes>
		<include class="..." />
		<exclude class="..." />
	</skippable-exception-classes>
	<retryable-exception-classes>
		<include class="..." />
	</retryable-exception-classes>
```

* `SkipReadListener, SkipProcessListener, SkipWriteListener`
* `RetryReadListener, RetryProcessListener, RetryWriteListener`

### Batchlet Processing 

* roll-your-own batch pattern 

```XML
<job id="myJob" ...
	<step id="myStep">
		<batchlet ref="myBachlet" />
```

* CDI bean of class a class implementing `Batchlet` (method `process` returns String) 

### Listeners

* `JobListener`
* `StepListener`
* `ChunkListener`
* `ItemReadListener`
* `ItemProcessListener`
* `ItemWriteListener`
* ...

```XML
<job id="myJob" ...
	<listeners>
		<listener ref="myJobListener" />
	</listeners>
	<step id="myStep">
		<listeners>
			<listener ref="myStepListener" />
			<listener ref="myChunkListener" />
			<listener ref="myItemReadListener" />
			<listener ref="myItemProcessorListener" />
			<listener ref="myItemWriterListener" />
		</listeners>
	</step>
	<chunk>
		...
	</chunk>
</job>
```

### Job Sequence

* by default every step is the last step 
* `next` attribute can be used to define a following step 

### Flow 

* flow defines a sequence of execution elements that execute together as a unit 

```XML
<job id="myJob" ...
	<flow id="flow1" next="step3">
		<step id="step1" next="step2">
			...
		</step>
		<step id="step2">
		...
		</step>
	</flow>
	<step id="step3">
		...
	</step>
</job>
```

### Split

* a split execution defines a set of flows that execute concurrently 

```XML
<job id="myJob" ...
	<split id="split1" next="step3">
		<flow id="flow1">
			<step id="step1">
				...
			</step>
		</flow>
		<flow id="flow2">
			<step id="step2">
				...
			</step>
		</flow>
	</split>
	<step id="step3">
		...
	</step>
</job>
```

### Decision 

* customized way of determining sequencing among steps, flows, and splits 
* four transition elements are defined to direct job execution or to terminate job execution:

* *next* ~ to next element 
* *fail* ~ causes a job to end with a FAILED
* *end* ~ causes a job to end with a COMPLETED 
* *stop* ~ causes a job to end with a STOPPED   

```XML
<job id="myJob" ...
	<step id="step1" next="step2">
		...
	</step>
	<decision id="decider1" ref="myDecider">
		<next on="DATA_LOADED" to="step2" />
		<end on="NOT_LOADED" />
	</decision>
	<step id="step2">
	...
	</step>
</job>
```
* implements `Decider` (method `decide(StepExecution[] ses)`)

### Partitioning the Job

* to run on multiple threads 
* one partition per thread 
* each partition can have unique parameters and data 
* partition optional element inside step 

```XML
<step id="stepE" next="stepF">
  <chunk>
    <reader ...></reader>
    <processor ...></processor>
    <writer ...></writer>
  </chunk>
  <partition>
    <plan partitions="2" threads="2">
      <properties partition="0">
        <property name="firstItem" value="0"/>
        <property name="lastItem" value="500"/>
      </properties>
      <properties partition="1">
        <property name="firstItem" value="501"/>
        <property name="lastItem" value="999"/>
      </properties>
    </plan>
  </partition>
  <reducer ref="MyPartitionReducerImpl"/>
  <collector ref="MyPartitionCollectorImpl"/>
  <analyzer ref="MyPartitionAnalyzerImpl"/>
</step>
```

```XML
<step id="stepE" next="stepF">
  <chunk>
    <reader ...></reader>
    <processor ...></processor>
    <writer ...></writer>
  </chunk>
  <partition>
    <mapper ref="MyPartitionMapperImpl"/>
    <reducer ref="MyPartitionReducerImpl"/>
    <collector ref="MyPartitionCollectorImpl"/>
    <analyzer ref="MyPartitionAnalyzerImpl"/>
  </partition>
</step>
```