package com.oragif.jxpress.worker.middleware;

import com.oragif.jxpress.worker.IWorker;
import com.oragif.jxpress.worker.Method;

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
