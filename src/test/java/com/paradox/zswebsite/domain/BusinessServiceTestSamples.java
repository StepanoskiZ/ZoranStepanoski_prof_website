package com.paradox.zswebsite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BusinessServiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BusinessService getBusinessServiceSample1() {
        return new BusinessService().id(1L).title("title1").description("description1").icon("icon1");
    }

    public static BusinessService getBusinessServiceSample2() {
        return new BusinessService().id(2L).title("title2").description("description2").icon("icon2");
    }

    public static BusinessService getBusinessServiceRandomSampleGenerator() {
        return new BusinessService()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .icon(UUID.randomUUID().toString());
    }
}
