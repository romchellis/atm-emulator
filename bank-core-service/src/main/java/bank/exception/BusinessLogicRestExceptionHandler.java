package bank.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessLogicRestExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public final ResponseEntity<ErrorResponse> insufficientFundsException(BusinessLogicException ex) {
        final var error = new ErrorResponse(ex.getMessage(), ex.httpStatus().value());
        return new ResponseEntity<>(error, ex.httpStatus());
    }
}
