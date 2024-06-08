package org.example;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private int orderId;
    private Customer customer;
    private Map<Book, Integer> books; // Map to store books and their quantities
    private boolean isProcessed;

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.books = new HashMap<>();
        this.isProcessed = false;
    }

    public void addBook(Book book, int quantity) {
        books.put(book, quantity);
    }

    public int calculateTotal() {
        return books.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public void processOrder() {
        isProcessed = true;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Map<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(Map<Book, Integer> books) {
        this.books = books;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}
