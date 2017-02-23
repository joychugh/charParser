package com.jchugh.model.response;

/**
 * Class to represent the response when an error is encountered.
 * This is used by JSON Serializer/Deserializer
 */
public class ExceptionResponse {
    private String message;

    public ExceptionResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
