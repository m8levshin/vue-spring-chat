package ru.moolls.webchat.service;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moolls.webchat.dao.EventDao;
import ru.moolls.webchat.dao.domain.Event;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.enums.EventTypeEnum;

import java.util.List;

@Log4j
@Service
public class MessageService {

    @Autowired
    private EventDao eventDao;

    public Event sendMessage(User userFrom, String message, String remoteIp) {
        log.info("Starting send message service method for userId=" + userFrom.getUserId());
        Event event = new Event();
        event.setEventType(EventTypeEnum.MESSAGE.getValue());
        event.setFromUser(userFrom);
        event.setMessage(message);
        event.setIp(remoteIp);
        Event newMessageEvent = eventDao.putEvent(event);
        log.info("Ending send message service method for userId=" + userFrom.getUserId());
        log.debug("Event for new message: " + newMessageEvent);

        return newMessageEvent;
    }

    public List<Event> getLastMessages(int count) {
        log.info("Starting get last messages service method");
        List<Event> lastEventsByType = eventDao.getLastEventsByType(EventTypeEnum.MESSAGE.getValue(), count);
        log.info("Ending get last messages service method");
        return lastEventsByType;
    }
}
