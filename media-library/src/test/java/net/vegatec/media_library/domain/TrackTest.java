package net.vegatec.media_library.domain;

import static net.vegatec.media_library.domain.TrackTestSamples.*;
import static net.vegatec.media_library.domain.TrackTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.vegatec.media_library.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Track.class);
        Track track1 = getTrackSample1();
        Track track2 = new Track();
        assertThat(track1).isNotEqualTo(track2);

        track2.setId(track1.getId());
        assertThat(track1).isEqualTo(track2);

        track2 = getTrackSample2();
        assertThat(track1).isNotEqualTo(track2);
    }

    @Test
    void typeTest() throws Exception {
        Track track = getTrackRandomSampleGenerator();
        TrackType trackTypeBack = getTrackTypeRandomSampleGenerator();

        track.setType(trackTypeBack);
        assertThat(track.getType()).isEqualTo(trackTypeBack);

        track.type(null);
        assertThat(track.getType()).isNull();
    }
}
