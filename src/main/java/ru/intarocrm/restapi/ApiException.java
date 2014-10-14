package ru.intarocrm.restapi;

public class ApiException extends Exception {

    private static final long serialVersionUID = 1L;

    public ApiException(String text) {
        super(text);
    }

    public ApiException(String text, Exception innerEx) {
        super(text, innerEx);
    }

    public ApiException(Exception innerEx) {
        super(innerEx);
    }

}
