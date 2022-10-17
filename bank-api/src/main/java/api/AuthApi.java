package api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import config.FeignConfig;
import request.LoginRequest;
import request.PreferableLoginMethodRequest;
import result.LoginResult;
import result.PreferableLoginMethodResult;

@FeignClient(name = "authApi",
        url = "http://" + "${bank.api.host}" + ":${bank.api.port}",
        path = "/auth",
        configuration = FeignConfig.class)
public interface AuthApi {

    @PostMapping("/login")
    LoginResult login(@RequestBody LoginRequest request);

    @PostMapping("/set-login-method")
    PreferableLoginMethodResult setPreferableLoginMethod(@RequestBody PreferableLoginMethodRequest request);
}
