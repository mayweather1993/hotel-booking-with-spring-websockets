package com.booking.models;

import org.springframework.context.ApplicationEvent;

public class NewSubscriptionEvent extends ApplicationEvent {
    private final Subscription subscription;
    private final WebSocketSessionWrapper webSocketSessionWrapper;

    public NewSubscriptionEvent(final Object source, final Subscription subscription, final WebSocketSessionWrapper webSocketSessionWrapper) {
        super(source);
        this.subscription = subscription;
        this.webSocketSessionWrapper = webSocketSessionWrapper;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public WebSocketSessionWrapper getWebSocketSessionWrapper() {
        return webSocketSessionWrapper;
    }
}
