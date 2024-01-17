package net.vegatec.media_library.domain;

import static net.vegatec.media_library.domain.TrackTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.vegatec.media_library.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrackTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrackType.class);
        TrackType trackType1 = getTrackTypeSample1();
        TrackType trackType2 = new TrackType();
        assertThat(trackType1).isNotEqualTo(trackType2);

        trackType2.setId(trackType1.getId());
        assertThat(trackType1).isEqualTo(trackType2);

        trackType2 = getTrackTypeSample2();
        assertThat(trackType1).isNotEqualTo(trackType2);
    }
}
