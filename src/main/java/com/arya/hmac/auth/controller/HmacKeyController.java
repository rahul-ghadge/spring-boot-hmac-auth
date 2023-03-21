package com.arya.hmac.auth.controller;

import com.arya.hmac.auth.config.HmacHelper;
import com.arya.hmac.auth.config.HmacSignature;
import com.arya.hmac.auth.config.properties.HmacProperties;
import com.arya.hmac.auth.model.PlainText;
import com.arya.hmac.auth.model.SuperHero;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/hmac-key/super-heroes")
public class HmacKeyController {

    @Autowired
    private HmacProperties properties;

    private static final String algorithm = "HmacSHA256";


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(HttpServletRequest httpServletRequest) throws IOException {
        Map<String, Object> response = getHmacAuthToken(httpServletRequest, Thread.currentThread().getStackTrace()[1].getMethodName());
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<?> findAll(HttpServletRequest httpServletRequest) throws IOException {
        Map<String, Object> response = getHmacAuthToken(httpServletRequest, Thread.currentThread().getStackTrace()[1].getMethodName());
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<?> save(HttpServletRequest httpServletRequest) throws IOException {
        Map<String, Object> response = getHmacAuthToken(httpServletRequest, Thread.currentThread().getStackTrace()[1].getMethodName());
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(HttpServletRequest httpServletRequest) throws IOException {
        Map<String, Object> response = getHmacAuthToken(httpServletRequest, Thread.currentThread().getStackTrace()[1].getMethodName());
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(HttpServletRequest httpServletRequest) throws IOException {
        Map<String, Object> response = getHmacAuthToken(httpServletRequest, Thread.currentThread().getStackTrace()[1].getMethodName());
        return ResponseEntity.ok(response);
    }


    @NotNull
    private Map<String, Object> getHmacAuthToken(HttpServletRequest httpServletRequest, String method) throws IOException {
        Map<String, Object> response = new HashMap<>();
        String nonce = String.valueOf(System.currentTimeMillis());
        String secretKey = properties.getSecretKey();

        String query = StringUtils.isBlank(httpServletRequest.getQueryString())
                ? "" : "?" + httpServletRequest.getQueryString();

        String contentType = Objects.isNull(httpServletRequest.getContentType())
                ? "" : httpServletRequest.getContentType();

        PlainText plainText = getPlainText(httpServletRequest, nonce, query, contentType, HmacHelper.getBody(httpServletRequest));
        log.info("Plain Test request: {}", plainText);
        String authorization = getAuthorizationToken(secretKey, plainText);

        response.put("authorization", authorization);
        response.put("nonce", nonce);

        log.info("Returning success response for {}: {}", method, response);
        return response;
    }


    @NotNull
    private static String getAuthorizationToken(String secretKey, PlainText plainText) {
        String signature = HmacSignature.builder()
                .plainText(plainText)
                .algorithm(algorithm)
                .secretKey(secretKey)
                .build()
                .signature();

        String authorization = algorithm + ":" + signature;
        return authorization;
    }


    @NotNull
    private static PlainText getPlainText(HttpServletRequest httpServletRequest, String nonce, String query,
                                          String contentTyp, String body) {
        String uri = httpServletRequest.getRequestURI().replace("/hmac-key", "/v1");

        log.info("uri: {}, nonce: {}", uri, nonce);

        return PlainText.builder()
                .method(httpServletRequest.getMethod().toUpperCase(Locale.ROOT))
                .scheme(httpServletRequest.getScheme())
                .host(httpServletRequest.getServerName())
                .path(uri)
                .query(query)
                .contentType(contentTyp)
                .body(body)
                .nonce(nonce)
                .build();
    }

}