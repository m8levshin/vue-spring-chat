package ru.moolls.webchat.dao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "userStatusId")
@AllArgsConstructor
@NoArgsConstructor
public class UserStatus {
    private long userStatusId;
    @JsonIgnore
    private String statusName;

}
