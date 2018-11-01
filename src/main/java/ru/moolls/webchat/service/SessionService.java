package ru.moolls.webchat.service;

import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moolls.webchat.dao.EventDao;
import ru.moolls.webchat.dao.UserDao;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.UserStatus;
import ru.moolls.webchat.dao.domain.enums.UserStatusEnum;
import ru.moolls.webchat.exception.ProcessingRequestException;
import ru.moolls.webchat.exception.WebServiceException;
import ru.moolls.webchat.utility.ExceptionThrower;

@Log4j
@Service
public class SessionService {

    @Autowired
    UserDao userDao;

    @Autowired
    EventDao eventDao;

    @Autowired
    EventService eventService;

    public User authUser(String userName, String password, String remoteIp) throws WebServiceException {
        log.info("Login attempt with user=" + userName + "; password:*******");
        String passMd5 = DigestUtils.md5Hex(password);
        User foundUser = userDao.getUserByNameAndPassword(userName, passMd5);

        if (foundUser == null) {
            log.warn("Login attempt for user:" + userName + "  failed");
            ExceptionThrower
                    .initException(new ProcessingRequestException())
                    .error("badCredentialError")
                    .throwException();
        }

        UserStatus foundUserStatus = foundUser.getUserStatus();
        if (foundUserStatus.equals(UserStatusEnum.BANNED.getValue())) {
            log.warn("Login attempt for user:" + userName + "  failed. User banned.");
            ExceptionThrower
                    .initException(new ProcessingRequestException())
                    .error("userBannedError")
                    .throwException();
        }

        if (foundUserStatus.equals(UserStatusEnum.KICKED.getValue())) {
            log.info("Changing status to active to userID=" + foundUser.getUserId());
            foundUser.setUserStatus(UserStatusEnum.ACTIVE.getValue());
            userDao.updateUser(foundUser);
        }

        log.info("Login attempt for user:" + userName + "  successful; UserID=" + foundUser.getUserId());
        eventService.createAuthEvent(foundUser, remoteIp);

        return foundUser;
    }


    public void logOutUser(User user, String remoteIp) {
        log.info("Logout service method for user=" + user.getUserName());
        eventService.createLogOutEvent(user, remoteIp);
    }


    public User kickUser(User initiatorUser, long kickingUserId, String remoteIp)
            throws WebServiceException {
        log.info("Started kick service method. User with ID=" + initiatorUser.getUserId() +
                " trying kick user with ID=" + kickingUserId);

        User kickedUser = userDao.getUserById(kickingUserId);

        if (kickedUser == null) {
            log.warn("User with the same ID doesn't exists");
            ExceptionThrower.initException(new ProcessingRequestException())
                    .error("kickUserNotFoundError")
                    .throwException();
        }

        if (initiatorUser.equals(kickedUser)) {
            log.warn("Self kick attempt for user with ID=" + kickedUser.getUserId());
            ExceptionThrower.initException(new ProcessingRequestException())
                    .error("selfKickError")
                    .throwException();
        }

        kickedUser.setUserStatus(UserStatusEnum.KICKED.getValue());
        kickedUser = userDao.updateUser(kickedUser);
        eventService.createKickUserEvent(initiatorUser, kickedUser, remoteIp);

        log.info("Ended kick service method. User with ID=" + initiatorUser.getUserId() +
                " kicked user with ID=" + kickingUserId);

        return kickedUser;
    }


}
