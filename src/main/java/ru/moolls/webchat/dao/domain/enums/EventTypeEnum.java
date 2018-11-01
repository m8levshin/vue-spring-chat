package ru.moolls.webchat.dao.domain.enums;

import ru.moolls.webchat.dao.domain.EventType;


public enum EventTypeEnum {

    REGISTERED(1),
    MESSAGE(2),
    LOGOUT(3),
    LOGIN(4),
    KICKED(5),
    BANNED(6);

    private long eventTypeId;

    EventTypeEnum(long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public static EventType getValueById(long eventTypeId) {
        for (EventTypeEnum value : EventTypeEnum.values()) {
            if (value.eventTypeId == eventTypeId) return value.getValue();
        }
        return null;
    }

    public EventType getValue() {
        EventType event = new EventType(this.eventTypeId, this.name());
        return event;
    }
}
