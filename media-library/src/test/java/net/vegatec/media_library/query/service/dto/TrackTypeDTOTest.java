package net.vegatec.media_library.query.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import net.vegatec.media_library.query.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrackTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrackTypeDTO.class);
        TrackTypeDTO trackTypeDTO1 = new TrackTypeDTO();
        trackTypeDTO1.setId(1L);
        TrackTypeDTO trackTypeDTO2 = new TrackTypeDTO();
        assertThat(trackTypeDTO1).isNotEqualTo(trackTypeDTO2);
        trackTypeDTO2.setId(trackTypeDTO1.getId());
        assertThat(trackTypeDTO1).isEqualTo(trackTypeDTO2);
        trackTypeDTO2.setId(2L);
        assertThat(trackTypeDTO1).isNotEqualTo(trackTypeDTO2);
        trackTypeDTO1.setId(null);
        assertThat(trackTypeDTO1).isNotEqualTo(trackTypeDTO2);
    }
}
