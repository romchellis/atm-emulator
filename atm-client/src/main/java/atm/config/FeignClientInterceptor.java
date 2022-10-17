package atm.config;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("Authorization".equals(c.getName())) {
                    requestTemplate.header(AUTHORIZATION_HEADER, c.getValue());
                    return;
                }
            }
        }
    }
}
