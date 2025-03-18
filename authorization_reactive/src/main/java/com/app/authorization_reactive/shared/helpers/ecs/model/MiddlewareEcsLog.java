package com.app.authorization_reactive.shared.helpers.ecs.model;

public abstract class MiddlewareEcsLog {

    public MiddlewareEcsLog next;
    public abstract void handler(Throwable request,
                                 String service);
    public abstract MiddlewareEcsLog setNext(MiddlewareEcsLog next);
}
