package com.bookstore.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<BaseApiException> {
    @Override
    public Response toResponse(BaseApiException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getErrorName());
        errorResponse.put("message", ex.getMessage());

        return Response.status(ex.getStatus())
                .entity(errorResponse)
                .type(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .build();
    }
}
