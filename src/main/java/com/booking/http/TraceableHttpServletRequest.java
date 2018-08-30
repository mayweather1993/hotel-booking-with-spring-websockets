package com.booking.http;

import org.springframework.boot.actuate.trace.http.TraceableRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TraceableHttpServletRequest implements TraceableRequest {

    private final HttpServletRequest request;

    public TraceableHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getMethod() {
        return this.request.getMethod();
    }

    @Override
    public URI getUri() {
        String queryString = this.request.getQueryString();
        if (!StringUtils.hasText(queryString)) {
            return URI.create(this.request.getRequestURL().toString());
        }
        try {
            StringBuffer urlBuffer = appendQueryString(queryString);
            return new URI(urlBuffer.toString());
        }
        catch (URISyntaxException ex) {
            String encoded = UriUtils.encodeQuery(queryString, StandardCharsets.UTF_8);
            StringBuffer urlBuffer = appendQueryString(encoded);
            return URI.create(urlBuffer.toString());
        }
    }

    private StringBuffer appendQueryString(String queryString) {
        return this.request.getRequestURL().append("?").append(queryString);
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return extractHeaders();
    }

    @Override
    public String getRemoteAddress() {
        return this.request.getRemoteAddr();
    }

    private Map<String, List<String>> extractHeaders() {
        Map<String, List<String>> headers = new LinkedHashMap<>();
        Enumeration<String> names = this.request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, toList(this.request.getHeaders(name)));
        }
        return headers;
    }

    private List<String> toList(Enumeration<String> enumeration) {
        List<String> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }

}