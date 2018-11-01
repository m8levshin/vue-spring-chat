package ru.moolls.webchat.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.moolls.webchat.config.ApplicationProperties;
import ru.moolls.webchat.dao.EventDao;
import ru.moolls.webchat.dao.domain.Event;
import ru.moolls.webchat.dao.domain.enums.UserRoleEnum;
import ru.moolls.webchat.security.annotation.Permission;

import java.util.List;

@Log4j
@RestController
@PropertySource("classpath:app.properties")
public class EventController {

    @Autowired
    private ApplicationProperties config;

    @Autowired
    private EventDao eventDao;

    @GetMapping("/api/events/")
    @Permission(roles = {UserRoleEnum.USER, UserRoleEnum.ADMIN, UserRoleEnum.ANONYMOUS})
    List<Event> getLastEvents() {
        log.info("Started getting last events process");
        int countLastMessageInChat = config.getLastEventCount();
        List<Event> lastEvents = eventDao.getLastEvents(countLastMessageInChat);
        log.info("Ended getting last events process");
        return lastEvents;
    }
}
