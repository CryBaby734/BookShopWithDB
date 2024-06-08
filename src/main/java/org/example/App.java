package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
     Scanner console = new Scanner(System.in);
        BookDatabase bookDatabase = new BookDatabase();
        CustomerDatabase customerDatabase = new CustomerDatabase();
        OrderDatabase orderDatabase = new OrderDatabase();


        OrderProcessor orderProcessor = new OrderProcessor(orderDatabase);


        Book book1 = new Book(1, "Book One", "Author One", 123456, 20);
        Book book2 = new Book(2, "Book Two", "Author Two", 654321, 25);
        bookDatabase.addBook(book1);
        bookDatabase.addBook(book2);

        System.out.println("Введите ваше имя: ");
        String input = console.nextLine();
        System.out.println("Введите ваш email адрес:");
        String email = console.nextLine();
        Customer customer = new Customer(1, input, email);
        customerDatabase.addCustomer(customer);

        List<Book> booksToOrder = Arrays.asList(book1, book2);
        orderProcessor.placeOrder(customer, booksToOrder);

        orderProcessor.displayOrders();
        System.out.println("Введите id кого вы хотите удалить ?");
        int id = console.nextInt();
        customerDatabase.removeCustomer(id);
    }
}
