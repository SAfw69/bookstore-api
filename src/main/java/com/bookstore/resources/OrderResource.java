package com.bookstore.resources;

import com.bookstore.exceptions.CartNotFoundException;
import com.bookstore.exceptions.CustomerNotFoundException;
import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.exceptions.OutOfStockException;
import com.bookstore.models.Book;
import com.bookstore.models.Cart;
import com.bookstore.models.CartItem;
import com.bookstore.models.Order;
import com.bookstore.store.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final DataStore store = DataStore.getInstance();

    private void ensureCustomerExists(int customerId) {
        if (store.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException(customerId);
        }
    }

    @GET
    public Collection<Order> getOrders(@PathParam("customerId") int customerId) {
        ensureCustomerExists(customerId);
        return store.getOrdersByCustomer(customerId).values();
    }

    @GET
    @Path("/{orderId}")
    public Order getOrder(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        ensureCustomerExists(customerId);
        Order order = store.getOrder(orderId);
        if (order == null || order.getCustomerId() != customerId) {
            throw new WebApplicationException("Order Not Found", Response.Status.NOT_FOUND);
        }
        return order;
    }

    @POST
    public Response createOrder(@PathParam("customerId") int customerId) {
        ensureCustomerExists(customerId);
        Cart cart = store.getCart(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new InvalidInputException("Cannot create an order from an empty cart.");
        }

        double totalAmount = 0.0;
        List<CartItem> orderedItems = new ArrayList<>();

        for (CartItem item : cart.getItems().values()) {
            Book book = store.getBook(item.getBookId());
            if (book == null) {
                cart.removeItem(item.getBookId()); // cleanup invalid book
                continue;
            }
            if (book.getStock() < item.getQuantity()) {
                throw new OutOfStockException(book.getId());
            }
            // Decrease stock
            book.setStock(book.getStock() - item.getQuantity());
            totalAmount += (book.getPrice() * item.getQuantity());
            orderedItems.add(item);
        }

        Order order = new Order(customerId, orderedItems, totalAmount);
        store.addOrder(order);
        
        // Clear the cart after successful order creation
        cart.clearCart();

        return Response.status(Response.Status.CREATED).entity(order).build();
    }
}
