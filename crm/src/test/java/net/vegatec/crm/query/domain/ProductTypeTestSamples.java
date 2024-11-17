package net.vegatec.crm.query.domain;

import net.vegatec.crm.query.domain.ProductType;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductType getProductTypeSample1() {
        return new ProductType().id(1L).name("name1");
    }

    public static ProductType getProductTypeSample2() {
        return new ProductType().id(2L).name("name2");
    }

    public static ProductType getProductTypeRandomSampleGenerator() {
        return new ProductType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
