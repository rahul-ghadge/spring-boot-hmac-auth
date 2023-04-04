package com.arya.hmac.auth.config.verifier;

import com.arya.hmac.auth.config.CredentialsProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Builder
public class CredentialsVerifier implements CredentialsProvider {

    @Singular
    private Map<String, String> credentials = new ConcurrentHashMap<>();

    // expire time, unit is second
    private Integer expire;

    @Override
    public Credential getCredential(String accessKey) {
        if (Strings.isBlank(accessKey)) {
            return null;
        }
        String secretKey = credentials.get(accessKey);
        if (Strings.isBlank(secretKey)) {
            return null;
        }
        return new CredentialImpl(accessKey, secretKey, new NonceCheckerForExpire(expire));
    }

    @AllArgsConstructor
    @Data
    public static class CredentialImpl implements Credential {
        private String accessKey;
        private String secretKey;
        private NonceChecker nonceChecker;
    }

    @Slf4j
    @AllArgsConstructor
    public static class NonceCheckerForExpire implements NonceChecker {
        private Integer expire;

        @Override
        public boolean check(String nonce) {
            if (expire == null || expire < 0) {
                log.error("check nonce failure : expire:{}", expire);
                return false;
            }
            long now = System.currentTimeMillis();
            Long req = null;
            try {
                req = Long.parseLong(nonce);
            } catch (Exception e) {
                log.error("check nonce failure : not number nonce:{}", nonce);
                return false;
            }
            long expireTime = req + expire * 1000L;
            return now <= expireTime;
        }
    }
}
