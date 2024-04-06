package net.vegatec.media_library.query.domain;

import net.vegatec.media_library.query.domain.TrackType;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrackTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TrackType getTrackTypeSample1() {
        return new TrackType().id(1L).name("name1").creditsNeeded(1).vipCreditsNeeded(1);
    }

    public static TrackType getTrackTypeSample2() {
        return new TrackType().id(2L).name("name2").creditsNeeded(2).vipCreditsNeeded(2);
    }

    public static TrackType getTrackTypeRandomSampleGenerator() {
        return new TrackType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .creditsNeeded(intCount.incrementAndGet())
            .vipCreditsNeeded(intCount.incrementAndGet());
    }
}
