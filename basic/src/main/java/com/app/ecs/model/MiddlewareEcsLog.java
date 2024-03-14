package com.app.ecs.model;

public abstract class MiddlewareEcsLog {

    public MiddlewareEcsLog next;
    public abstract void handler(Throwable request);
    public abstract MiddlewareEcsLog setNext(MiddlewareEcsLog next);
}
