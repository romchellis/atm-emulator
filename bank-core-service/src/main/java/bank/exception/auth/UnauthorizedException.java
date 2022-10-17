package bank.exception.auth;

import org.springframework.http.HttpStatus;

import bank.exception.BusinessLogicException;

public class UnauthorizedException extends BusinessLogicException {
    public UnauthorizedException() {
        super("Not authorized");
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
