package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDatabase {
    private static final String URL = "jdbc:postgresql://localhost:5432/BookShop";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root7769";

    public void addOrder(Order order) {
        String insertSQL = "INSERT INTO orders (customer_id, is_processed) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, order.getCustomer().getId());
            preparedStatement.setBoolean(2, order.isProcessed());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    order.setOrderId(resultSet.getInt(1));
                }
            }

            for (Map.Entry<Book, Integer> entry : order.getBooks().entrySet()) {
                addOrderItem(order.getOrderId(), entry.getKey(), entry.getValue());
            }

            System.out.println("Order added successfully to the database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addOrderItem(int orderId, Book book, int quantity) {
        String insertSQL = "INSERT INTO order_items (order_id, book_id, quantity) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, book.getIdBook());
            preparedStatement.setInt(3, quantity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order findOrder(int orderId) {
        String selectSQL = "SELECT * FROM orders WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int orderIdis = resultSet.getInt("id");
                int customerId = resultSet.getInt("customer_id");
                boolean isProcessed = resultSet.getBoolean("is_processed");
                Customer customer = new CustomerDatabase().findClientById(customerId); // Assumes you have a method to find a customer by ID
                Order order = new Order(orderIdis, customer);
                order.setProcessed(isProcessed);

                order.setBooks(getOrderBooks(orderIdis));

                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<Book, Integer> getOrderBooks(int orderId) {
        String selectSQL = "SELECT book_id, quantity FROM order_items WHERE order_id = ?";
        Map<Book, Integer> books = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {

            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                int quantity = resultSet.getInt("quantity");
                Book book = new BookDatabase().findBook(bookId); // Assumes you have a method to find a book by ID
                books.put(book, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Order> getAllOrders() {
        String selectAllSQL = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectAllSQL)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int customerId = resultSet.getInt("customer_id");
                boolean isProcessed = resultSet.getBoolean("is_processed");
                Customer customer = new CustomerDatabase().findClientById(customerId);
                Order order = new Order(id, customer);
                order.setProcessed(isProcessed);
                order.setBooks(getOrderBooks(id));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
