package com.paradox.zswebsite.domain;

import static com.paradox.zswebsite.domain.PageContentMediaTestSamples.*;
import static com.paradox.zswebsite.domain.PageContentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PageContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageContent.class);
        PageContent pageContent1 = getPageContentSample1();
        PageContent pageContent2 = new PageContent();
        assertThat(pageContent1).isNotEqualTo(pageContent2);

        pageContent2.setId(pageContent1.getId());
        assertThat(pageContent1).isEqualTo(pageContent2);

        pageContent2 = getPageContentSample2();
        assertThat(pageContent1).isNotEqualTo(pageContent2);
    }

    @Test
    void mediaTest() throws Exception {
        PageContent pageContent = getPageContentRandomSampleGenerator();
        PageContentMedia pageContentMediaBack = getPageContentMediaRandomSampleGenerator();

        pageContent.addMedia(pageContentMediaBack);
        assertThat(pageContent.getMedia()).containsOnly(pageContentMediaBack);
        assertThat(pageContentMediaBack.getPagecontent()).isEqualTo(pageContent);

        pageContent.removeMedia(pageContentMediaBack);
        assertThat(pageContent.getMedia()).doesNotContain(pageContentMediaBack);
        assertThat(pageContentMediaBack.getPagecontent()).isNull();

        pageContent.media(new HashSet<>(Set.of(pageContentMediaBack)));
        assertThat(pageContent.getMedia()).containsOnly(pageContentMediaBack);
        assertThat(pageContentMediaBack.getPagecontent()).isEqualTo(pageContent);

        pageContent.setMedia(new HashSet<>());
        assertThat(pageContent.getMedia()).doesNotContain(pageContentMediaBack);
        assertThat(pageContentMediaBack.getPagecontent()).isNull();
    }
}
