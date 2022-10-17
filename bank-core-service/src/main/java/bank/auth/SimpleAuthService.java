package bank.auth;

import static bank.jooq.generated.model.tables.Card.CARD;
import static bank.jooq.generated.model.tables.CardLoginUnsuccessfulAttempt.CARD_LOGIN_UNSUCCESSFUL_ATTEMPT;
import static bank.jooq.generated.model.tables.CardSession.CARD_SESSION;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import bank.exception.auth.BadPasswordException;
import bank.exception.card.CardBlockedException;
import bank.exception.card.CardNotExistsException;
import bank.jooq.generated.model.tables.records.CardRecord;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import request.LoginRequest;
import request.PreferableLoginMethodRequest;
import result.LoginResult;
import result.PreferableLoginMethodResult;

@Service
@RequiredArgsConstructor
public class SimpleAuthService implements AuthService {

    @Value("${card.session.time-to-live}")
    private Long tokenTimeToLive;
    private final DSLContext dslContext;

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public LoginResult login(LoginRequest loginRequest) {
        var cardNumber = loginRequest.getCardNumber();
        var password = loginRequest.getPassword();
        final var cardRecord = fetchCardByNumber(cardNumber);
        throwIfCardBlocked(cardNumber, cardRecord);
        storeAttemptIfUnsucsefful(cardRecord, password);
        final String token = generateToken(cardNumber);
        return new LoginResult(token);
    }

    @Override
    public PreferableLoginMethodResult setPreferableLoginMethod(PreferableLoginMethodRequest request) {
        throw new UnsupportedOperationException();
    }

    private void throwIfCardBlocked(Long cardNumber, CardRecord cardRecord) {
        if (cardRecord.getBlockedTimestamp() != null) {
            throw new CardBlockedException(cardNumber);
        }
    }

    private CardRecord fetchCardByNumber(Long cardNumber) {
        return dslContext.fetchOptional(CARD, CARD.NUMBER.eq(cardNumber))
                .orElseThrow(() -> new CardNotExistsException(cardNumber));
    }

    private void storeAttemptIfUnsucsefful(CardRecord card, String password) {
        var actualPassword = card.getPassword();
        if (actualPassword.equals(password)) {
            return;
        }

        var now = LocalDateTime.now();
        int attemptsCount = getUnsucsessAttemptsCount(card.getNumber(), now, now.minusMinutes(1L));
        dslContext.insertInto(CARD_LOGIN_UNSUCCESSFUL_ATTEMPT,
                        CARD_LOGIN_UNSUCCESSFUL_ATTEMPT.ATTEMPT_TIME,
                        CARD_LOGIN_UNSUCCESSFUL_ATTEMPT.CARD_NUMBER)
                .values(now, card.getNumber())
                .execute();
        blockIfThreeMoreAttempts(card, attemptsCount);
        throw new BadPasswordException(3 - attemptsCount);
    }

    private void blockIfThreeMoreAttempts(CardRecord card, int attemptsCount) {
        var now = LocalDateTime.now();
        var cardNumber = card.getNumber();
        if (attemptsCount <= 3) {
            return;
        }

        dslContext.update(CARD)
                .set(CARD.BLOCKED_TIMESTAMP, now)
                .where(CARD.NUMBER.eq(cardNumber))
                .execute();
        throw new CardBlockedException(cardNumber);
    }

    private int getUnsucsessAttemptsCount(Long cardNumber, LocalDateTime now,
                                          LocalDateTime minuteBefore) {
        return dslContext.select()
                .from(CARD_LOGIN_UNSUCCESSFUL_ATTEMPT)
                .where(CARD_LOGIN_UNSUCCESSFUL_ATTEMPT.CARD_NUMBER.eq(cardNumber))
                .and(CARD_LOGIN_UNSUCCESSFUL_ATTEMPT.ATTEMPT_TIME.betweenSymmetric(now, minuteBefore))
                .orderBy(CARD_LOGIN_UNSUCCESSFUL_ATTEMPT.ATTEMPT_TIME)
                .fetchCount();
    }

    private String generateToken(Long cardNumber) {
        var now = LocalDateTime.now();
        final var token = getJWTToken(cardNumber);
        dslContext.insertInto(CARD_SESSION,
                        CARD_SESSION.VALID_UNTIL,
                        CARD_SESSION.TOKEN,
                        CARD_SESSION.CARD_NUMBER)
                .values(now.plusSeconds(tokenTimeToLive), token, cardNumber)
                .execute();
        return token;
    }

    private String getJWTToken(Long cardNumber) {
        final var grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_CLIENT");
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(cardNumber.toString())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenTimeToLive))
                .signWith(Keys.secretKeyFor(HS512))
                .compact();
    }
}
