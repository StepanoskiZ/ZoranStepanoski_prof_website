package com.paradox.zswebsite.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.paradox.zswebsite.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.paradox.zswebsite.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.paradox.zswebsite.domain.User.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.Authority.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.User.class.getName() + ".authorities");

            // --- START: Manual additions ---
            createCache(cm, com.paradox.zswebsite.domain.Project.class.getName());
            // Assuming 'images' is a relationship in Project
            createCache(cm, com.paradox.zswebsite.domain.Project.class.getName() + ".images");
            // Assuming 'skills' is a relationship in Project
            createCache(cm, com.paradox.zswebsite.domain.Project.class.getName() + ".skills");
            createCache(cm, com.paradox.zswebsite.domain.Skill.class.getName());
            // Assuming 'projects' is a relationship in Skill
            createCache(cm, com.paradox.zswebsite.domain.Skill.class.getName() + ".projects");
            createCache(cm, com.paradox.zswebsite.domain.BusinessService.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.BlogPost.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.ContactMessage.class.getName());
            // --- END: Manual additions ---

            createCache(cm, com.paradox.zswebsite.domain.Project.class.getName() + ".media");
            createCache(cm, com.paradox.zswebsite.domain.ProjectMedia.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.VisitorLog.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.AboutMe.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.AboutMe.class.getName() + ".media");
            createCache(cm, com.paradox.zswebsite.domain.AboutMeMedia.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.CurriculumVitae.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.CurriculumVitae.class.getName() + ".media");
            createCache(cm, com.paradox.zswebsite.domain.CurriculumVitaeMedia.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.BusinessServiceMedia.class.getName());
            createCache(cm, com.paradox.zswebsite.domain.BusinessService.class.getName() + ".media");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
