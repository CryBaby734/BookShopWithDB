package org.example;

import java.sql.*;

public class CustomerDatabase {
    private static final String URL = "jdbc:postgresql://localhost:5432/BookShop";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root7769";


    public void addCustomer(Customer customer){
        String insertSQL = "INSERT INTO customers (name, contact_info) VALUES(?, ?)";
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS)) {

           preparedStatement.setString(1,customer.getName());
           preparedStatement.setString(2,customer.getContactInfo());
           preparedStatement.executeUpdate();

           ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
           if(generatedKeys.next()){
               customer.setId(generatedKeys.getInt(1));
           }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Customer findCustomer(String name) {
        String selectSQL = "SELECT * FROM customers WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String contactInfo = resultSet.getString("contact_info");
                return new Customer(id, name, contactInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customer findClientById(int customerId) {
        String selectSQL = "SELECT * FROM customers WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contact_Info = resultSet.getString("contact_Info");
                return new Customer(id, name, contact_Info);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public void removeCustomer(int customerId) {
        String deleteOrderItemsSQL = "DELETE FROM order_items WHERE order_id IN (SELECT id FROM orders WHERE customer_id = ?)";
        String deleteOrdersSQL = "DELETE FROM orders WHERE customer_id = ?";
        String deleteCustomerSQL = "DELETE FROM customers WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false); // Start transaction

            // Удаление элементов заказов, связанных с заказами клиента
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteOrderItemsSQL)) {
                preparedStatement.setInt(1, customerId);
                preparedStatement.executeUpdate();
            }

            // Удаление заказов, связанных с клиентом
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteOrdersSQL)) {
                preparedStatement.setInt(1, customerId);
                preparedStatement.executeUpdate();
            }

            // Удаление клиента
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteCustomerSQL)) {
                preparedStatement.setInt(1, customerId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Removed Customer with id " + customerId);
                } else {
                    System.out.println("No customer found with id " + customerId);
                }
            }

            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
