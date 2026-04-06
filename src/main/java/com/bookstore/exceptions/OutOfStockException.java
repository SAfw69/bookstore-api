package com.bookstore.exceptions;

import jakarta.ws.rs.core.Response;

public class OutOfStockException extends BaseApiException {
    public OutOfStockException(int bookId) {
        super(Response.Status.BAD_REQUEST, "Out Of Stock", "Book with ID " + bookId + " is currently out of stock.");
    }
}
