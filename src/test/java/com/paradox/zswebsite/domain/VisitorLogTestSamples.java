package com.paradox.zswebsite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VisitorLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VisitorLog getVisitorLogSample1() {
        return new VisitorLog().id(1L).ipAddress("ipAddress1").pageVisited("pageVisited1").userAgent("userAgent1");
    }

    public static VisitorLog getVisitorLogSample2() {
        return new VisitorLog().id(2L).ipAddress("ipAddress2").pageVisited("pageVisited2").userAgent("userAgent2");
    }

    public static VisitorLog getVisitorLogRandomSampleGenerator() {
        return new VisitorLog()
            .id(longCount.incrementAndGet())
            .ipAddress(UUID.randomUUID().toString())
            .pageVisited(UUID.randomUUID().toString())
            .userAgent(UUID.randomUUID().toString());
    }
}
