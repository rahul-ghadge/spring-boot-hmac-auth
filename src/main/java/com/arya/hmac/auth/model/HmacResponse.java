package com.arya.hmac.auth.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Builder
public class HmacResponse implements Serializable {

    private int statusCode;
    private String message;

}
