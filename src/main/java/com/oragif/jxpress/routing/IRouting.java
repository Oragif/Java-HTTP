package com.oragif.jxpress.routing;

import com.oragif.jxpress.http.IRequestHandler;
import com.oragif.jxpress.worker.IWorker;

public interface IRouting {
    public void get(String path, IRequestHandler method);
    public void post(String path, IRequestHandler method);
    public void put(String path, IRequestHandler method);
    public void delete(String path, IRequestHandler method);
    public void use(String path, Router method);
    public void use(String path, IWorker worker);
    public void use(IWorker worker);
    public void webFolder(String path, String folderPath);
    public void publicFolder(String folderPath);
}