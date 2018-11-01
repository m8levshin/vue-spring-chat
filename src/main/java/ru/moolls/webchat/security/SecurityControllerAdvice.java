package ru.moolls.webchat.security;


import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.moolls.webchat.exception.BadUserStatusException;
import ru.moolls.webchat.exception.PermissionDeniedException;

import java.util.List;

@Log4j
@ControllerAdvice
public class SecurityControllerAdvice {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({PermissionDeniedException.class})
    void authFailedExceptionHandler() {
        log.warn("Handling PermissionDeniedException exception");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({BadUserStatusException.class})
    @ResponseBody
    List<String> processExceptionHandler(BadUserStatusException ex) {
        log.warn("Handling  BadUserStatusException exception");
        ex.addError("YouKickedOrBan");
        log.debug(ex);
        return ex.getErrorList();
    }

}
