package jp.co.fujixerox.nbd;

import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;

public class ApplicationException extends Exception{
    @Getter
    @Setter
    private Throwable cause;
    @Getter
    @Setter
    private Object[] args;
    @Getter
    @Setter
    private HttpError error;

    public ApplicationException(Throwable cause){
        super(cause);
    }

    public ApplicationException(HttpError error){
        super();
        this.error = error;
    }

    public ApplicationException(HttpError error, Throwable cause, String... args){
        super();
        this.error = error;
        this.cause = cause;
        this.args = args;
    }

    public String getMessage(){
        if(args != null){
            return "[" + error.name() + "]" + MessageFormat.format(error.getMessage(), args);
        }
        return "[" + error.name() + "]" + MessageFormat.format(error.getMessage(), args);
    }
}
