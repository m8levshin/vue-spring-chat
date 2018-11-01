package ru.moolls.webchat.dao;

import ru.moolls.webchat.dao.domain.Event;
import ru.moolls.webchat.dao.domain.EventType;
import ru.moolls.webchat.dao.domain.User;

import java.util.List;

public interface EventDao {
    List<Event> getLastEvents(int count);

    List<Event> getLastEventsByType(EventType eventType, int count);

    Event getEventById(long eventId);

    Event putEvent(Event event);

    Event getLastEventOfUser(User user);
}
