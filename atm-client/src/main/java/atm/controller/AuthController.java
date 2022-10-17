package atm.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import api.AuthApi;
import lombok.RequiredArgsConstructor;
import request.LoginRequest;
import request.PreferableLoginMethodRequest;
import result.LoginResult;
import result.PreferableLoginMethodResult;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApi authApi;
    private final HttpServletResponse response;

    @PostMapping("/login")
    public LoginResult login(@RequestBody LoginRequest request) {
        final var loginResult = authApi.login(request);
        Cookie authorization = new Cookie("Authorization", loginResult.getToken());
        authorization.setMaxAge(60);
        authorization.setPath("/");
        response.addCookie(authorization);
        return loginResult;
    }

    @PostMapping("/set-login-method")
    public PreferableLoginMethodResult setPreferableLoginMethod(@RequestBody PreferableLoginMethodRequest request) {
        return authApi.setPreferableLoginMethod(request);
    }
}
