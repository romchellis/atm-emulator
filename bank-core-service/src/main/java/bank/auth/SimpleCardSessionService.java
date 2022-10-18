package bank.auth;

import static bank.jooq.generated.model.tables.CardSession.CARD_SESSION;
import static bank.util.TokenUtil.authTokenOrNull;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimpleCardSessionService implements CardSessionService {

    private final HttpServletRequest request;
    private final CardSessionValidator cardSessionValidator;
    private final DSLContext dslContext;

    @Override
    public Integer fetchCurrentSessionCardId() {
        final var authToken = authTokenOrNull(request);
        cardSessionValidator.validate(request);
        return dslContext.select(CARD_SESSION.ID)
                .from(CARD_SESSION)
                .where(CARD_SESSION.TOKEN.eq(authToken))
                .fetchOneInto(Integer.class);
    }
}
