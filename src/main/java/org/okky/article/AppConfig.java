package org.okky.article;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class AppConfig {
    @Value("${app.internal-endpoint}")
    String serviceUrl;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .rootUri(serviceUrl)
                .build();
    }
}
