package com.booking.services;

import com.booking.models.CustomHttpTrace;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class HttpTraceBroadcastService {

    private final WsSessionsHolder wsSessionsHolder;
    private final ObjectMapper objectMapper;

    public void broadcast(final String uri, final CustomHttpTrace httpTrace) {
        try {
            final String msg = objectMapper.writeValueAsString(httpTrace);
            wsSessionsHolder.findSessionsListening(uri)
                    .forEach(webSocketSessionWrapper -> webSocketSessionWrapper.sendMsg(msg));
        } catch (JsonProcessingException e) {
            log.error("Can't create json");
        }
    }
}
