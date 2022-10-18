package bank.auth;

import result.LoginResult;
import request.PreferableLoginMethodRequest;
import request.LoginRequest;
import result.PreferableLoginMethodResult;

/**
 * Service for auth through the client card
 */
public interface AuthService {

    LoginResult login(LoginRequest loginRequest);

    PreferableLoginMethodResult setPreferableLoginMethod(PreferableLoginMethodRequest request);

}
