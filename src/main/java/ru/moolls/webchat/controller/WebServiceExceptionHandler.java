package ru.moolls.webchat.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.moolls.webchat.exception.AlreadyAuthException;
import ru.moolls.webchat.exception.ProcessingRequestException;

import java.util.List;

@Log4j
@ControllerAdvice
public class WebServiceExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ProcessingRequestException.class})
    @ResponseBody
    List<String> processExceptionHandler(ProcessingRequestException ex) {
        log.error("Handling ProcessingRequestException exception", ex);
        return ex.getErrorList();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AlreadyAuthException.class})
    @ResponseBody
    List<String> processExceptionHandler(AlreadyAuthException ex) {
        log.warn("Handling AlreadyAuthException exception");
        ex.addError("AlreadyAuthError");
        log.debug(ex);
        return ex.getErrorList();
    }


}
