package com.arya.hmac.auth.config;

import com.arya.hmac.auth.model.HmacConfig;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HmacVerifyHelper {
    public static final List<String> supportMethods =
            Collections.unmodifiableList(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE"));

    public static final String UTF8 = "UTF-8";

    public static final Map<String, String> algorithmMap = new HashMap<>();
    static {
        algorithmMap.put("HmacSHA256".toLowerCase(), "HmacSHA256");
        algorithmMap.put("HmacSHA512".toLowerCase(), "HmacSHA512");
    }


    public static JSONPObject verify(HttpServletRequest request, HmacConfig config) {

        String httpMethod = request.getMethod();
        if (!supportMethods.contains(httpMethod)) {
            return fail("405", request.getRequestURI() + " not support http method : " + httpMethod);
        }

        String accessKey = request.getHeader(config.getHeaderOfAccessKey());
        if (StringUtils.isBlank(accessKey)) {
            return fail("401", request.getRequestURI() + " need " + config.getHeaderOfAccessKey() + " in header");
        }

        String authorization = request.getHeader(config.getHeaderOfAuthorization());
        if (StringUtils.isBlank(authorization)) {
            return fail("401", request.getRequestURI() + " need " + config.getHeaderOfAuthorization() + " header");
        }

        String nonce = request.getHeader(config.getHeaderOfNonce());
        if (StringUtils.isBlank(nonce)) {
            return fail("400", "Bad request: " + request.getRequestURI() + " need " + config.getHeaderOfNonce() + " header");
        }

//        CachingRequestWrapper cachingRequestWrapper = null;
//        if (request instanceof HttpServletRequestWrapper) {
//            if (request instanceof CachingRequestWrapper) {
//                cachingRequestWrapper = (CachingRequestWrapper) request;
//            } else {
//                if (((HttpServletRequestWrapper) request).getRequest() instanceof CachingRequestWrapper) {
//                    cachingRequestWrapper = (CachingRequestWrapper) ((HttpServletRequestWrapper) request).getRequest();
//                }
//            }
//        }
//
//        if (cachingRequestWrapper == null) {
//            ResponseObject<String> responseObject = new ResponseObject<>(
//                    20500, request.getRequestURI() + " request is not CachingRequestWrapper");
//            return fail(500, responseObject);
//        }

        HmacSignature hmacSignature = HmacHelper.builder()
                .encoding(UTF8)
                .request(request)
                .serverScheme(config.getServerScheme())
                .serverHost(config.getServerHost())
                .build()
                .createHmacSignatureBuilder(nonce);

        if (hmacSignature == null) {
//            ResponseObject<String> responseObject = new ResponseObject<>(
//                    20500, request.getRequestURI() + " get hmac signature from request failure");
            return fail("500", request.getRequestURI()
                    + " get hmac signature from request failure");
        }

        CredentialsProvider.Credential credential = config.getProvider().getCredential(accessKey);
        if (credential == null || StringUtils.isBlank(credential.getSecretKey())) {
            return fail("401", request.getRequestURI() + " header " + config.getHeaderOfAccessKey() + " value is invalidate");
        }

        String[] authArray = authorization.split(":");
        if (authArray.length != 2) {
            return fail("401", request.getRequestURI() + " header " + config.getHeaderOfAuthorization() + " value is invalidate");
        }
        String algorithm = authArray[0].trim();
        String clientSignature = authArray[1].trim();

        if (StringUtils.isBlank(algorithm)) {
            return fail("401", request.getRequestURI() + " header " + config.getHeaderOfAuthorization() + " value is invalidate, lost algorithm");
        }

        if (!algorithmMap.containsKey(algorithm.toLowerCase())) {
            return fail("401", request.getRequestURI() + " header " + config.getHeaderOfAuthorization() + " value is invalidate, algorithm: " + algorithm + " not support");
        }

        if (StringUtils.isBlank(clientSignature)) {
            return fail("401", request.getRequestURI() + " header " + config.getHeaderOfAuthorization() + " value is invalidate, lost signature");
        }

        hmacSignature.setAlgorithm(algorithmMap.get(algorithm.toLowerCase()));
        hmacSignature.setSecretKey(credential.getSecretKey());
        String serverSignature = hmacSignature.signature();

        if (StringUtils.isBlank(serverSignature)) {
            return fail("500", request.getRequestURI() + " signature failure");
        }

        Map<String, String> sign = new HashMap();
        sign.put("plainText", hmacSignature.getPlainText().toString());
        sign.put("signature", serverSignature);
        sign.put("client", clientSignature);
        log.info("sign: {}", sign);

        if (!serverSignature.equals(clientSignature)) {
            return fail("500", request.getRequestURI() + " verify signature failure. Sign: " + sign + ", nonce" + nonce);
        }

        // check nonce
        if (credential.getNonceChecker() != null && !credential.getNonceChecker().check(nonce)) {
            return fail("401", request.getRequestURI() + " check nonce failure. Sign: " + sign + ", nonce" + nonce);
        }

        return new JSONPObject("200", sign);
    }

    private static JSONPObject fail(String httpStatus, String responseObject) {
        return new JSONPObject(httpStatus, responseObject);
    }

//    private static VerifyResult fail(int httpStatus,
//                                     ResponseObject<String> responseObject,
//                                     Map<String, String> sign,
//                                     String nonce) {
//        return VerifyResult
//                .builder()
//                .success(false)
//                .httpStatus(httpStatus)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(JSON.toJSONString(responseObject))
//                .sign(sign)
//                .nonce(nonce)
//                .build();
//    }

//    @Data
//    @Builder
//    public static class VerifyResult {
//        /**
//         * verify is success
//         */
//        private Boolean success;
//        /**
//         * http status
//         */
//        private int httpStatus;
//
//        /**
//         * response contentType
//         */
//        private String contentType;
//
//        private String nonce;
//
//        /**
//         * response body
//         */
//        private String body;
//
//        private Map<String, String> sign;
//
//
//        public static VerifyResult ok(Map<String, String> sign, String nonce) {
//            return VerifyResult.builder().success(true).sign(sign).nonce(nonce).build();
//        }
//    }

}
