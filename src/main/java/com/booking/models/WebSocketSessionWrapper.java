package com.booking.models;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@EqualsAndHashCode(of = "id")
public class WebSocketSessionWrapper {

    private final WebSocketSession socketSession;
    private final String id;

    public WebSocketSessionWrapper(final WebSocketSession socketSession) {
        this.socketSession = socketSession;
        this.id = socketSession.getId();
    }

    public boolean isOpen() {
        return socketSession.isOpen();
    }

    public void sendMsg(final String msg) {
        try {
            socketSession.sendMessage(new TextMessage(msg));
        } catch (IOException e) {
            log.error("Can't send msg [{}]", msg);
        }
    }

    public String getId() {
        return id;
    }
}
