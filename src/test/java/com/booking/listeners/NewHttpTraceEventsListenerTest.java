package com.booking.listeners;

import com.booking.http.TraceableHttpServletRequest;
import com.booking.models.CustomHttpTrace;
import com.booking.models.NewHttpTraceEvent;
import com.booking.services.HttpTraceBroadcastService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NewHttpTraceEventsListenerTest {

    @Mock
    private HttpTraceBroadcastService httpTraceBroadcastService;
    private HttpExchangeTracer httpExchangeTracer = new HttpExchangeTracer(Set.of());
    private NewHttpTraceEventsListener newHttpTraceEventsListener;

    @Before
    public void setUp() {
        this.newHttpTraceEventsListener = new NewHttpTraceEventsListener(httpTraceBroadcastService);
    }

    @Test
    public void onApplicationEventBroadcastShouldBeDone() {
        //given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/test");
        final HttpTrace httpTrace = httpExchangeTracer.receivedRequest(new TraceableHttpServletRequest(servletRequest));
        final CustomHttpTrace customHttpTrace = new CustomHttpTrace(httpTrace, "body");
        final NewHttpTraceEvent event = new NewHttpTraceEvent(this, customHttpTrace);

        //when
        newHttpTraceEventsListener.onApplicationEvent(event);
        //then
        verify(httpTraceBroadcastService).broadcast(eq("http://localhost/test"), eq(customHttpTrace));
    }
}