package ru.moolls.webchat.dao;

import ru.moolls.webchat.dao.domain.User;

public interface UserDao {
    User getUserById(long userId);

    User getUserByNameAndPassword(String userName, String password);

    User getUserByName(String userName);

    User insertUser(User user);

    User updateUser(User user);
}
