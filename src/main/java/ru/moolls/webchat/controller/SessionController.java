package ru.moolls.webchat.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.enums.UserRoleEnum;
import ru.moolls.webchat.dto.AuthDto;
import ru.moolls.webchat.exception.EnumException;
import ru.moolls.webchat.exception.ProcessingRequestException;
import ru.moolls.webchat.exception.WebServiceException;
import ru.moolls.webchat.security.annotation.Permission;
import ru.moolls.webchat.service.SessionService;
import ru.moolls.webchat.session.UserSession;
import ru.moolls.webchat.session.UserSessionManager;
import ru.moolls.webchat.utility.ExceptionThrower;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Log4j
@RestController
public class SessionController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private UserSessionManager userSessionManager;

    @Autowired
    private SessionService sessionService;


    @PostMapping("/api/sessions/")
    @Permission(roles = {UserRoleEnum.ANONYMOUS}, exception = EnumException.ALREADY_AUTH)
    User auth(@Valid @RequestBody AuthDto authForm, Errors validationErrors,
              HttpServletRequest request,
              HttpSession session) throws WebServiceException {

        log.info("Started login process");
        if (validationErrors.hasErrors()) {
            log.warn("Input validation failed for trying auth with username=" + authForm.getUserName());
            ExceptionThrower
                    .initException(new ProcessingRequestException())
                    .validationError(validationErrors)
                    .throwException();
        }

        String userName = authForm.getUserName();
        String password = authForm.getPassword();

        User authUser = sessionService.authUser(userName, password, request.getRemoteAddr());
        userSession.setUser(authUser);
        userSessionManager.put(authUser);
        log.info("Ended login process");
        return authUser;
    }

    @DeleteMapping("/api/sessions/")
    @Permission(roles = {UserRoleEnum.USER, UserRoleEnum.ADMIN})
    void logOut(HttpServletRequest request) {
        User sessionUser = userSession.getUser();
        log.info("Started logout process for user with ID=" + sessionUser.getUserId());
        sessionService.logOutUser(sessionUser, request.getRemoteAddr());
        userSessionManager.remove(sessionUser);
        request.getSession().invalidate();
        log.info("Ended login process for user with ID=" + sessionUser.getUserId());
    }


    @DeleteMapping("/api/sessions/{userId}/")
    @Permission(roles = {UserRoleEnum.ADMIN})
    void kickUser(@PathVariable long userId, HttpServletRequest request)
            throws WebServiceException {
        User sessionUser = userSession.getUser();
        log.info("Started kick process for user with ID=" + userId + " from user with ID=" + sessionUser.getUserId());
        User kickedUser = sessionService.kickUser(sessionUser, userId, request.getRemoteAddr());
        userSessionManager.remove(kickedUser);
        log.info("Ended kick process for user with ID=" + userId + " from user with ID=" + sessionUser.getUserId());
    }


    @GetMapping("/api/sessions/")
    @Permission(roles = {UserRoleEnum.USER, UserRoleEnum.ADMIN})
    User getSession() {
        User sessionUser = userSession.getUser();
        log.info("Started get current session process for user with ID=" + sessionUser.getUserId());
        log.info("Ended" + " get current session process for user with ID=" + sessionUser.getUserId());
        return sessionUser;
    }
}
