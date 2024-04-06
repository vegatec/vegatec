package net.vegatec.media_library.query.domain;

import static org.assertj.core.api.Assertions.assertThat;

import net.vegatec.media_library.query.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MusicRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MusicRequest.class);
        MusicRequest musicRequest1 = MusicRequestTestSamples.getMusicRequestSample1();
        MusicRequest musicRequest2 = new MusicRequest();
        assertThat(musicRequest1).isNotEqualTo(musicRequest2);

        musicRequest2.setId(musicRequest1.getId());
        assertThat(musicRequest1).isEqualTo(musicRequest2);

        musicRequest2 = MusicRequestTestSamples.getMusicRequestSample2();
        assertThat(musicRequest1).isNotEqualTo(musicRequest2);
    }
}
