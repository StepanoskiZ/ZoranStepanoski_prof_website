package com.paradox.zswebsite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PageContentMediaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PageContentMedia getPageContentMediaSample1() {
        return new PageContentMedia().id(1L).mediaUrl("mediaUrl1").caption("caption1");
    }

    public static PageContentMedia getPageContentMediaSample2() {
        return new PageContentMedia().id(2L).mediaUrl("mediaUrl2").caption("caption2");
    }

    public static PageContentMedia getPageContentMediaRandomSampleGenerator() {
        return new PageContentMedia()
            .id(longCount.incrementAndGet())
            .mediaUrl(UUID.randomUUID().toString())
            .caption(UUID.randomUUID().toString());
    }
}
