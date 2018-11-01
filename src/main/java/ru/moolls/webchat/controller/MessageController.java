package ru.moolls.webchat.controller;


import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.moolls.webchat.config.ApplicationProperties;
import ru.moolls.webchat.dao.domain.Event;
import ru.moolls.webchat.dao.domain.enums.UserRoleEnum;
import ru.moolls.webchat.exception.ProcessingRequestException;
import ru.moolls.webchat.exception.WebServiceException;
import ru.moolls.webchat.security.annotation.Permission;
import ru.moolls.webchat.service.MessageService;
import ru.moolls.webchat.session.UserSession;
import ru.moolls.webchat.utility.ExceptionThrower;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Log4j
@RestController
@PropertySource("classpath:app.properties")
public class MessageController {


    @Autowired
    private ApplicationProperties config;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserSession userSession;


    @GetMapping("/api/messages/")
    @Permission(roles = {UserRoleEnum.ADMIN, UserRoleEnum.USER, UserRoleEnum.ANONYMOUS})
    List<Event> getLastMessages() {
        log.info("Started getting messages process");
        int countLastMessageInChat = config.getLastEventCount();
        List<Event> lastMessageInChat = messageService.getLastMessages(countLastMessageInChat);
        log.info("Ended getting messages process");
        return lastMessageInChat;
    }


    @PostMapping("/api/messages/")
    @Permission(roles = {UserRoleEnum.ADMIN, UserRoleEnum.USER})
    Event sendMessage(@Valid @RequestBody Event event, Errors validationErrors, HttpServletRequest request)
            throws WebServiceException {


        if (validationErrors.hasErrors()) {
            log.warn("Validation error for message from userId" + userSession.getUser().getUserId());
            ExceptionThrower
                    .initException(new ProcessingRequestException())
                    .validationError(validationErrors)
                    .throwException();
        }

        Event newMessageEvent = messageService.sendMessage(userSession.getUser(),
                event.getMessage(), request.getRemoteAddr());
        return newMessageEvent;
    }
}
