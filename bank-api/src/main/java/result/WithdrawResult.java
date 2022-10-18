package result;

import java.math.BigDecimal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WithdrawResult {
    /**
     * Money after withdrawal
     */
    private final BigDecimal currentAmount;
    /**
     * Withdrawn money
     */
    private final BigDecimal withdrawMoney;

    public static WithdrawResult of(BigDecimal updatedAmount, BigDecimal amountToWithdraw) {
        return new WithdrawResult(updatedAmount, amountToWithdraw);
    }
}
