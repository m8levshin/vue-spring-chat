package ru.moolls.webchat.exception;


import java.util.ArrayList;
import java.util.List;

public class WebServiceException extends Exception {

    private List<String> errorList;

    WebServiceException() {
        this.errorList = new ArrayList<>();
    }

    public void addError(String errorKey) {
        errorList.add(errorKey);
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }
}


