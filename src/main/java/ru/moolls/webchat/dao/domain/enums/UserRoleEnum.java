package ru.moolls.webchat.dao.domain.enums;

import ru.moolls.webchat.dao.domain.UserRole;

public enum UserRoleEnum {
    ANONYMOUS(1), USER(2), ADMIN(3);

    private long userRoleId;

    UserRoleEnum(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public static UserRole getUserRoleById(long userRoleId) {
        for (UserRoleEnum value : UserRoleEnum.values()) {
            if (value.userRoleId == userRoleId) {
                return value.getValue();
            }
        }
        return null;
    }

    public UserRole getValue() {
        UserRole userRole = new UserRole(userRoleId, this.name());
        return userRole;
    }
}
