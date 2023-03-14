package com.arya.hmac.auth.config.filter;

import com.arya.hmac.auth.model.HmacConfig;
import com.arya.hmac.auth.config.HmacVerifyHelper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Objects;

public class HmacInterceptor implements HandlerInterceptor {

    private HmacConfig config;

    public HmacInterceptor(HmacConfig config) {
        this.config = config;
    }

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        JSONPObject verifyResult = HmacVerifyHelper.verify(request, config);
        if (verifyResult.getFunction().equals("200")) {
            return true;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(Objects.nonNull(verifyResult.getFunction()) ? Integer.parseInt(verifyResult.getFunction()) : 500);
        PrintWriter writer = response.getWriter();
        writer.append(verifyResult.getValue().toString());

        return false;
    }

}