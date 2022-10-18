package bank.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import api.AuthApi;
import bank.auth.AuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import result.LoginResult;
import request.PreferableLoginMethodRequest;
import request.LoginRequest;
import result.PreferableLoginMethodResult;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    @Value("${card.session.time-to-live}")
    private Long tokenTimeToLive;
    private final AuthService authService;
    private final HttpServletResponse httpServletResponse;

    @Override
    public LoginResult login(@NonNull LoginRequest request) {
        final var loginResult = authService.login(request);
        final var cookie = new Cookie("Authorization", loginResult.getToken());
        cookie.setMaxAge(tokenTimeToLive.intValue());
        cookie.setPath("/");
        httpServletResponse.addHeader("Authorization", loginResult.getToken());
        httpServletResponse.addCookie(cookie);
        return loginResult;
    }

    @Override
    public PreferableLoginMethodResult setPreferableLoginMethod(PreferableLoginMethodRequest request) {
        return authService.setPreferableLoginMethod(request);
    }

}

