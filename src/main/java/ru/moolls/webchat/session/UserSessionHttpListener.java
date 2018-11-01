package ru.moolls.webchat.session;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.moolls.webchat.dao.domain.User;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.util.Enumeration;

@Component
public class UserSessionHttpListener implements javax.servlet.http.HttpSessionListener, ApplicationContextAware {

    @Autowired
    UserSessionManager userSessionManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ((WebApplicationContext) applicationContext).getServletContext().addListener(this);
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object attribute = session.getAttribute(attributeNames.nextElement());
            if (attribute instanceof UserSession) {
                UserSession userSession = (UserSession) attribute;
                User sessionUser = userSession.getUser();
                userSessionManager.remove(sessionUser);
            }
        }
    }
}