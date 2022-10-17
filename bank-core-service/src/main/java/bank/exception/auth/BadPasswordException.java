package bank.exception.auth;

import org.springframework.http.HttpStatus;

import bank.exception.BusinessLogicException;

public class BadPasswordException extends BusinessLogicException {
    public BadPasswordException(Integer attemtps) {
        super("You have provided wrong password, available attemtps: " + attemtps);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
