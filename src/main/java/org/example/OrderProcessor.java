package org.example;

import java.util.List;
import java.util.Random;

public class OrderProcessor {

    private OrderDatabase orderDatabase;

    public OrderProcessor(OrderDatabase orderDatabase) {
        this.orderDatabase = orderDatabase;
    }

    public void placeOrder(Customer customer, List<Book> books) {
        int orderId = generateOrderId();
        Order order = new Order(orderId, customer);

        for (Book book : books) {
            int quantity = new Random().nextInt(10) + 1;
            order.addBook(book, quantity);
        }

        orderDatabase.addOrder(order);
    }

    private int generateOrderId() {
        Random random = new Random();
        return random.nextInt(1000000);
    }

    public void displayOrders() {
        List<Order> orders = orderDatabase.getAllOrders();
        System.out.println("Orders:");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Customer: " + order.getCustomer().getName());
            System.out.println("Total: $" + order.calculateTotal());
            System.out.println("Processed: " + order.isProcessed());
            System.out.println("Books:");
            for (Book book : order.getBooks().keySet()) {
                int quantity = order.getBooks().get(book);
                System.out.println(quantity + "x " + book.getBookname() + " by " + book.getAuthor());
            }
            System.out.println("----------------------");
        }
    }
}
