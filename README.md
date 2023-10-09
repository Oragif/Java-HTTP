# Java-HTTP
Building an HTTP routing system with inspiration from Express.js.

# Load order
Middleware and routes will be loaded in the order they are specified, and all dynamics using annotations will be loaded last. 
Meaning if a middleware is important at the start, add it in code at the beginning before the use of listen.\
It is therefore also important to add predefined middlewares like Session, at the start to ensure it gets ran before other middleware.

# Functionality
## Creating New HTTP Server & Router
```java
JXpress jXpress = new JXpress();
```
### Start server:
```java
jXpress.listen(int port);
```
### Print Route Tree:
Can also be done for individual routers
```java
jXpress.printRouteTree();
```

### Set 404 route
```java
jXpress.set404((request, response) -> void);
```

## Basic
### GET
```java
jXpress.get(String path, (request, response) -> void);
```
### POST
```java
jXpress.post(String path, (request, response) -> void);
```
### PUT
```java
jXpress.put(String path, (request, response) -> void);
```
### DELETE
```java
jXpress.delete(String path, (request, response) -> void);
```
## Routing
The router has the same functionality as the main JXpress
### Creating a route:
```java
Router router = new Router();
// Add it to the pool of routes
jXpress.use(String path, router);
```
## File and Folder Provider
### File
```java
jXpress.use(String path, new FileReader(Path pathToFile));
```
### Web Folder
```java
jXpress.webFolder(String path, String folderPath);
```
### Public Folder
Will provide all files and sub-folders files on the base path of "/".
```java
jXpress.publicFolder(String folderPath);
```
## Middleware
### Custom
```java
jXpress.use((request, response) -> void);
```

## Routes using annotations
Creating a class as such, it will automatically get added to the route pool
```java
//path, method, provides all optional, the ones used below are the default values
@Route(path = "/", method = Method.GET, provides = "text/plain")
public class className implements IRequestHandler

@Route
public class className implements IRequestHandler
```

## Middleware using annotations
Creating a class as such, it will automatically get added to the worker pool
```java
//path, the one used below is the default value
@Middleware(path = "/")
public class className extends Worker

@Middleware
public class className extends Worker
```


## Request
### Method
```java
request.getMethod();
```

### Body
```java
// Get the body as string
request.getRawBody();
// Get the body as byte[]
request.getByteBody();
// Get the body as Json using GSON
request.getJsonBody(Class<?> jsonClass);
```

### Cookies
```java
// Get a cookie
request.getCookie(String key);
// Get all cookies
request.getCookies();
```

### Headers
```java
// Get a header
request.getHeader(String key);
// Get all headers
request.getHeaders();
```

### Parameters
Parameters are send in the url eg. website.com/user ***?id=1,2,4&role=admin***
```java
// Get a parameter
request.getParameter(String key);
// Get all parameters
request.getParameters();
```

### Middleware Data
A way of keeping track of data throughout handling of requests.
#### Add data
```java
request.setMiddlewareData(String key, Object data);
```
#### Retrieving data
```java
// Get a single value
request.getMiddlewareData(String key);
// Get the entire HashMap
request.getAllMiddlewareData();
```

## Response
### Send
Once a message is sent, the stream automatically closes
```java
// Sending data
response.send(String message)
/* Or will use toString */
response.send(Object object)
```
### Close
To stop the stream prematurely use
```java
response.close()
```
### Response Code
```java
// Get the current response code, defaults to 200
response.getResponseCode();
// Set response code
response.setResponseCode(int code);
```

### Content Type
```java
response.setContentType(String type)
```

### Headers
```java
response.addHeader(String header, String value);
response.addHeader(String header, List<String> values);
```

## Predefined Middleware
### Session
Will automatically add or retrieve a session and store it in the middleware data.\
The session id is automatically sent to the client to store as a cookie.\
Session cookie should remove itself once browser is closed, be aware this depends on the browser implementation.
#### Adding it
```java
jXpress.use(new Session());
```
#### Accessing it
```java
// Getting the session
Session session = (Session) request.getMiddlewareData("session");
// Setting data in the session
session.setSessionData(String key, Object data);
// Retrieving data from the session
session.getSessionData(String key);
```
```java
// Getting the session key
String sessionKey = (String) request.getMiddlewareData("sessionKey");
```