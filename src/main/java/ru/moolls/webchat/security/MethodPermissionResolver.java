package ru.moolls.webchat.security;


import lombok.extern.log4j.Log4j;
import org.springframework.web.method.HandlerMethod;
import ru.moolls.webchat.dao.domain.UserRole;
import ru.moolls.webchat.dao.domain.enums.UserRoleEnum;
import ru.moolls.webchat.exception.WebServiceException;
import ru.moolls.webchat.security.annotation.Permission;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@Log4j
public class MethodPermissionResolver {


    public List<UserRole> getMethodPermission(Object handler) {
        List<UserRole> userRoles = new ArrayList<>();
        Method method = getReflectionMethod(handler);

        log.info("Started getting permission list for method:" + method.getName());

        if (method.isAnnotationPresent(Permission.class)) {
            log.info("Method annotated by Permission annotation, getting allow roles");
            Permission annotation = method.getAnnotation(Permission.class);
            for (UserRoleEnum userRoleEnum : annotation.roles()) {
                userRoles.add(userRoleEnum.getValue());
            }
            log.debug("Allow roles for method:" + userRoles);
        } else {
            log.info("Method didn't annotated by Permission annotation");
        }

        log.info("Ended getting permission list for method: " + method.getName());

        return userRoles;
    }


    public WebServiceException getThrowableException(Object handler) {
        Method method = getReflectionMethod(handler);

        log.info("Started resolve exception for method:" + method.getName());
        if (!method.isAnnotationPresent(Permission.class)) {
            log.warn("Method doesn't have permission annotation");
            return null;
        }

        Permission annotation = method.getAnnotation(Permission.class);
        WebServiceException webServiceException = annotation.exception().getException();
        log.info("Ended resolve exception for method:" + method.getName());
        return webServiceException;
    }

    private Method getReflectionMethod(Object handler) {
        HandlerMethod hm = (HandlerMethod) handler;
        return hm.getMethod();
    }
}
