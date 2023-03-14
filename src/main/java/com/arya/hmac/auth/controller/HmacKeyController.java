package com.arya.hmac.auth.controller;

import com.arya.hmac.auth.config.HmacHelper;
import com.arya.hmac.auth.config.HmacSignature;
import com.arya.hmac.auth.config.properties.HmacProperties;
import com.arya.hmac.auth.model.PlainText;
import com.arya.hmac.auth.model.SuperHero;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/hmac-key")
public class HmacKeyController {

    @Autowired
    private HmacProperties properties;

    private static final String algorithm = "HmacSHA256";


    @GetMapping("/super-heroes/{id}")
    public ResponseEntity<?> keyForFindById(HttpServletRequest httpServletRequest, Integer id) {

        Map<String, Object> response = new HashMap<>();
        String nonce = String.valueOf(System.currentTimeMillis());
        String secretKey = properties.getSecretKey();

        PlainText plainText = getPlainText(httpServletRequest, nonce, "", "", "");
        log.info("Plain Test request: {}", plainText);
        String authorization = getAuthorizationToken(secretKey, plainText);

        response.put("authorization", authorization);
        response.put("nonce", nonce);

        log.info("Returning success response for FindById: {}", response);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/super-heroes")
    public ResponseEntity<?> keyForFindAll(HttpServletRequest httpServletRequest) {

        Map<String, Object> response = new HashMap<>();
        String nonce = String.valueOf(System.currentTimeMillis());
        String secretKey = properties.getSecretKey();

        PlainText plainText = getPlainText(httpServletRequest, nonce, "", "", "");
        log.info("Plain Test request: {}", plainText);
        String authorization = getAuthorizationToken(secretKey, plainText);

        response.put("authorization", authorization);
        response.put("nonce", nonce);

        log.info("Returning success response for FindAll: {}", response);

        return ResponseEntity.ok(response);
    }



    @PostMapping("/super-heroes")
    public ResponseEntity<?> keyForSave(HttpServletRequest httpServletRequest, @RequestBody SuperHero superHero) throws IOException {

        Map<String, Object> response = new HashMap<>();
        String nonce = String.valueOf(System.currentTimeMillis());
        String secretKey = properties.getSecretKey();

        PlainText plainText = getPlainText(httpServletRequest, nonce, "", "application/json", HmacHelper.getBody(httpServletRequest));
        log.info("Plain Test request: {}", plainText);
        String authorization = getAuthorizationToken(secretKey, plainText);

        response.put("authorization", authorization);
        response.put("nonce", nonce);

        log.info("Returning success response for Save: {}", response);

        return ResponseEntity.ok(response);
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
                .scheme("http")
                .host("localhost")
                .path(uri)
                .query(query)
                .contentType(contentTyp)
                .body(body)
                .nonce(nonce)
                .build();
    }

}