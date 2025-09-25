package com.paradox.zswebsite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactMessageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContactMessage getContactMessageSample1() {
        return new ContactMessage().id(1L).visitorName("visitorName1").visitorEmail("visitorEmail1");
    }

    public static ContactMessage getContactMessageSample2() {
        return new ContactMessage().id(2L).visitorName("visitorName2").visitorEmail("visitorEmail2");
    }

    public static ContactMessage getContactMessageRandomSampleGenerator() {
        return new ContactMessage()
            .id(longCount.incrementAndGet())
            .visitorName(UUID.randomUUID().toString())
            .visitorEmail(UUID.randomUUID().toString());
    }
}
