package com.booking.listeners;

import com.booking.models.CustomHttpTrace;
import com.booking.models.NewHttpTraceEvent;
import com.booking.services.HttpTraceBroadcastService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class NewHttpTraceEventsListener implements ApplicationListener<NewHttpTraceEvent> {

    private final HttpTraceBroadcastService broadcastService;

    @Override
    public void onApplicationEvent(final NewHttpTraceEvent event) {
        final CustomHttpTrace trace = event.getTrace();
        final HttpTrace httpTrace = trace.getHttpTrace();
        final HttpTrace.Request request = httpTrace.getRequest();
        final String uri = request.getUri().toString();
        broadcastService.broadcast(uri, trace);
    }

}
