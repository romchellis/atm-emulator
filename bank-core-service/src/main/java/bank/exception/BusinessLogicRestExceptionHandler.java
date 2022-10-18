package bank.exception;

import static java.util.Optional.ofNullable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class BusinessLogicRestExceptionHandler {

    private final HttpServletResponse response;
    private final HttpServletRequest request;

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessLogicException ex) {
        final var error = new ErrorResponse(ex.getMessage(), ex.httpStatus().value());
        ofNullable(request)
                .map(HttpServletRequest::getCookies)
                .map(Arrays::asList)
                .ifPresent(it -> it.forEach(response::addCookie));
        return ResponseEntity.status(ex.httpStatus())
                .body(error);
    }
}
