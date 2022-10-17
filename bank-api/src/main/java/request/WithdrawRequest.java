package request;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class WithdrawRequest {
    private final BigDecimal amount;
}
