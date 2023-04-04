package com.arya.hmac.auth.config;

import com.arya.hmac.auth.model.HmacRequest;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Data
@Builder
public class HmacSignature {
    public static final String DELIMITER = "\n";
    private HmacRequest hmacRequest;
    private String algorithm;
    private String secretKey;

    private static String signature(String algorithm, String hmacRequest, String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        final Mac digest = Mac.getInstance(algorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
        digest.init(secretKeySpec);
        digest.update(hmacRequest.getBytes());

        final byte[] signatureBytes = digest.doFinal();
        digest.reset();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public static byte[] signatureByte(String algorithm, String hmacRequest, String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        final Mac digest = Mac.getInstance(algorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
        digest.init(secretKeySpec);
        digest.update(hmacRequest.getBytes());

        final byte[] signatureBytes = digest.doFinal();
        digest.reset();
        return signatureBytes;
    }

    public String signature() {
        try {
            return HmacSignature.signature(algorithm, hmacRequest.toString(), secretKey);
        } catch (NoSuchAlgorithmException e) {
            log.error("signature failure no such algorithm:{}, error:{}", algorithm, e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("signature failure invalid key :{}, error:{}", secretKey, e.getMessage());
        } catch (Exception e) {
            log.error("signature failure error:{}", e.getMessage());
        }
        return "";
    }
}
