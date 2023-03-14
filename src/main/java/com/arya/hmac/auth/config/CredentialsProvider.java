package com.arya.hmac.auth.config;

public interface CredentialsProvider {

    Credential getCredential(String accessKey);

    interface Credential {

        String getAccessKey();

        String getSecretKey();

        NonceChecker getNonceChecker();
    }

    interface NonceChecker {
        boolean check(String nonce);
    }
}