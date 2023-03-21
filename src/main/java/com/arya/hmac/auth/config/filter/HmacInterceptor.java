package com.arya.hmac.auth.config.filter;

import com.arya.hmac.auth.model.HmacConfigProperties;
import com.arya.hmac.auth.config.HmacAuthHelper;
import com.arya.hmac.auth.model.HmacResponse;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Objects;

public class HmacInterceptor implements HandlerInterceptor {

    private HmacConfigProperties config;

    public HmacInterceptor(HmacConfigProperties config) {
        this.config = config;
    }

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HmacResponse hmacResponse = HmacAuthHelper.verify(request, config);
        if (hmacResponse.getStatusCode() == 200) {
            return true;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(hmacResponse.getStatusCode() == 0 ? 500 : hmacResponse.getStatusCode());
        PrintWriter writer = response.getWriter();
        writer.append(hmacResponse.getMessage());

        return false;
    }

}