package ru.moolls.webchat.security.interceptor;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.UserStatus;
import ru.moolls.webchat.dao.domain.enums.UserStatusEnum;
import ru.moolls.webchat.exception.BadUserStatusException;
import ru.moolls.webchat.session.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Log4j
public class BadUserStatusInterceptor implements HandlerInterceptor {

    @Autowired
    private UserSession userSession;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.info("Started permission checker interceptor");
        User sessionUser = userSession.getUser();
        boolean isAuthUser = sessionUser.getUserId() != 0;

        if (isAuthUser) {
            UserStatus actualUserStatus = sessionUser.getUserStatus();

            if (actualUserStatus.equals(UserStatusEnum.KICKED.getValue()) ||
                    actualUserStatus.equals(UserStatusEnum.BANNED.getValue())) {
                log.info("UserId" + sessionUser.getUserId() + " has BANNED or KICKED status");
                log.info("Invalidate userId=" + sessionUser.getUserId() + " session");
                request.getSession().invalidate();
                throw new BadUserStatusException();
            }
        }
        log.info("UserId=" + sessionUser.getUserId() + " doesn't have bad status.");
        return true;
    }


}
