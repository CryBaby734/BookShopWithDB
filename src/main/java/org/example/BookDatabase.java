package org.example;

import java.sql.*;

public class BookDatabase {
    private static final String URL = "jdbc:postgresql://localhost:5432/BookShop";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root7769";

    public void addBook(Book book) {
        String insertSQL = "INSERT INTO books (name, author, isbn, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, book.getBookname());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getIsbn());
            preparedStatement.setInt(4, book.getPrice());
            preparedStatement.executeUpdate();

            // Получение сгенерированного id
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setIdBook(generatedKeys.getInt(1));
                }
            }

            System.out.println("Book added successfully to the database with ID: " + book.getIdBook());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book findBook(int bookId) {
        String selectSQL = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                int isbn = resultSet.getInt("isbn");
                int price = resultSet.getInt("price");
                return new Book(bookId, name, author, isbn, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        BookDatabase bookDatabase = new BookDatabase();
        Book newBook = new Book("Effective Java", "Joshua Bloch", 1234567890, 45);
        bookDatabase.addBook(newBook);

        Book book = bookDatabase.findBook(newBook.getIdBook());
        if (book != null) {
            System.out.println("Found book: " + book.getBookname());
        } else {
            System.out.println("Book not found.");
        }
    }
}
