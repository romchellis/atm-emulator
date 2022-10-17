package bank.exception.card;

import org.springframework.http.HttpStatus;

import bank.exception.BusinessLogicException;

public class CardNotExistsException extends BusinessLogicException {
    private static final String format = "Card with number %s seem does not exist!";

    public CardNotExistsException(Long cardNumber) {
        super(String.format(format, cardNumber));
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
