package atm.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import api.AuthApi;
import lombok.RequiredArgsConstructor;
import request.LoginRequest;
import request.PreferableLoginMethodRequest;
import result.LoginResult;
import result.PreferableLoginMethodResult;

@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApi authApi;

    @PostMapping("/login")
    public LoginResult login(@RequestBody LoginRequest request) {
        return authApi.login(request);
    }

    @PostMapping("/set-login-method")
    public PreferableLoginMethodResult setPreferableLoginMethod(@RequestBody PreferableLoginMethodRequest request) {
        return authApi.setPreferableLoginMethod(request);
    }
}
