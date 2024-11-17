package net.vegatec.crm.domain;

import net.vegatec.crm.query.domain.Product;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product().id(1L).name("name1").model("model1").serialNumber("serialNumber1").manufacturer("manufacturer1");
    }

    public static Product getProductSample2() {
        return new Product().id(2L).name("name2").model("model2").serialNumber("serialNumber2").manufacturer("manufacturer2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .serialNumber(UUID.randomUUID().toString())
            .manufacturer(UUID.randomUUID().toString());
    }
}
