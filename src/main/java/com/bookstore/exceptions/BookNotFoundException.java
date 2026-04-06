package com.bookstore.exceptions;

import jakarta.ws.rs.core.Response;

public class BookNotFoundException extends BaseApiException {
    public BookNotFoundException(int bookId) {
        super(Response.Status.NOT_FOUND, "Book Not Found", "Book with ID " + bookId + " does not exist.");
    }
}
