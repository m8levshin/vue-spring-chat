package ru.moolls.webchat.dao.domain;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "eventTypeId")
@AllArgsConstructor
@NoArgsConstructor
public class EventType {
    private long eventTypeId;
    private String eventName;

}
