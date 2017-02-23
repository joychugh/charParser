package com.jchugh.handler;

/**
 * Class that represents internal response.
 */
public class InternalResponse {
    private final String response;
    private final int statusCode;

    public InternalResponse(String response, int statusCode) {
        this.response = response;
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }


}
