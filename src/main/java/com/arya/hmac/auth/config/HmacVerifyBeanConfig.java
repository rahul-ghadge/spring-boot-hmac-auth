package com.arya.hmac.auth.config;

import com.arya.hmac.auth.config.filter.HmacInterceptor;
import com.arya.hmac.auth.config.properties.HmacProperties;
import com.arya.hmac.auth.config.verifier.CredentialsVerifier;
import com.arya.hmac.auth.model.HmacConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HmacProperties.class)
public class HmacVerifyBeanConfig {

    private HmacProperties hmacProperties;

    public HmacVerifyBeanConfig(HmacProperties hmacProperties) {
        this.hmacProperties = hmacProperties;
    }

//    @Bean
//    public FilterRegistrationBean cachingRequestFilterRegister() {
//        FilterRegistrationBean<CachingRequestFilter> registration = new FilterRegistrationBean<>();
//
//        registration.setName("cachingRequestFilter");
//        registration.setFilter(new CachingRequestFilter());
//        registration.addUrlPatterns("/api/v1/*");
//        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);          // first filter
//
//        return registration;
//    }

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

        HmacConfig config = HmacConfig.builder()
                .provider(credentialsProvider)
                .headerOfAccessKey(hmacProperties.getHeader().getAccessKey())
                .headerOfAuthorization(hmacProperties.getHeader().getAuthorization())
                .headerOfNonce(hmacProperties.getHeader().getNonce())
                .serverScheme(hmacProperties.getServer().getScheme())
                .serverHost(hmacProperties.getServer().getHost())
                .build();

        return new HmacInterceptor(config);
    }
}
