package fr.diginamic.appliweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * Bean RestTemplate pour pouvoir appeler des APIs externes.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
