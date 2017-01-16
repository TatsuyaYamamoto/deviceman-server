package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

/**
 * ControllerクラスがthrowするExceptionのレスポンスを定義する
 */
@ControllerAdvice
@Order(0)
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    private class Response {
        @Getter
        @Setter
        private List<Map> errors;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity handleApplicationRuntimeException(ApplicationException e) {
        return new ResponseEntity(e.getCause(), e.getError().getStatus());
    }
}
