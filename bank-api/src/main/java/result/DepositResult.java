package result;

import java.math.BigDecimal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DepositResult {

    private final BigDecimal currentAmount;

    public static DepositResult of(BigDecimal currentAmount) {
        return new DepositResult(currentAmount);
    }
}
