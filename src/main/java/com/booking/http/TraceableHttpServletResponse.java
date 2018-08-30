package com.booking.http;

import org.springframework.boot.actuate.trace.http.TraceableResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TraceableHttpServletResponse implements TraceableResponse {

    private final HttpServletResponse delegate;
    private final int status;

    TraceableHttpServletResponse(HttpServletResponse response, int status) {
        this.delegate = response;
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return extractHeaders();
    }

    private Map<String, List<String>> extractHeaders() {
        Map<String, List<String>> headers = new LinkedHashMap<>();
        for (String name : this.delegate.getHeaderNames()) {
            headers.put(name, new ArrayList<>(this.delegate.getHeaders(name)));
        }
        return headers;
    }

}
