package net.vegatec.media_library.query.domain;

import net.vegatec.media_library.query.domain.Track;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrackTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicLong intCount = new AtomicLong(random.nextLong() + (2 * Long.MAX_VALUE));

    public static Track getTrackSample1() {
        return new Track()
            .id(1L)
            .filePath("filePath1")
            .subfolder("subfolder1")
            .name("name1")
            .sortName("sortName1")
            //            .artistName("artistName1")
            //            .artistSortName("artistSortName1")
            //            .albumName("albumName1")
            //            .albumSortName("albumSortName1")
            //            .albumArtistName("albumArtistName1")
            //            .albumArtistSortName("albumArtistSortName1")
            //            .albumReleasedYear(1)
            //            .genreName("genreName1")
            //            .genreSortName("genreSortName1")
            .trackNumber(1)
            .playbackLength(1L)
            .bitRate(1);
    }

    public static Track getTrackSample2() {
        return new Track()
            .id(2L)
            .filePath("filePath2")
            .subfolder("subfolder2")
            .name("name2")
            .sortName("sortName2")
            //            .artistName("artistName2")
            //            .artistSortName("artistSortName2")
            //            .albumName("albumName2")
            //            .albumSortName("albumSortName2")
            //            .albumArtistName("albumArtistName2")
            //            .albumArtistSortName("albumArtistSortName2")
            //            .albumReleasedYear(2)
            //            .genreName("genreName2")
            //            .genreSortName("genreSortName2")
            .trackNumber(2)
            .playbackLength(2L)
            .bitRate(2);
    }

    public static Track getTrackRandomSampleGenerator() {
        return new Track()
            .id(longCount.incrementAndGet())
            .filePath(UUID.randomUUID().toString())
            .subfolder(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .sortName(UUID.randomUUID().toString());
            //            .artistName(UUID.randomUUID().toString())
            //            .artistSortName(UUID.randomUUID().toString())
            //            .albumName(UUID.randomUUID().toString())
            //            .albumSortName(UUID.randomUUID().toString())
            //            .albumArtistName(UUID.randomUUID().toString())
            //            .albumArtistSortName(UUID.randomUUID().toString())
            //            .albumReleasedYear(intCount.incrementAndGet())
            //            .genreName(UUID.randomUUID().toString())
            //            .genreSortName(UUID.randomUUID().toString())
           // .trackNumber(intCount.incrementAndGet())
//            .playbackLength(longCount.incrementAndGet())
//            .bitRate(intCount.incrementAndGet());
    }
}
