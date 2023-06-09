package com.arya.hmac.auth.model;

import com.arya.hmac.auth.config.CredentialsProvider;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HmacConfigProperties {
    private CredentialsProvider provider;
    private String headerOfNonce;
    private String headerOfAccessKey;
    private String headerOfAuthorization;
}