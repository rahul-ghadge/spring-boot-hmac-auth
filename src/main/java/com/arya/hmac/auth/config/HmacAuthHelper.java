package com.arya.hmac.auth.config;

import com.arya.hmac.auth.model.HmacConfigProperties;
import com.arya.hmac.auth.model.HmacResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HmacAuthHelper {
    public static final List<String> supportMethods =
            Collections.unmodifiableList(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE"));

    public static final String UTF8 = "UTF-8";

    public static final Map<String, String> algorithmMap = new HashMap<>();
    static {
        algorithmMap.put("HmacSHA256".toLowerCase(), "HmacSHA256");
        algorithmMap.put("HmacSHA512".toLowerCase(), "HmacSHA512");
    }


    public static HmacResponse verify(HttpServletRequest request, HmacConfigProperties config) {


        String httpMethod = request.getMethod();
        if (!supportMethods.contains(httpMethod)) {
            return reply(405, request.getRequestURI() + " not support http method : " + httpMethod);
        }

        String accessKey = request.getHeader(config.getHeaderOfAccessKey());
        if (StringUtils.isBlank(accessKey)) {
            return reply(401, request.getRequestURI() + " need " + config.getHeaderOfAccessKey() + " in header");
        }

        String authorization = request.getHeader(config.getHeaderOfAuthorization());
        if (StringUtils.isBlank(authorization)) {
            return reply(401, request.getRequestURI() + " need " + config.getHeaderOfAuthorization() + " header");
        }

        String nonce = request.getHeader(config.getHeaderOfNonce());
        if (StringUtils.isBlank(nonce)) {
            return reply(400, "Bad request: " + request.getRequestURI() + " need " + config.getHeaderOfNonce() + " header");
        }

        HmacSignature hmacSignature = HmacHelper.builder()
                .encoding(UTF8)
                .request(request)
                .build()
                .createHmacSignatureBuilder(nonce);

        if (hmacSignature == null) {
            return reply(500, request.getRequestURI()
                    + " get hmac signature from request failure");
        }

        CredentialsProvider.Credential credential = config.getProvider().getCredential(accessKey);
        if (credential == null || StringUtils.isBlank(credential.getSecretKey())) {
            return reply(401, request.getRequestURI() + " header " + config.getHeaderOfAccessKey() + " value is invalidate");
        }

        String[] authArray = authorization.split(":");
        if (authArray.length != 2) {
            return reply(401, request.getRequestURI() + " header " + config.getHeaderOfAuthorization() + " value is invalidate");
        }
        String algorithm = authArray[0].trim();
        String clientSignature = authArray[1].trim();

        if (StringUtils.isBlank(algorithm)) {
            return reply(401, request.getRequestURI() + " header " + config.getHeaderOfAuthorization() + " value is invalidate, lost algorithm");
        }

        if (!algorithmMap.containsKey(algorithm.toLowerCase())) {
            return reply(401, request.getRequestURI() + " header " + config.getHeaderOfAuthorization() + " value is invalidate, algorithm: " + algorithm + " not support");
        }

        if (StringUtils.isBlank(clientSignature)) {
            return reply(401, request.getRequestURI() + " header " + config.getHeaderOfAuthorization() + " value is invalidate, lost signature");
        }

        hmacSignature.setAlgorithm(algorithmMap.get(algorithm.toLowerCase()));
        hmacSignature.setSecretKey(credential.getSecretKey());
        String serverSignature = hmacSignature.signature();

        if (StringUtils.isBlank(serverSignature)) {
            return reply(500, request.getRequestURI() + " signature failure");
        }

        Map<String, String> sign = new HashMap();
        sign.put("plainText", hmacSignature.getPlainText().toString());
        sign.put("signature", serverSignature);
        sign.put("client", clientSignature);
        log.info("sign: {}", sign);

        if (!serverSignature.equals(clientSignature)) {
            return reply(500, request.getRequestURI() + " verify signature failure. Sign: " + sign + ", nonce" + nonce);
        }

        // check nonce
        if (credential.getNonceChecker() != null && !credential.getNonceChecker().check(nonce)) {
            return reply(401, request.getRequestURI() + " check nonce failure. Sign: " + sign + ", nonce" + nonce);
        }

        return reply(200, "Success");
    }

    private static HmacResponse reply(int httpStatus, String responseObject) {
        return HmacResponse.builder()
                .statusCode(httpStatus)
                .message(responseObject)
                .build();
    }
}
