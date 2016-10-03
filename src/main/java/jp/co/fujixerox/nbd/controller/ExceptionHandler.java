package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.exception.InvalidRequestException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
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
    public ResponseEntity handleApplicationRuntimeException(ApplicationException e){
        return new ResponseEntity(e.getCause() , e.getError().getStatus());
    }

    /**
     * BeanValidationの結果throwされたInvalidRequestExceptionをハンドリングする
     *
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = InvalidRequestException.class)
    protected ResponseEntity<Object> handleInvalidRequest(InvalidRequestException e) {
        List<Map> errors = new ArrayList<Map>();
        for(FieldError fieldError: e.getErrors().getFieldErrors()){
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("message", fieldError.getDefaultMessage());
            error.put("field", fieldError.getField());
            error.put("rejected_value", fieldError.getRejectedValue());

            errors.add(error);
        }

        Response response = new Response();
        response.setErrors(errors);

        return new ResponseEntity(response , HttpStatus.BAD_REQUEST);
    }
}