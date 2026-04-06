package com.bookstore.exceptions;

import jakarta.ws.rs.core.Response;

public class CustomerNotFoundException extends BaseApiException {
    public CustomerNotFoundException(int customerId) {
        super(Response.Status.NOT_FOUND, "Customer Not Found", "Customer with ID " + customerId + " does not exist.");
    }
}
