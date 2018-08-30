package com.booking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.actuate.trace.http.HttpTrace;

public class CustomHttpTrace {
    @JsonIgnore
    private final HttpTrace httpTrace;
    private long responseLength;
    private String responseBody;

    public CustomHttpTrace(final HttpTrace httpTrace, final String body) {
        this.httpTrace = httpTrace;
        this.responseBody = body;
        this.responseLength = body == null ? 0 : body.length();
    }

    @JsonProperty
    public String getMethod() {
        return httpTrace.getRequest().getMethod();
    }

    @JsonProperty
    public String getUri() {
        return httpTrace.getRequest().getUri().toString();
    }

    @JsonProperty
    public long getResponseTime() {
        return httpTrace.getTimeTaken();
    }

    @JsonProperty
    public int getResponseCode() {
        return httpTrace.getResponse().getStatus();
    }

    @JsonProperty
    public long getResponseLength() {
        return responseLength;
    }

    @JsonProperty
    public String getResponseBody() {
        return responseBody;
    }

    public HttpTrace getHttpTrace() {
        return httpTrace;
    }
}
