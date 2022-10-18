package bank.auth;

import javax.servlet.http.HttpServletRequest;

public interface CardSessionValidator {

    /**
     * @throws  bank.exception.BusinessLogicException in case of problems with session
     */
    void validate(HttpServletRequest request);

}
