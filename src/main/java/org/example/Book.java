package org.example;

public class Book {
    private int idBook;
    private String bookname;
    private String author;
    private int isbn;
    private int price;

    public Book(String bookname, String author, int isbn, int price) {
        this.bookname = bookname;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
    }

    public Book(int idBook, String bookname, String author, int isbn, int price) {
        this.idBook = idBook;
        this.bookname = bookname;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
    }

    // Getters and setters
    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
