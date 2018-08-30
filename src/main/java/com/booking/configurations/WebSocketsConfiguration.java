package com.booking.configurations;

import com.booking.websockets.UriListenerWsEndpoint;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketsConfiguration implements WebSocketConfigurer {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void registerWebSocketHandlers(@NonNull final WebSocketHandlerRegistry registry) {
        registry.addHandler(listeningUriWsEndpoint(), UriListenerWsEndpoint.URL)
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler listeningUriWsEndpoint() {
        return new ExceptionWebSocketHandlerDecorator(new UriListenerWsEndpoint(eventPublisher));
    }

//    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        final long idleTimeout = 60_000L; //1min in ms
        final ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxSessionIdleTimeout(idleTimeout);
        return container;
    }

}
