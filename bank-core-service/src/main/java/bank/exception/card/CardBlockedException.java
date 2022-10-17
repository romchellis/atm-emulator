package bank.exception.card;

import org.springframework.http.HttpStatus;

import bank.exception.BusinessLogicException;

public class CardBlockedException extends BusinessLogicException {
    private static final String format = "Your card %s has blocked! Please contact support center";

    public CardBlockedException(Long cardNumber) {
        super(String.format(format, cardNumber));
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
