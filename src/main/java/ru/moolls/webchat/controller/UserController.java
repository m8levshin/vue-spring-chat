package ru.moolls.webchat.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.enums.UserRoleEnum;
import ru.moolls.webchat.dto.RegDto;
import ru.moolls.webchat.exception.EnumException;
import ru.moolls.webchat.exception.ProcessingRequestException;
import ru.moolls.webchat.exception.WebServiceException;
import ru.moolls.webchat.security.annotation.Permission;
import ru.moolls.webchat.service.UserService;
import ru.moolls.webchat.session.UserSession;
import ru.moolls.webchat.session.UserSessionManager;
import ru.moolls.webchat.utility.ExceptionThrower;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

@Log4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionManager userSessionManager;

    @Autowired
    private UserSession userSession;

    @GetMapping("/api/users/")
    @Permission(roles = {UserRoleEnum.ADMIN, UserRoleEnum.USER, UserRoleEnum.ANONYMOUS})
    Set<User> getOnlineUsers() {
        log.info("Started getting  online user process");
        Set<User> onlineUsers = userSessionManager.getUsers();
        log.info("Ended getting  online user process");

        return onlineUsers;
    }

    @PostMapping("/api/users/")
    @Permission(roles = {UserRoleEnum.ANONYMOUS}, exception = EnumException.ALREADY_AUTH)
    User regUser(@Valid @RequestBody RegDto regDto,
                 Errors validationErrors,
                 HttpServletRequest request)
            throws WebServiceException {

        log.info("Started reg user process with username=" + regDto.toString());
        if (validationErrors.hasErrors()) {
            log.warn("Input validation error for trying with username=" + regDto.getUserName());
            ExceptionThrower
                    .initException(new ProcessingRequestException())
                    .validationError(validationErrors)
                    .throwException();
        }
        String userName = regDto.getUserName();
        String password = regDto.getPassword();

        User registeredUser = userService.regUser(userName, password, request.getRemoteAddr());
        log.debug("New user: " + registeredUser);
        log.info("Ended reg user process with username=" + regDto.toString());
        return registeredUser;
    }


    @DeleteMapping("/api/users/{userId}/")
    @Permission(roles = {UserRoleEnum.ADMIN})
    void banUser(long userId, HttpServletRequest request) throws WebServiceException {

        User banInitiatorUser = userSession.getUser();
        log.info("Started ban process from user with ID=" + banInitiatorUser.getUserId() +
                " to user with ID=" + userId);

        User banningUser = userService.banUser(banInitiatorUser, userId, request.getRemoteAddr());
        userSessionManager.remove(banningUser);

        log.info("Ended ban process from user with ID=" + banInitiatorUser.getUserId() +
                " to user with ID=" + userId);
    }

}
