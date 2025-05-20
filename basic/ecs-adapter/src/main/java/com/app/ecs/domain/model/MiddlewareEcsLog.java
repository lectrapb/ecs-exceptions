package com.app.ecs.domain.model;

public abstract class MiddlewareEcsLog {

    private MiddlewareEcsLog next;

    public void handler(Object request, String service) {
        process(request, service);
        if (next != null) {
            next.handler(request, service);
        }
    }

    protected abstract void process(Object request, String service);

    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.next = next;
        return this;
    }
}
