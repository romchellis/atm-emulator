package bank.exception.auth;

import org.springframework.http.HttpStatus;

import bank.exception.BusinessLogicException;

public class SessionHasExpiredException extends BusinessLogicException {
    public SessionHasExpiredException() {
        super("Your session has expired, please re-enter your login");
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
