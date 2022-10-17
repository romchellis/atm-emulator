package bank.client;

import java.sql.Timestamp;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CardSession {

    private final Timestamp validUntil;
}
