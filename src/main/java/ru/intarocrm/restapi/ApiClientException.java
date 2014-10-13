package ru.intarocrm.restapi;

public class ApiClientException extends Exception {

    private static final long serialVersionUID = 1L;

    public ApiClientException(String text) {
        super(text);
    }

    public ApiClientException(String text, Exception innerEx) {
        super(text, innerEx);
    }

    public ApiClientException(Exception innerEx) {
        super(innerEx);
    }

}
