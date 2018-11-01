package ru.moolls.webchat.service;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moolls.webchat.dao.EventDao;
import ru.moolls.webchat.dao.domain.Event;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.enums.EventTypeEnum;

@Log4j
@Service
public class EventService {

    @Autowired
    private EventDao eventDao;


    public Event getLastEventOfUser(User user) {
        Event lastEventOfUser = eventDao.getLastEventOfUser(user);
        return lastEventOfUser;
    }

    public void createLogOutEvent(User user, String remoteIp) {
        log.info("Creating logout event for user with ID:" + user.getUserId());
        Event event = new Event();
        event.setEventType(EventTypeEnum.LOGOUT.getValue());
        event.setFromUser(user);
        event.setIp(remoteIp);
        eventDao.putEvent(event);
    }


    public void createKickUserEvent(User initiatorUser, User kickingUser, String remoteIp) {
        log.info("Creating kick event for user with ID=" + kickingUser.getUserId() +
                " from user with ID=" + initiatorUser.getUserId());
        Event event = new Event();
        event.setEventType(EventTypeEnum.KICKED.getValue());
        event.setFromUser(initiatorUser);
        event.setToUser(kickingUser);
        event.setIp(remoteIp);
        eventDao.putEvent(event);
    }

    public void createAuthEvent(User user, String remoteIp) {
        log.info("Creating auth event for user with ID:" + user.getUserId());
        Event event = new Event();
        event.setEventType(EventTypeEnum.LOGIN.getValue());
        event.setFromUser(user);
        event.setIp(remoteIp);
        eventDao.putEvent(event);
    }


    public void createUserRegEvent(User user, String remoteIp) {
        log.info("Creating reg event for new user with username:" + user.getUserName());
        Event event = new Event();
        event.setEventType(EventTypeEnum.REGISTERED.getValue());
        event.setFromUser(user);
        event.setIp(remoteIp);
        eventDao.putEvent(event);
    }
}
