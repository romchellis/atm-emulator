package bank.client;

import static bank.util.TokenUtil.getAuthToken;
import static bank.exception.auth.UnauthorizedException.unauthorizedException;
import static bank.jooq.generated.model.tables.Account.ACCOUNT;
import static bank.jooq.generated.model.tables.CardSession.CARD_SESSION;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimpleClientService implements ClientService {

    private final HttpServletRequest request;
    private final DSLContext dslContext;

    @Override
    public Integer getCurrentClientId() {
        final var token = getAuthToken(request);
        if (token == null) {
            throw unauthorizedException();
        }

        return dslContext.select(ACCOUNT.CLIENT_ID)
                .from(ACCOUNT)
                .leftJoin(CARD_SESSION)
                .on(CARD_SESSION.TOKEN.eq(token))
                .fetchAnyInto(Integer.class);
    }

}
