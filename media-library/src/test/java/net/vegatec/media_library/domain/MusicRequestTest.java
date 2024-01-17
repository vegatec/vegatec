package net.vegatec.media_library.domain;

import static net.vegatec.media_library.domain.MusicRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.vegatec.media_library.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MusicRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MusicRequest.class);
        MusicRequest musicRequest1 = getMusicRequestSample1();
        MusicRequest musicRequest2 = new MusicRequest();
        assertThat(musicRequest1).isNotEqualTo(musicRequest2);

        musicRequest2.setId(musicRequest1.getId());
        assertThat(musicRequest1).isEqualTo(musicRequest2);

        musicRequest2 = getMusicRequestSample2();
        assertThat(musicRequest1).isNotEqualTo(musicRequest2);
    }
}
