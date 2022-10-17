package bank.util;

import static org.springframework.web.util.WebUtils.getCookie;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

    public static String getAuthToken(HttpServletRequest request) {
        return Optional.ofNullable(getCookie(request, "Authorization"))
                .map(Cookie::getValue)
                .orElse(request.getHeader("Authorization"));
    }
}
