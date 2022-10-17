package request;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@RequiredArgsConstructor
public class DepositRequest {
    private final BigDecimal amount;
}
