package bank.client;

import static java.util.Optional.ofNullable;
import static org.springframework.web.util.WebUtils.getCookie;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import bank.exception.auth.UnauthorizedException;
import bank.jooq.generated.model.tables.Account;
import bank.jooq.generated.model.tables.CardSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimpleClientService implements ClientService {

    private final HttpServletRequest request;
    private final DSLContext dslContext;

    @Override
    public Integer getCurrentClientId() {
        final var authorisation = ofNullable(getCookie(request, "Authorization"))
                .map(Cookie::getValue)
                .orElseThrow(UnauthorizedException::new);

        return dslContext.select(Account.ACCOUNT.CLIENT_ID)
                .from(Account.ACCOUNT)
                .leftJoin(CardSession.CARD_SESSION)
                .on(CardSession.CARD_SESSION.TOKEN.eq(authorisation))
                .fetchAnyInto(Integer.class);
    }

}
