package ru.moolls.webchat.dao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "eventId")
public class Event {
    private long eventId;
    private EventType eventType;
    private User fromUser;
    private User toUser;
    @Size(min = 1, max = 255)
    private String message;
    @JsonIgnore
    private String ip;
    private Timestamp timestamp;
}
