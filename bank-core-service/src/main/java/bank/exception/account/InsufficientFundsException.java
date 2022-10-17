package bank.exception.account;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

import bank.exception.BusinessLogicException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientFundsException extends BusinessLogicException {

    private static final String format = "You cannot withdraw %s money , you have only %s";

    public InsufficientFundsException(BigDecimal currentAmount, BigDecimal amount) {
        super(format(format, currentAmount, amount));
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
