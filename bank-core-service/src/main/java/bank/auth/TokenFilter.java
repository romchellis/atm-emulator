package bank.auth;

import static bank.jooq.generated.model.tables.CardSession.CARD_SESSION;

import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.exception.auth.SessionHasExpiredException;
import bank.exception.auth.UnauthorizedException;
import bank.jooq.generated.model.tables.records.CardSessionRecord;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final DSLContext dslContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final var token = authCookie(request).orElseThrow(UnauthorizedException::new);
        final var cardSession = dslContext.select()
                .from(CARD_SESSION)
                .where(CARD_SESSION.TOKEN.eq(token))
                .fetchOneInto(CardSessionRecord.class);

        if (cardSession == null) {
            throw new UnauthorizedException();
        }

        final var now = LocalDateTime.now();
        final var validUntil = cardSession.getValidUntil();
        if (now.isAfter(validUntil)) {
            throw new SessionHasExpiredException();
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        return "/auth/login".equals(path);
    }

    public Optional<String> authCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(it -> "Authorization".equals(it.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}
