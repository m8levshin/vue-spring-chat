package ru.moolls.webchat.exception;

public enum EnumException {

    PERMISSION_DENIED(new PermissionDeniedException()),
    ALREADY_AUTH(new AlreadyAuthException()),
    BAD_USER(new BadUserStatusException());

    private WebServiceException exception;

    EnumException(WebServiceException e) {
        this.exception = e;
    }

    public WebServiceException getException() {
        return exception;
    }
}
