package oragif.jxpress.routing;

import oragif.jxpress.http.RequestHandler;
import oragif.jxpress.routing.Router;
import oragif.jxpress.worker.Worker;

public interface IRouting {
    public void get(String path, RequestHandler method);
    public void post(String path, RequestHandler method);
    public void put(String path, RequestHandler method);
    public void delete(String path, RequestHandler method);
    public void use(String path, Router method);
    public void use(Worker worker);
}