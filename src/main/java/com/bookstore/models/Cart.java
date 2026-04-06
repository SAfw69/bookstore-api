package com.bookstore.models;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private int customerId;
    // Map of bookId to quantity
    private Map<Integer, CartItem> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    public Cart(int customerId) {
        this.customerId = customerId;
        this.items = new HashMap<>();
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public Map<Integer, CartItem> getItems() { return items; }
    public void setItems(Map<Integer, CartItem> items) { this.items = items; }

    public void addItem(int bookId, int quantity) {
        if (items.containsKey(bookId)) {
            CartItem existing = items.get(bookId);
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            items.put(bookId, new CartItem(bookId, quantity));
        }
    }

    public void updateItem(int bookId, int quantity) {
        if (quantity <= 0) {
            items.remove(bookId);
        } else {
            items.put(bookId, new CartItem(bookId, quantity));
        }
    }

    public void removeItem(int bookId) {
        items.remove(bookId);
    }
    
    public void clearCart() {
        items.clear();
    }
}
