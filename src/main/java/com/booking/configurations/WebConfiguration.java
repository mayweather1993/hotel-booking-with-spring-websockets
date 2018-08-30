package com.booking.configurations;

import com.booking.http.CustomInMemoryHttpTraceRepository;
import com.booking.http.HttpTraceFilterIncludeResponseBody;
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {
    @Bean
    public HttpTraceFilter httpTraceFilter(final CustomInMemoryHttpTraceRepository repository, final HttpExchangeTracer tracer) {
        return new HttpTraceFilterIncludeResponseBody(repository, tracer);
    }
}
