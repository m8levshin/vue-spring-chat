package ru.moolls.webchat.security.interceptor;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.service.UserService;
import ru.moolls.webchat.session.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Log4j
public class ActualUserUpdateInterceptor implements HandlerInterceptor {

    @Autowired
    private UserSession userSession;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("Started getting actual user interceptor");
        User sessionUser = userSession.getUser();
        boolean isAuthUser = sessionUser.getUserId() != 0;

        if (isAuthUser) {
            User actualUser = userService.getUser(sessionUser);
            userSession.setUser(actualUser);
        } else {
            log.info("User doesn't auth");
        }

        log.info("Ended getting actual user interceptor");
        return true;
    }
}
