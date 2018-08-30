package com.booking.models;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class SubscriptionTest {

    @Test
    public void testListening() {
        //given
        final Subscription subscription = new Subscription(Set.of("http://localhost", "http://google.com"));
        //when
        boolean result = subscription.listening("http://localhost");
        //then
        Assert.assertTrue(result);

        //when
        result = subscription.listening("http://google.com");
        //then
        Assert.assertTrue(result);

        //when
        result = subscription.listening("http://google.coam");
        //then
        Assert.assertFalse(result);
    }

}