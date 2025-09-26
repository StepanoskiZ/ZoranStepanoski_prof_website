package com.paradox.zswebsite.domain;

import static com.paradox.zswebsite.domain.PageContentMediaTestSamples.*;
import static com.paradox.zswebsite.domain.PageContentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PageContentMediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageContentMedia.class);
        PageContentMedia pageContentMedia1 = getPageContentMediaSample1();
        PageContentMedia pageContentMedia2 = new PageContentMedia();
        assertThat(pageContentMedia1).isNotEqualTo(pageContentMedia2);

        pageContentMedia2.setId(pageContentMedia1.getId());
        assertThat(pageContentMedia1).isEqualTo(pageContentMedia2);

        pageContentMedia2 = getPageContentMediaSample2();
        assertThat(pageContentMedia1).isNotEqualTo(pageContentMedia2);
    }

    @Test
    void pagecontentTest() throws Exception {
        PageContentMedia pageContentMedia = getPageContentMediaRandomSampleGenerator();
        PageContent pageContentBack = getPageContentRandomSampleGenerator();

        pageContentMedia.setPagecontent(pageContentBack);
        assertThat(pageContentMedia.getPagecontent()).isEqualTo(pageContentBack);

        pageContentMedia.pagecontent(null);
        assertThat(pageContentMedia.getPagecontent()).isNull();
    }
}
