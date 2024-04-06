package net.vegatec.media_library.query.domain;

import net.vegatec.media_library.query.domain.MusicRequest;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MusicRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MusicRequest getMusicRequestSample1() {
        return new MusicRequest()
            .id(1L)
            .song("song1")
            .artist("artist1")
            .album("album1")
            .genre("genre1")
            .requestedBy("requestedBy1")
            .url("url1");
    }

    public static MusicRequest getMusicRequestSample2() {
        return new MusicRequest()
            .id(2L)
            .song("song2")
            .artist("artist2")
            .album("album2")
            .genre("genre2")
            .requestedBy("requestedBy2")
            .url("url2");
    }

    public static MusicRequest getMusicRequestRandomSampleGenerator() {
        return new MusicRequest()
            .id(longCount.incrementAndGet())
            .song(UUID.randomUUID().toString())
            .artist(UUID.randomUUID().toString())
            .album(UUID.randomUUID().toString())
            .genre(UUID.randomUUID().toString())
            .requestedBy(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
