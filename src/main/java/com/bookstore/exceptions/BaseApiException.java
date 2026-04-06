package com.bookstore.exceptions;

import jakarta.ws.rs.core.Response;

public abstract class BaseApiException extends RuntimeException {
    private final Response.Status status;
    private final String errorName;

    public BaseApiException(Response.Status status, String errorName, String message) {
        super(message);
        this.status = status;
        this.errorName = errorName;
    }

    public Response.Status getStatus() { return status; }
    public String getErrorName() { return errorName; }
}
