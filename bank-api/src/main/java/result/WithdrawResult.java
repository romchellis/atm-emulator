package result;

import java.math.BigDecimal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WithdrawResult {
    private final BigDecimal currentAmount;
    private final BigDecimal withdrawMoney;

    public static WithdrawResult of(BigDecimal updatedAmount, BigDecimal amountToWithdraw) {
        return new WithdrawResult(updatedAmount, amountToWithdraw);
    }
}
