package atm.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BankApiErrorResponse {
    private final String body;
    private final int status;
}
