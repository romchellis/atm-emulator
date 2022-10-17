package result;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TotalAmountResult {
    private final BigDecimal amount;

    public static TotalAmountResult of(@NonNull BigDecimal amount) {
        return new TotalAmountResult(amount);
    }
}
