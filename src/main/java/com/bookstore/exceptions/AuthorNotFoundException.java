package com.bookstore.exceptions;

import jakarta.ws.rs.core.Response;

public class AuthorNotFoundException extends BaseApiException {
    public AuthorNotFoundException(int authorId) {
        super(Response.Status.NOT_FOUND, "Author Not Found", "Author with ID " + authorId + " does not exist.");
    }
}
