package com.paradox.zswebsite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PageContentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PageContent getPageContentSample1() {
        return new PageContent().id(1L).sectionKey("sectionKey1");
    }

    public static PageContent getPageContentSample2() {
        return new PageContent().id(2L).sectionKey("sectionKey2");
    }

    public static PageContent getPageContentRandomSampleGenerator() {
        return new PageContent().id(longCount.incrementAndGet()).sectionKey(UUID.randomUUID().toString());
    }
}
