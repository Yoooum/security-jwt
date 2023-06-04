package com.prprv.token.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 通用返回类
 * @author Yoooum
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record R<T>(int code, String message, T data) implements Serializable {
    public static <T> R<T> ok() {
        return new R<>(0, null, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(0, null, data);
    }

    public static <T> R<T> ok(T data, String message) {
        return new R<>(0, message, data);
    }

    public static <T> R<T> fail() {
        return new R<>(1, null, null);
    }

    public static <T> R<T> fail(String message) {
        return new R<>(1, message, null);
    }

    public static <T> R<T> fail(T data) {
        return new R<>(1, null, data);
    }

    public static <T> R<T> fail(T data, String message) {
        return new R<>(1, message, data);
    }

    public static <T> R<T> of(int code, String message) {
        return new R<>(code, message, null);
    }

    public static <T> R<T> of(int code, String message, T data) {
        return new R<>(code, message, data);
    }

}
