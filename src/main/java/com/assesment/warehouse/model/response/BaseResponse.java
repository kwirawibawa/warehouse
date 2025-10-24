package com.assesment.warehouse.model.response;

import com.assesment.warehouse.util.CommonConstants;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BaseResponse {

    private String code;
    private String message;
    private Object data;

    public BaseResponse() {
    }

    public BaseResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static BaseResponse success(Object data) {
        return new BaseResponse(CommonConstants.SUCCESS_CODE, "Success", data);
    }

    public static BaseResponse success(String message, Object data) {
        return new BaseResponse(CommonConstants.SUCCESS_CODE, message, data);
    }

    public static BaseResponse error(String code, String message) {
        return new BaseResponse(code, message);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        if (data != null) response.put("data", data);
        return response;
    }
}