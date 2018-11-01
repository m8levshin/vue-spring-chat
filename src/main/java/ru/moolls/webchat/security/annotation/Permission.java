package ru.moolls.webchat.security.annotation;

import ru.moolls.webchat.dao.domain.enums.UserRoleEnum;
import ru.moolls.webchat.exception.EnumException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    UserRoleEnum[] roles();

    EnumException exception() default EnumException.PERMISSION_DENIED;
}
