package jp.co.fujixerox.nbd.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

public class InvalidRequestException extends RuntimeException {
    @Getter
    private Errors errors;

    public InvalidRequestException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }
}