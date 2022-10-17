package bank.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class BusinessLogicRestExceptionHandler {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessLogicException ex) {
        final var error = new ErrorResponse(ex.getMessage(), ex.httpStatus().value());
        Optional.ofNullable(request)
                .map(HttpServletRequest::getCookies)
                .map(Arrays::asList)
                .ifPresent(it -> it.forEach(response::addCookie));
        return ResponseEntity.status(ex.httpStatus())
                .body(error);
    }
}
