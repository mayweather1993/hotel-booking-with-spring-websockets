package com.booking.websockets;

import com.booking.models.NewSubscriptionEvent;
import com.booking.models.Subscription;
import com.booking.models.WebSocketSessionWrapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class UriListenerWsEndpoint extends TextWebSocketHandler {
    public static final String URL = "/listen";

    private final ApplicationEventPublisher eventPublisher;

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) {
        final String payload = message.getPayload();
        if (StringUtils.startsWithIgnoreCase(payload, "sub")) {
            registerNewSubscription(payload, session);
        }

    }

    private void registerNewSubscription(final String payload, final WebSocketSession session) {
        final String[] split = payload.split(",");
        final Set<String> listeningUrl = Stream.of(split)
                .skip(1) //skipping command e.x. 'sub'
                .collect(Collectors.toSet());

        if (listeningUrl.isEmpty()) {
            return;
        }

        final Subscription subscription = new Subscription(listeningUrl);
        eventPublisher.publishEvent(new NewSubscriptionEvent(this, subscription, new WebSocketSessionWrapper(session)));
    }
}
