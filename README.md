# Java-HTTP
Building an HTTP routing system with inspiration from Express.js.

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

### Enable sessiondata
```java
jXpress.enableSession();
jXpress.disableSession();
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
Will provide all files and sub-folder files on the base bath of "/".
```java
jXpress.publicFolder(String folderPath);
```
## Middleware
### Custom
```java
jXpress.use((request, response) -> void);
```

## Routes using annotations
Create a class as such, it will automatically get added to the route pool
```java
//path, method, provides all optional, the ones used below are the default values
@Route(path = "/", method = Method.GET, provides = "text/plain")
public class className implements IRequestHandler

@Route
public class className implements IRequestHandler
```

## Request

## Response