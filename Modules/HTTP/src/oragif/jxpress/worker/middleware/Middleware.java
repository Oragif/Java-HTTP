package oragif.jxpress.worker.middleware;

import oragif.jxpress.worker.IWorker;
import oragif.jxpress.worker.Method;

public abstract class Middleware implements IWorker {
    protected Method method;
    public Middleware() {
        this.method = Method.MIDDLEWARE;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }
}
