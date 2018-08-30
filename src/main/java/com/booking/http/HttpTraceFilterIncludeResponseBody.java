package com.booking.http;

import com.booking.io.DelegatingServletOutputStream;
import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class HttpTraceFilterIncludeResponseBody extends HttpTraceFilter {
    private final CustomInMemoryHttpTraceRepository repository;
    private final HttpExchangeTracer tracer;

    /**
     * Create a new {@link HttpTraceFilter} instance.
     *
     * @param repository the trace repository
     * @param tracer     used to trace exchanges
     */
    public HttpTraceFilterIncludeResponseBody(final CustomInMemoryHttpTraceRepository repository, final HttpExchangeTracer tracer) {
        super(repository, tracer);
        this.repository = repository;
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        TraceableHttpServletRequest traceableRequest = new TraceableHttpServletRequest(
                request);
        HttpTrace trace = this.tracer.receivedRequest(traceableRequest);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(baos);

        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        try {
            final HttpServletResponseWrapper httpServletResponseWrapper = new HttpServletResponseWrapper(response) {
                @Override
                public ServletOutputStream getOutputStream() throws IOException {
                    return new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), ps));
                }

                @Override
                public PrintWriter getWriter() throws IOException {
                    return new PrintWriter(new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), ps)));
                }
            };
            filterChain.doFilter(request, httpServletResponseWrapper);
            status = response.getStatus();
        } finally {
            TraceableHttpServletResponse traceableResponse = new TraceableHttpServletResponse(response, status);
            this.tracer.sendingResponse(trace, traceableResponse,
                    request::getUserPrincipal, () -> getSessionId(request));
            this.repository.add(trace,  new String(baos.toByteArray(), StandardCharsets.UTF_8));
        }
    }

    private String getSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? session.getId() : null;
    }
}
