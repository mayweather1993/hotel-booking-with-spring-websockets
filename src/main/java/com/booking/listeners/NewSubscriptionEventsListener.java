package com.booking.listeners;

import com.booking.models.NewSubscriptionEvent;
import com.booking.services.WsSessionsHolder;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NewSubscriptionEventsListener implements ApplicationListener<NewSubscriptionEvent> {
    private final WsSessionsHolder wsSessionsHolder;

    @Override
    public void onApplicationEvent(final NewSubscriptionEvent event) {
        wsSessionsHolder.register(event.getSubscription(), event.getWebSocketSessionWrapper());
    }
}
