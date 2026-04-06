package com.bookstore.exceptions;

import jakarta.ws.rs.core.Response;

public class CartNotFoundException extends BaseApiException {
    public CartNotFoundException(int customerId) {
        super(Response.Status.NOT_FOUND, "Cart Not Found", "Cart for customer with ID " + customerId + " does not exist.");
    }
}
