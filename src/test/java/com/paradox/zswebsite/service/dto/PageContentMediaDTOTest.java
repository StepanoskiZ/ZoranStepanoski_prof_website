package com.paradox.zswebsite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PageContentMediaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageContentMediaDTO.class);
        PageContentMediaDTO pageContentMediaDTO1 = new PageContentMediaDTO();
        pageContentMediaDTO1.setId(1L);
        PageContentMediaDTO pageContentMediaDTO2 = new PageContentMediaDTO();
        assertThat(pageContentMediaDTO1).isNotEqualTo(pageContentMediaDTO2);
        pageContentMediaDTO2.setId(pageContentMediaDTO1.getId());
        assertThat(pageContentMediaDTO1).isEqualTo(pageContentMediaDTO2);
        pageContentMediaDTO2.setId(2L);
        assertThat(pageContentMediaDTO1).isNotEqualTo(pageContentMediaDTO2);
        pageContentMediaDTO1.setId(null);
        assertThat(pageContentMediaDTO1).isNotEqualTo(pageContentMediaDTO2);
    }
}
