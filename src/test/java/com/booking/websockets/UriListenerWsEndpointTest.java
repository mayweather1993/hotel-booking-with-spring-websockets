package com.booking.websockets;

import com.booking.models.NewSubscriptionEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UriListenerWsEndpointTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Captor
    private ArgumentCaptor<NewSubscriptionEvent> eventArgumentCaptor;
    private UriListenerWsEndpoint uriListenerWsEndpoint;

    @Before
    public void setUp() {
        this.uriListenerWsEndpoint = new UriListenerWsEndpoint(eventPublisher);
    }

    @Test
    public void testHandleTextMessageNewSubscription() {
        //given
        final WebSocketSession session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn("1");
        when(session.isOpen()).thenReturn(true);
        final TextMessage message = new TextMessage("sub,http://localhost");
        //when
        uriListenerWsEndpoint.handleTextMessage(session, message);
        //then
        verify(eventPublisher).publishEvent(eventArgumentCaptor.capture());
        final NewSubscriptionEvent subscriptionEvent = eventArgumentCaptor.getValue();
        Assert.assertEquals(Set.of("http://localhost"), subscriptionEvent.getSubscription().getUrisListening());
        Assert.assertEquals("1", subscriptionEvent.getWebSocketSessionWrapper().getId());
        Assert.assertTrue(subscriptionEvent.getWebSocketSessionWrapper().isOpen());
    }

    @Test
    public void testHandleTextMessageNoSubscriptionUri() {
        //given
        final WebSocketSession session = mock(WebSocketSession.class);
        final TextMessage message = new TextMessage("sub,");
        //when
        uriListenerWsEndpoint.handleTextMessage(session, message);
        //then
        verify(eventPublisher, never()).publishEvent(any());
    }
}