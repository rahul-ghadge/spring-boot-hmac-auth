package com.arya.hmac.auth.config;

import com.arya.hmac.auth.model.PlainText;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Builder
public class HmacHelper {
//    private static final String URL_DECODE_METHOD = "POST";
    private HttpServletRequest request;
    private String serverScheme;
    private String serverHost;
    private String encoding;

    public HmacSignature createHmacSignatureBuilder(String nonce) {
        try {

            PlainText plainText = PlainText.builder()
                    .method(getRequest().getMethod())
                    .scheme(getServerScheme())
                    .host(getServerHost())
                    .contentType(getRequest().getContentType() == null ? "" : getRequest().getContentType())
                    .path(getRequest().getRequestURI())
                    .query(getQueryString())
                    .body(getBody(request))
                    .nonce(nonce)
                    .build();

            log.info("Plain Test Request: {}", plainText);

            return HmacSignature.builder()
                    .plainText(plainText)
                    .build();
        } catch (IOException e) {
            log.error("createBuilderFromRequest failure :{}", e.getMessage());
            if (log.isTraceEnabled()) {
                log.trace("createBuilderFromRequest failure : ", e);
            }
        }
        return null;
    }

    private HttpServletRequest getRequest() {
        return this.request;
    }

    private String getServerScheme() {
        if (this.serverScheme != null) {
            return this.serverScheme;
        }
        return getRequest().getScheme();
    }

    private String getServerHost() {
        if (this.serverHost != null) {
            return this.serverHost;
        }
        String host = getRequest().getHeader("host");
        if (StringUtils.isNotBlank(host)) {
            host = host.split(":")[0];
        }
        return host;
    }

    private String getEncoding() {
        return this.encoding;
    }

    /**
     * querystring if not blank, start with '?'
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getQueryString() throws UnsupportedEncodingException {
        return StringUtils.isBlank(getRequest().getQueryString())
                ? ""
                : "?" + getRequest().getQueryString();
    }

    /**
     * don't
     *
     * @return
     * @throws IOException
     */
    public static String getBody(HttpServletRequest request) throws IOException {
        String body = "";
        byte[] content = getPayload(request);
        if (content != null && content.length > 0) {
            body = new String(content);
        }
//        if ("POST".equals(method))
//            return new ObjectMapper().readValue(body, SuperHero.class).toString();

        return body;

    }

    private static byte[] payload;

    private static byte[] getPayload(HttpServletRequest request) throws IOException {
        if (payload == null) {
            int contentLength = request.getContentLength();
            if (contentLength > 0) {
                payload = StreamUtils.copyToByteArray(request.getInputStream());
                if (payload.length != contentLength) {
                    throw new IOException("request inputStream read payload length : " + payload.length
                            + " not equals content-length : " + contentLength
                            + ", does the request inputStream already closed?");
                }
            } else {
                payload = new byte[0];
            }
        }
        return payload;
    }
}
