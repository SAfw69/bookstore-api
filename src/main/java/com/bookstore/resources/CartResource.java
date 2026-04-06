package com.bookstore.resources;

import com.bookstore.exceptions.BookNotFoundException;
import com.bookstore.exceptions.CartNotFoundException;
import com.bookstore.exceptions.CustomerNotFoundException;
import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.models.Book;
import com.bookstore.models.Cart;
import com.bookstore.models.CartItem;
import com.bookstore.models.Customer;
import com.bookstore.store.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private final DataStore store = DataStore.getInstance();

    private void ensureCustomerExists(int customerId) {
        if (store.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException(customerId);
        }
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        ensureCustomerExists(customerId);
        Cart cart = store.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }
        return cart;
    }

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem item) {
        ensureCustomerExists(customerId);
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero");
        }
        Book book = store.getBook(item.getBookId());
        if (book == null) {
            throw new BookNotFoundException(item.getBookId());
        }

        Cart cart = store.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }

        cart.addItem(item.getBookId(), item.getQuantity());
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Cart updateCartItemQuantity(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId, CartItem item) {
        ensureCustomerExists(customerId);
        if (store.getBook(bookId) == null) {
            throw new BookNotFoundException(bookId);
        }
        Cart cart = store.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }
        // Assuming the caller sends {"quantity": X}
        cart.updateItem(bookId, item.getQuantity());
        return cart;
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItemFromCart(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId) {
        ensureCustomerExists(customerId);
        Cart cart = store.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }
        cart.removeItem(bookId);
        return Response.noContent().build();
    }
}
