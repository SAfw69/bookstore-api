package com.bookstore.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Filter that restricts access to the API to Postman requests only.
 */
@Provider
public class PostmanFilter implements ContainerRequestFilter {

    // Global flag to unlock browser visibility after Postman has been used
    private static boolean isUnlocked = false;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String userAgent = requestContext.getHeaderString("User-Agent");

        // Check if the request is from Postman
        boolean isPostman = userAgent != null && userAgent.contains("PostmanRuntime");

        if (isPostman) {
            isUnlocked = true; // Unlock for everyone once Postman is used
        }

        // If not unlocked and not Postman, block access
        if (!isUnlocked && !isPostman) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\": \"Access Restricted\", \"message\": \"Please use Postman to view this data for the first time to unlock browser access.\"}")
                    .type("application/json")
                    .build());
        }
    }
}
