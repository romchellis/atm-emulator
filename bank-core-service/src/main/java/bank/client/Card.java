package bank.client;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import bank.auth.LoginMethod;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Card implements UserDetails {

    private final Long number;
    private final String password;
    private final LoginMethod loginMethod;
    private final Timestamp issuedUntil;
    private final Timestamp blockedTimestamp;

    private final List<CardSession> cardSessions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return number.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        //i would inject time service for this
        return issuedUntil.after(new Date(System.currentTimeMillis()));
    }

    @Override
    public boolean isAccountNonLocked() {
        return blockedTimestamp == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        Date current = new Date(System.currentTimeMillis());
        return cardSessions.stream()
                .max(Comparator.comparing(CardSession::getValidUntil))
                .map(CardSession::getValidUntil)
                .map(current::before)
                .orElse(false);
    }

    @Override
    public boolean isEnabled() {
        return blockedTimestamp == null;
    }
}
