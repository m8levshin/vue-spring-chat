package ru.moolls.webchat.dao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


@Getter
@Setter
@EqualsAndHashCode(of = "userRoleId")
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    private long userRoleId;
    @JsonIgnore
    private String roleName;
}
