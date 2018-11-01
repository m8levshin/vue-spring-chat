package ru.moolls.webchat.session;

import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.moolls.webchat.dao.domain.User;

import java.util.HashSet;
import java.util.Set;

@Component
@Log4j
@Scope(value = "application")
public class UserSessionManager {

    private Set<User> userSessions;

    public UserSessionManager() {
        this.userSessions = new HashSet<>();
    }

    public void put(User user) {
        userSessions.add(user);
    }

    public Set<User> getUsers() {
        return userSessions;
    }

    public void remove(User user) {
        userSessions.remove(user);
    }
}
