package atm.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BankApiException extends RuntimeException {
    private final String body;
    private final int status;
}
