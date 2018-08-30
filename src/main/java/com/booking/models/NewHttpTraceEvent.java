package com.booking.models;

import org.springframework.context.ApplicationEvent;

public class NewHttpTraceEvent extends ApplicationEvent {

    private final CustomHttpTrace trace;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     * @param trace
     */
    public NewHttpTraceEvent(final Object source, final CustomHttpTrace trace) {
        super(source);
        this.trace = trace;
    }

    public CustomHttpTrace getTrace() {
        return trace;
    }
}
