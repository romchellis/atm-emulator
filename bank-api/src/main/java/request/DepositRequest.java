package request;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Request for deposit money to current card
 */
@Data
@Builder
@RequiredArgsConstructor
public class DepositRequest {
    private final BigDecimal amount;
}
