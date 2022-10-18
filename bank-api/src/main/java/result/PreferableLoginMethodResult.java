package result;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PreferableLoginMethodResult {
    private final boolean success;

    public static PreferableLoginMethodResult success() {
        return new PreferableLoginMethodResult(true);
    }
}
