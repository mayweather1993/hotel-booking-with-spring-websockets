package com.booking.http;

import com.booking.models.CustomHttpTrace;
import com.booking.models.NewHttpTraceEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@AllArgsConstructor
public class CustomInMemoryHttpTraceRepository extends InMemoryHttpTraceRepository {

    private final ApplicationEventPublisher eventPublisher;

    public void add(final HttpTrace trace, final String body) {
        super.add(trace);
        final CustomHttpTrace customHttpTrace = new CustomHttpTrace(trace, body);
        eventPublisher.publishEvent(new NewHttpTraceEvent(this, customHttpTrace));
    }
}
