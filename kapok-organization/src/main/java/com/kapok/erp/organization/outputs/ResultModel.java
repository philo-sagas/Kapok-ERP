package com.kapok.erp.organization.outputs;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultModel<T> {
    private int status;

    private String message;

    private T data;

    public static <M> ResultModel<M> success(M data) {
        return new ResultModel<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <M> ResultModel<M> success(M data, String message) {
        return new ResultModel<>(HttpStatus.OK.value(), message, data);
    }

    public static <M> ResultModel<M> failure(M data, String message) {
        return new ResultModel<>(HttpStatus.SERVICE_UNAVAILABLE.value(), message, data);
    }

    public static ResultModel failure(int status, String message) {
        return new ResultModel<>(status, message, null);
    }

    public static ResultModel failure(String message) {
        return new ResultModel<>(HttpStatus.SERVICE_UNAVAILABLE.value(), message, null);
    }
}
