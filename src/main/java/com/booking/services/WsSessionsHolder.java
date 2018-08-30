package com.booking.services;

import com.booking.models.Subscription;
import com.booking.models.WebSocketSessionWrapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class WsSessionsHolder {

    private final Map<Subscription, Set<WebSocketSessionWrapper>> sessions = new ConcurrentHashMap<>();

    public Set<WebSocketSessionWrapper> findSessionsListening(final String uri) {
        final Set<Subscription> subscriptions = sessions.keySet().stream()
                .filter(subscription -> subscription.listening(uri))
                .collect(Collectors.toSet());

        return subscriptions.stream()
                .flatMap(subscription -> sessions.get(subscription).stream())
                .filter(WebSocketSessionWrapper::isOpen)
                .collect(Collectors.toSet());
    }

    public void register(final Subscription subscription, final WebSocketSessionWrapper webSocketSessionWrapper) {
        final Set<WebSocketSessionWrapper> webSocketSessionWrappers = sessions.getOrDefault(subscription, new HashSet<>());
        webSocketSessionWrappers.add(webSocketSessionWrapper);
        sessions.put(subscription, webSocketSessionWrappers);
    }
}
