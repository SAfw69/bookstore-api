package com.bookstore.resources;

import com.bookstore.exceptions.CustomerNotFoundException;
import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.models.Customer;
import com.bookstore.store.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private final DataStore store = DataStore.getInstance();

    @GET
    public Collection<Customer> getCustomers() {
        return store.getAllCustomers().values();
    }

    @POST
    public Response addCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new InvalidInputException("Customer name cannot be empty");
        }
        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            throw new InvalidInputException("Valid email is required");
        }
        Customer created = store.addCustomer(customer);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Customer getCustomer(@PathParam("id") int id) {
        Customer customer = store.getCustomer(id);
        if (customer == null) {
            throw new CustomerNotFoundException(id);
        }
        return customer;
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id") int id, Customer customer) {
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new InvalidInputException("Customer name cannot be empty");
        }
        Customer updated = store.updateCustomer(id, customer);
        if (updated == null) {
            throw new CustomerNotFoundException(id);
        }
        return updated;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        boolean deleted = store.deleteCustomer(id);
        if (!deleted) {
            throw new CustomerNotFoundException(id);
        }
        return Response.noContent().build();
    }
}
