package bank.auth;

import static bank.exception.auth.SessionHasExpiredException.sessionHasExpiredException;
import static bank.exception.auth.UnauthorizedException.unauthorizedException;
import static bank.jooq.generated.model.tables.CardSession.CARD_SESSION;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.exception.BusinessLogicException;
import bank.exception.ErrorResponse;
import bank.jooq.generated.model.tables.records.CardSessionRecord;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final DSLContext dslContext;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final var token = authCookie(request);
        if (token == null) {
            errorResponse(response, unauthorizedException());
            return;
        }

        final var cardSession = fetchSession(token);

        if (cardSession == null) {
            errorResponse(response, unauthorizedException());
            return;
        }

        final var now = LocalDateTime.now();
        final var validUntil = cardSession.getValidUntil();
        if (now.isAfter(validUntil)) {
            errorResponse(response, sessionHasExpiredException());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private CardSessionRecord fetchSession(String token) {
        return dslContext.select()
                .from(CARD_SESSION)
                .where(CARD_SESSION.TOKEN.eq(token))
                .fetchOneInto(CardSessionRecord.class);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final var pathMatcher = new AntPathMatcher();
        return AUTH_WHITELIST.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    private String authCookie(HttpServletRequest request) {
        final var token = request.getHeader("Authorization");
        if (token != null) {
            return token;
        }

        final var cookies = ofNullable(request.getCookies())
                .orElse(new Cookie[]{});
        return stream(cookies)
                .filter(it -> "Authorization".equals(it.getName()))
                .map(Cookie::getValue)
                .findAny()
                .orElse(null);
    }

    @SneakyThrows
    private void errorResponse(HttpServletResponse response,
                               BusinessLogicException ex) {
        final var errorResponse = new ErrorResponse(ex.getMessage(), ex.httpStatus().value());
        response.setContentType("application/json");
        response.setStatus(ex.httpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        log.warn(String.valueOf(errorResponse));
    }

    private static final Set<String> AUTH_WHITELIST = Set.of(
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/auth/**"
    );
}
