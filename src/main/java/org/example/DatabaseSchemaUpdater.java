package org.example;
import java.sql.*;

import java.sql.DriverManager;

public class DatabaseSchemaUpdater {
    private static final String URL = "jdbc:postgresql://localhost:5432/BookShop";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root7769";

    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement statement = connection.createStatement()){
            String dropConstraintsql = "ALTER TABLE orders DROP CONSTRAINT orders_customer_id_fkey";
            statement.executeUpdate(dropConstraintsql);

            String addContraintsql = "ALTER TABLE orders ADD CONSTRAINT orders_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE";
            statement.executeUpdate(addContraintsql);

            System.out.println("Database schema updated");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
