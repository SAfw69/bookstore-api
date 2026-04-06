package com.bookstore.exceptions;

import jakarta.ws.rs.core.Response;

public class InvalidInputException extends BaseApiException {
    public InvalidInputException(String message) {
        super(Response.Status.BAD_REQUEST, "Invalid Input", message);
    }
}
