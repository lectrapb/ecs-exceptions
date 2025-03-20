package com.app.authorization_reactive.shared.helpers.ecs.model;

public abstract class MiddlewareEcsLog {

    private MiddlewareEcsLog next;

    public void handle(Object request, String service) {
        process(request, service);
        if (next != null) {
            next.handle(request, service);
        }
    }

    protected abstract void process(Object request, String service);

    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.next = next;
        return this;
    }
}

