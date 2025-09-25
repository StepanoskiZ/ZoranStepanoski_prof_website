package com.paradox.zswebsite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProjectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Project getProjectSample1() {
        return new Project().id(1L).title("title1").projectUrl("projectUrl1").category("category1");
    }

    public static Project getProjectSample2() {
        return new Project().id(2L).title("title2").projectUrl("projectUrl2").category("category2");
    }

    public static Project getProjectRandomSampleGenerator() {
        return new Project()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .projectUrl(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString());
    }
}
