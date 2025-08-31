package com.paradox.zswebsite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProjectImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProjectImage getProjectImageSample1() {
        return new ProjectImage().id(1L).imageUrl("imageUrl1").caption("caption1");
    }

    public static ProjectImage getProjectImageSample2() {
        return new ProjectImage().id(2L).imageUrl("imageUrl2").caption("caption2");
    }

    public static ProjectImage getProjectImageRandomSampleGenerator() {
        return new ProjectImage()
            .id(longCount.incrementAndGet())
            .imageUrl(UUID.randomUUID().toString())
            .caption(UUID.randomUUID().toString());
    }
}
