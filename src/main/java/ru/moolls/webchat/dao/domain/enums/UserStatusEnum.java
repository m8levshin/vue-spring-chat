package ru.moolls.webchat.dao.domain.enums;

import ru.moolls.webchat.dao.domain.UserStatus;

public enum UserStatusEnum {

    ACTIVE(1), BANNED(2), KICKED(3);

    private long userStatusId;

    UserStatusEnum(long userStatusId) {
        this.userStatusId = userStatusId;
    }

    public static UserStatus getUserRoleById(long userStatusId) {
        for (UserStatusEnum value : UserStatusEnum.values()) {
            if (value.userStatusId == userStatusId) {
                return value.getValue();
            }
        }
        return null;
    }

    public UserStatus getValue() {
        UserStatus userStatus = new UserStatus(userStatusId, this.name());
        return userStatus;
    }

}
