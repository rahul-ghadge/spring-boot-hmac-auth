package com.arya.hmac.auth.config;

import com.arya.hmac.auth.config.filter.HmacInterceptor;
import com.arya.hmac.auth.config.properties.HmacProperties;
import com.arya.hmac.auth.config.verifier.CredentialsVerifier;
import com.arya.hmac.auth.model.HmacConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HmacProperties.class)
public class HmacAuthBeanConfig {

    private HmacProperties hmacProperties;

    public HmacAuthBeanConfig(HmacProperties hmacProperties) {
        this.hmacProperties = hmacProperties;
    }


    @Bean
    public CredentialsProvider credentialsProvider() {
        return CredentialsVerifier.builder()
                .credential(hmacProperties.getAccessKey(), hmacProperties.getSecretKey())
                .expire(hmacProperties.getExpires())
                .build();
    }

    @Bean
    public HmacInterceptor hmacInterceptor(
            @Autowired @Qualifier("credentialsProvider") CredentialsProvider credentialsProvider) {

        HmacConfigProperties config = HmacConfigProperties.builder()
                .provider(credentialsProvider)
                .headerOfAccessKey(hmacProperties.getHeader().getAccessKey())
                .headerOfAuthorization(hmacProperties.getHeader().getAuthorization())
                .headerOfNonce(hmacProperties.getHeader().getNonce())
                .build();

        return new HmacInterceptor(config);
    }
}
