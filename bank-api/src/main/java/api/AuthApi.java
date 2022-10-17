package api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import result.LoginResult;
import request.PreferableLoginMethodRequest;
import request.LoginRequest;
import result.PreferableLoginMethodResult;

@RequestMapping("auth")
public interface AuthApi {

    @PostMapping("/login")
    LoginResult login(@RequestBody LoginRequest request);

    @PostMapping("/set-login-method")
    PreferableLoginMethodResult setPreferableLoginMethod(@RequestBody PreferableLoginMethodRequest request);
}
