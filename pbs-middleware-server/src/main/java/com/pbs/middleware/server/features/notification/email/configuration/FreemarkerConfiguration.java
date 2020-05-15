package com.pbs.middleware.server.features.notification.email.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@ConditionalOnProperty("spring.mail.username")
@Configuration
public class FreemarkerConfiguration {

    @Bean
    public FreeMarkerConfigurationFactoryBean getFreemarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPaths("classpath:/templates/");
        return bean;
    }

}
