package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.HttpError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = ApplicationException.class)
    @ResponseBody
    public ResponseEntity handleApplicationRuntimeException(
            HttpServletRequest request, ApplicationException e){
        return handleError(request, e.getError(), e, e.getArgs());

    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ResponseEntity handlenRuntimeException(
            HttpServletRequest request, RuntimeException e){
        return handleError(request, HttpError.UNEXPECTED, e, e.toString());
    }

    private ResponseEntity handleError(
            HttpServletRequest request,
            HttpError httpError,
            Exception e,
            Object... args){
        String message = MessageFormat.format(httpError.getMessage(), args);

        if(httpError.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){

        }else{

        }

        if(httpError.getStatus() == HttpStatus.UNAUTHORIZED){
            return new ResponseEntity(httpError.getStatus());
        }

        RestError restError = new RestError();
        restError.path = request.getRequestURI();
        restError.error = httpError.name();
        restError.status = httpError.getStatus().value();
        restError.message = message;
        restError.exception = e.getClass().getName();

        return new ResponseEntity<>(restError, httpError.getStatus());
    }

    private class RestError{
        String path;
        String error;
        int status;
        String message;
        String exception;
    }
}
