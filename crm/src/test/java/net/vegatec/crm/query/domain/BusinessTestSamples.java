package net.vegatec.crm.query.domain;

import net.vegatec.crm.query.domain.Business;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BusinessTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Business getBusinessSample1() {
        return new Business().id(1L).name("name1").address("address1").owner("owner1").contact("contact1").phone("phone1");
    }

    public static Business getBusinessSample2() {
        return new Business().id(2L).name("name2").address("address2").owner("owner2").contact("contact2").phone("phone2");
    }

    public static Business getBusinessRandomSampleGenerator() {
        return new Business()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .owner(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString());
    }
}
