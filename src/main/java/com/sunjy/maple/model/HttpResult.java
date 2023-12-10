package com.sunjy.maple.model;

import com.sunjy.maple.constant.HttpResultCode;
import lombok.Data;

/**
 * @author created by sunjy on 12/1/23
 */
@Data
public class HttpResult<T> {

    private int code;
    private String message;
    private T data;

    public HttpResult() {

    }

    public HttpResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> HttpResult<T> success() {
        return new HttpResult<>(HttpResultCode.SUCCESS, "success");
    }

    public static <T> HttpResult<T> success(T data) {
        return new HttpResult<>(HttpResultCode.SUCCESS, "success", data);
    }

    public static <T> HttpResult<T> error(String message) {
        return new HttpResult<>(HttpResultCode.ERROR, message);
    }

}
