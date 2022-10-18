package bank.auth;

import static bank.exception.auth.SessionHasExpiredException.sessionHasExpiredException;
import static bank.exception.auth.UnauthorizedException.unauthorizedException;
import static bank.jooq.generated.model.tables.CardSession.CARD_SESSION;
import static bank.util.TokenUtil.authTokenOrNull;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import bank.jooq.generated.model.tables.records.CardSessionRecord;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthTokenCardSessionValidator implements CardSessionValidator {

    private final DSLContext dslContext;

    @Override
    public void validate(HttpServletRequest request) {
        final var token = authTokenOrNull(request);
        if (token == null) {
            throw unauthorizedException();
        }

        final var cardSession = fetchSession(token);

        if (cardSession == null) {
            throw unauthorizedException();
        }

        final var now = LocalDateTime.now();
        final var validUntil = cardSession.getValidUntil();
        if (now.isAfter(validUntil)) {
            throw sessionHasExpiredException();
        }
    }

    private CardSessionRecord fetchSession(String token) {
        return dslContext.select()
                .from(CARD_SESSION)
                .where(CARD_SESSION.TOKEN.eq(token))
                .fetchOneInto(CardSessionRecord.class);
    }
}
