package request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PreferableLoginMethodRequest {
    private final LoginMethod loginMethod;
}
