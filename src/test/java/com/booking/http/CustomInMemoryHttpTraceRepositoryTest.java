package com.booking.http;

import com.booking.models.CustomHttpTrace;
import com.booking.models.NewHttpTraceEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Set;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CustomInMemoryHttpTraceRepositoryTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Captor
    private ArgumentCaptor<NewHttpTraceEvent> httpTraceEventArgumentCaptor;
    private CustomInMemoryHttpTraceRepository traceRepository;
    private HttpExchangeTracer httpExchangeTracer = new HttpExchangeTracer(Set.of());

    @Before
    public void setUp() {
        this.traceRepository = new CustomInMemoryHttpTraceRepository(eventPublisher);
    }

    @Test
    public void testAddTraceEventShouldBeFired() {
        //given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/test");
        final HttpTrace httpTrace = httpExchangeTracer.receivedRequest(new TraceableHttpServletRequest(servletRequest));
        //when
        traceRepository.add(httpTrace, "body");
        //then
        verify(eventPublisher).publishEvent(httpTraceEventArgumentCaptor.capture());
        final NewHttpTraceEvent actualEvent = httpTraceEventArgumentCaptor.getValue();
        final CustomHttpTrace trace = actualEvent.getTrace();
        Assert.assertEquals("body", trace.getResponseBody());
        Assert.assertEquals(4, trace.getResponseLength());
        Assert.assertEquals(servletRequest.getMethod(), trace.getMethod());
        Assert.assertEquals(servletRequest.getRequestURL().toString(), trace.getHttpTrace().getRequest().getUri().toString());
    }
}