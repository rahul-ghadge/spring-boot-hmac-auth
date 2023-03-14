package com.arya.hmac.auth.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
@ConfigurationProperties(prefix = "hmac-verify")
@Data
public class HmacProperties {
    private Header header;
    private Server server;
    private String accessKey;
    private String secretKey;
    private Integer expires;

    @Data
    public static class Header {
        private String nonce;
        private String accessKey;
        private String authorization;
    }

    @Data
    public static class Server {
        private String scheme;
        private String host;

    }
}
