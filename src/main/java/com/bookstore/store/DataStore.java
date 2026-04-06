package com.bookstore.store;

import com.bookstore.models.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory datastore using HashMaps to simulate database tables.
 */
public class DataStore {
    // Singleton Instance
    private static final DataStore instance = new DataStore();

    // Data tables
    private final Map<Integer, Book> books = new HashMap<>();
    private final Map<Integer, Author> authors = new HashMap<>();
    private final Map<Integer, Customer> customers = new HashMap<>();
    private final Map<Integer, Cart> carts = new HashMap<>();
    private final Map<Integer, Order> orders = new HashMap<>();

    // ID Generators
    private final AtomicInteger bookIds = new AtomicInteger(1);
    private final AtomicInteger authorIds = new AtomicInteger(1);
    private final AtomicInteger customerIds = new AtomicInteger(1);
    private final AtomicInteger orderIds = new AtomicInteger(1);

    private DataStore() {
        // Pre-populate some dummy data for ease of testing
        Author tolkien = new Author("J.R.R. Tolkien", "English writer, poet, philologist, and academic.");
        addAuthor(tolkien);

        Book lotr = new Book("The Lord of the Rings", tolkien.getId(), "978-0-618-05326-7", 1954, 20.99, 100);
        addBook(lotr);

        // Add a default customer
        Customer guest = new Customer("John Doe", "john.doe@example.com", "password123");
        addCustomer(guest);
    }

    public static DataStore getInstance() {
        return instance;
    }

    // --- Book Operations ---
    public Book addBook(Book book) {
        book.setId(bookIds.getAndIncrement());
        books.put(book.getId(), book);
        return book;
    }

    public Book getBook(int id) {
        return books.get(id);
    }

    public Map<Integer, Book> getAllBooks() {
        return books;
    }

    public Book updateBook(int id, Book book) {
        if (books.containsKey(id)) {
            book.setId(id);
            books.put(id, book);
            return book;
        }
        return null;
    }

    public boolean deleteBook(int id) {
        return books.remove(id) != null;
    }

    // --- Author Operations ---
    public Author addAuthor(Author author) {
        author.setId(authorIds.getAndIncrement());
        authors.put(author.getId(), author);
        return author;
    }

    public Author getAuthor(int id) {
        return authors.get(id);
    }

    public Map<Integer, Author> getAllAuthors() {
        return authors;
    }

    public Author updateAuthor(int id, Author author) {
        if (authors.containsKey(id)) {
            author.setId(id);
            authors.put(id, author);
            return author;
        }
        return null;
    }

    public boolean deleteAuthor(int id) {
        return authors.remove(id) != null;
    }

    public Map<Integer, Book> getBooksByAuthor(int authorId) {
        return books.values().stream()
                .filter(b -> b.getAuthorId() == authorId)
                .collect(Collectors.toMap(Book::getId, b -> b));
    }

    // --- Customer Operations ---
    public Customer addCustomer(Customer customer) {
        customer.setId(customerIds.getAndIncrement());
        customers.put(customer.getId(), customer);
        // Automatically create a cart for the new customer
        carts.put(customer.getId(), new Cart(customer.getId()));
        return customer;
    }

    public Customer getCustomer(int id) {
        return customers.get(id);
    }

    public Map<Integer, Customer> getAllCustomers() {
        return customers;
    }

    public Customer updateCustomer(int id, Customer customer) {
        if (customers.containsKey(id)) {
            customer.setId(id);
            customers.put(id, customer);
            return customer;
        }
        return null;
    }

    public boolean deleteCustomer(int id) {
        carts.remove(id); // Remove their cart too
        return customers.remove(id) != null;
    }

    // --- Cart Operations ---
    public Cart getCart(int customerId) {
        return carts.get(customerId);
    }
    
    // --- Order Operations ---
    public Order addOrder(Order order) {
        order.setId(orderIds.getAndIncrement());
        orders.put(order.getId(), order);
        return order;
    }

    public Order getOrder(int orderId) {
        return orders.get(orderId);
    }

    public Map<Integer, Order> getOrdersByCustomer(int customerId) {
        return orders.values().stream()
                .filter(o -> o.getCustomerId() == customerId)
                .collect(Collectors.toMap(Order::getId, o -> o));
    }
}
