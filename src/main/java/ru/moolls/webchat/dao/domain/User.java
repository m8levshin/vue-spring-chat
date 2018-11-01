package ru.moolls.webchat.dao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Timestamp;


@Getter
@Setter
@ToString(exclude = {"passMd5"})
@EqualsAndHashCode(of = "userId")
@NoArgsConstructor
public class User {
    private long userId;
    private String userName;

    @JsonIgnore
    private String passMd5;
    private Timestamp regTimestamp;
    private UserStatus userStatus;
    private UserRole userRole;

}
