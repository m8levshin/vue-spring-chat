package ru.moolls.webchat.security.interceptor;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.UserRole;
import ru.moolls.webchat.exception.WebServiceException;
import ru.moolls.webchat.security.MethodPermissionResolver;
import ru.moolls.webchat.session.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Log4j
public class PermissionCheckerInterceptor implements HandlerInterceptor {

    @Autowired
    MethodPermissionResolver methodPermissionResolver;

    @Autowired
    private UserSession userSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws WebServiceException {

        log.info("Started permission checker interceptor");
        User checkingUser = userSession.getUser();
        List<UserRole> methodPermission = methodPermissionResolver.getMethodPermission(handler);

        if (methodPermission.size() > 0) {
            boolean userHavePermission = methodPermission.contains(checkingUser.getUserRole());

            if (!userHavePermission) {
                log.warn("Permission denied for userId=" + checkingUser.getUserId() +
                        "for URL:" + request.getRequestURL());
                throwPermissionException(handler);
            }
        }
        log.info("UserId=" + checkingUser.getUserId() + " has permission for URL: " + request.getRequestURL());
        return true;
    }

    private void throwPermissionException(Object handler) throws WebServiceException {
        log.info("Throwing permission exception");
        log.info("Resolving permission exception");
        WebServiceException exception = methodPermissionResolver.getThrowableException(handler);

        throw exception;
    }


}
