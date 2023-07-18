package com.kapok.erp.organization.exceptions;

import com.kapok.erp.organization.outputs.ResultModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultModel<Void> handleGlobalException(Throwable ex) {
        log.error(ex, ex);
        return ResultModel.failure(ex.getMessage());
    }
}
