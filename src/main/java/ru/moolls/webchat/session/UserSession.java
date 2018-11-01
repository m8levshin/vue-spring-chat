package ru.moolls.webchat.session;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.moolls.webchat.dao.domain.User;

@Getter
@Setter
@ToString
@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@EqualsAndHashCode(of = "user")
public class UserSession {

    @Autowired
    private User user;

}
