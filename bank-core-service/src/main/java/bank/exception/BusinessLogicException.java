package bank.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String message) {
        super(message);
    }

    public abstract HttpStatus httpStatus();
}
