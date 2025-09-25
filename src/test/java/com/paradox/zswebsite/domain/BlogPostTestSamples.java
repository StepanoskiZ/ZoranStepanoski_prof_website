package com.paradox.zswebsite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BlogPostTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BlogPost getBlogPostSample1() {
        return new BlogPost().id(1L).title("title1").imageUrl("imageUrl1");
    }

    public static BlogPost getBlogPostSample2() {
        return new BlogPost().id(2L).title("title2").imageUrl("imageUrl2");
    }

    public static BlogPost getBlogPostRandomSampleGenerator() {
        return new BlogPost().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).imageUrl(UUID.randomUUID().toString());
    }
}
