package oragif.jxpress;

import oragif.jxpress.worker.IWorker;

public interface IRouting {
    public void get(String path, IWorker method);
    public void post(String path, IWorker method);
    public void put(String path, IWorker method);
    public void delete(String path, IWorker method);
}