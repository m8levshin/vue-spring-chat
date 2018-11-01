package ru.moolls.webchat.service;

import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moolls.webchat.dao.UserDao;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.enums.UserRoleEnum;
import ru.moolls.webchat.dao.domain.enums.UserStatusEnum;
import ru.moolls.webchat.exception.ProcessingRequestException;
import ru.moolls.webchat.exception.WebServiceException;
import ru.moolls.webchat.utility.ExceptionThrower;

@Log4j
@Service
public class UserService {


    @Autowired
    private EventService eventService;

    @Autowired
    private UserDao userDao;


    public User regUser(String userName, String password, String remoteAddr)
            throws WebServiceException {

        log.info("Starting reg user service method for userName=" + userName);

        User userByName = userDao.getUserByName(userName);
        if (userByName != null) {
            log.warn("User with the same name already exists");
            ExceptionThrower.initException(new ProcessingRequestException())
                    .error("userExistError")
                    .throwException();
        }

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPassMd5(DigestUtils.md5Hex(password));
        newUser.setUserRole(UserRoleEnum.USER.getValue());
        newUser.setUserStatus(UserStatusEnum.ACTIVE.getValue());
        newUser = userDao.insertUser(newUser);
        eventService.createUserRegEvent(newUser, remoteAddr);
        log.info("Ending reg user service method for userName=" + userName);
        log.debug("Reged user: " + newUser);

        return newUser;
    }


    public User banUser(User from, long banningUserId, String remoteAddr) throws WebServiceException {

        log.info("Started ban user service method from userId=" + banningUserId +
                " to userId=" + banningUserId);

        User updatebleUser = userDao.getUserById(banningUserId);

        if (updatebleUser == null) {
            log.warn("User with the same ID doesn't exists");
            ExceptionThrower.initException(new ProcessingRequestException())
                    .error("userDoesNotExistError")
                    .throwException();
        }

        if (updatebleUser.equals(from)) {
            log.warn("Self ban attempt for user with ID=" + from.getUserId());
            ExceptionThrower.initException(new ProcessingRequestException())
                    .error("selfDeleteError")
                    .throwException();
        }

        updatebleUser.setUserStatus(UserStatusEnum.BANNED.getValue());
        User updatedUser = userDao.updateUser(updatebleUser);

        log.info("Ended ban user service method from userId=" + banningUserId +
                " to userId=" + banningUserId);
        log.debug("Banned user: " + updatedUser);

        return updatedUser;
    }

    public User getUser(User user) {
        log.info("Starting get user by id service method for userId=" + user.getUserId());
        User userById = userDao.getUserById(user.getUserId());
        log.info("Ending get user by id service method for userId=" + user.getUserId());
        log.debug("Got user: " + userById);

        return userById;
    }
}
