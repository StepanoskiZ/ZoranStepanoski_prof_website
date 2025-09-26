package com.paradox.zswebsite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProjectMediaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProjectMedia getProjectMediaSample1() {
        return new ProjectMedia().id(1L).mediaUrl("mediaUrl1").caption("caption1");
    }

    public static ProjectMedia getProjectMediaSample2() {
        return new ProjectMedia().id(2L).mediaUrl("mediaUrl2").caption("caption2");
    }

    public static ProjectMedia getProjectMediaRandomSampleGenerator() {
        return new ProjectMedia()
            .id(longCount.incrementAndGet())
            .mediaUrl(UUID.randomUUID().toString())
            .caption(UUID.randomUUID().toString());
    }
}
