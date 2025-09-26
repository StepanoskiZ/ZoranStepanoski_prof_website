package com.paradox.zswebsite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PageContentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageContentDTO.class);
        PageContentDTO pageContentDTO1 = new PageContentDTO();
        pageContentDTO1.setId(1L);
        PageContentDTO pageContentDTO2 = new PageContentDTO();
        assertThat(pageContentDTO1).isNotEqualTo(pageContentDTO2);
        pageContentDTO2.setId(pageContentDTO1.getId());
        assertThat(pageContentDTO1).isEqualTo(pageContentDTO2);
        pageContentDTO2.setId(2L);
        assertThat(pageContentDTO1).isNotEqualTo(pageContentDTO2);
        pageContentDTO1.setId(null);
        assertThat(pageContentDTO1).isNotEqualTo(pageContentDTO2);
    }
}
