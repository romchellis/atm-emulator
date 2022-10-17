package atm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import atm.exception.BankApiErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class FeignExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<BankApiErrorResponse> handleBusinessException(FeignException ex) {
        log.warn(String.valueOf(ex));
        final var error = new BankApiErrorResponse(ex.contentUTF8(), ex.status());
        return ResponseEntity.status(ex.status())
                .body(error);
    }
}
