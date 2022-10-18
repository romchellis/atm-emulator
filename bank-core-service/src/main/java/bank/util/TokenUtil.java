package bank.util;

import static java.util.Optional.ofNullable;
import static org.springframework.web.util.WebUtils.getCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

    public static String authTokenOrNull(HttpServletRequest request) {
        return ofNullable(getCookie(request, "Authorization"))
                .map(Cookie::getValue)
                .orElse(request.getHeader("Authorization"));
    }
}
