package ru.moolls.webchat.utility;

import lombok.extern.log4j.Log4j;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import ru.moolls.webchat.exception.WebServiceException;

@Log4j
public class ExceptionThrower {

    private WebServiceException ex;

    private ExceptionThrower(WebServiceException ex) {
        this.ex = ex;
    }

    public static ExceptionThrower initException(WebServiceException ex) {

        ExceptionThrower exceptionThrower = new ExceptionThrower(ex);
        return exceptionThrower;
    }

    public ExceptionThrower error(String key) {
        ex.addError(key);
        return this;
    }

    public ExceptionThrower validationError(Errors validationErrors) {

        for (FieldError fieldError : validationErrors.getFieldErrors()) {
            ex.addError(fieldError.getField() + "ValidationError");
        }
        return this;
    }

    public void throwException() throws WebServiceException {
        throw ex;
    }
}
