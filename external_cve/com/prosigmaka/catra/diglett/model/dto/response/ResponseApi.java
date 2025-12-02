package com.prosigmaka.catra.diglett.model.dto.response;

/*
 * Copyright(c) 2020. All rights reserved.
 * Last modified 08/05/20 08.29
 */

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ResponseApi<T> {
    private String name;
    private String message;
    private Integer code;
    private T errors;
    private T data;

    public static ResponseApi successNoData() {
        return ResponseApi.builder().build();
    }

    public static <T> ResponseApi<T> successWithData(T data) {
        return ResponseApi.<T>builder().data(data).build();
    }

    public static <T> ResponseApi<T> success(String name, String message, Integer code, T errors, T data) {
        return ResponseApi.<T>builder().name(name).message(message).code(code).errors(errors).data(data).build();
    }

    public static <T> ResponseApi error(T errors) {
        return ResponseApi.builder().errors(errors).build();
    }

    public static <T> ResponseApi ok(String name, T data) {
        return ResponseApi.builder().name(name).message(HttpStatus.OK.getReasonPhrase()).code(HttpStatus.OK.value()).data(data).build();
    }

    public static <T> ResponseApi created(String name, T data) {
        return ResponseApi.builder().name(name).message(HttpStatus.CREATED.getReasonPhrase()).code(HttpStatus.CREATED.value()).data(data).build();
    }
}
