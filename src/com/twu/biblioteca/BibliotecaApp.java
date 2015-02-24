package com.twu.biblioteca;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public final class BibliotecaApp {

    private final Scanner scanner;
    private final OutputStream outputStream;
    private final Library library;

    private static final String LIST_BOOKS_OPTION = "List Books";
    private static final String QUIT_OPTION = "Quit";

    public BibliotecaApp(Scanner scanner, OutputStream outputStream) {
        if (scanner == null) throw new IllegalArgumentException("scanner cannot be null");
        if (outputStream == null) throw new IllegalArgumentException("output stream cannot be null");
        this.scanner = scanner;
        this.outputStream = outputStream;
        library = new Library(getDefaultBooks());
    }

    public void run() throws IOException {
        writeLine("Welcome to Biblioteca!");
        writeLine("Please use one of the following options:");
        writeLine(LIST_BOOKS_OPTION);
        writeLine(QUIT_OPTION);
    }

    protected void listBooks() throws IOException {
        writeLine("Title, Author, Year");
        for( Book book : library.getBooks())
            writeLine(book.getTitle() + ", " + book.getAuthor() + ", " + book.getYear());
    }

    protected void selectMenuOption(String option) throws IOException, BibliotecaAppQuitException {
        if (LIST_BOOKS_OPTION.equals(option))
            listBooks();
        else if (QUIT_OPTION.equals(option))
            quit();
        else
            writeLine("Select a valid option!");
    }

    protected void checkoutBook(String title) throws IOException {
        writeLine("Thank you! Enjoy the book.");
    }

    protected Library getLibrary() {
        return library;
    }

    protected void quit() throws BibliotecaAppQuitException {
        throw new BibliotecaAppQuitException();
    }

    private void writeLine(String text) throws IOException {
        outputStream.write((text + "\n").getBytes());
    }

    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }

    private static SortedSet<Book> getDefaultBooks() {
        final SortedSet<Book> books = new TreeSet<Book>();
        books.add(new Book(1, "Great Expectations", "Charles Dickens", "1860"));
        books.add(new Book(2, "The Pickwick Papers", "Charles Dickens", "1837"));
        books.add(new Book(3, "Bleak House", "Charles Dickens", "1853"));
        return books;
    }

}
