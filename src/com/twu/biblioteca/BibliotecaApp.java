package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.BibliotecaAppQuitException;
import com.twu.biblioteca.exceptions.BookNotAvailableException;
import com.twu.biblioteca.exceptions.BookNotCheckedOutException;
import com.twu.biblioteca.exceptions.BookNotFoundException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public final class BibliotecaApp {

    private final Scanner scanner;
    private final OutputStream outputStream;
    private final Library library;

    private static final String LIST_BOOKS_OPTION = "List Books";
    private static final String CHECKOUT_BOOK_OPTION = "Checkout Book: <Title>";
    private static final String CHECKOUT_BOOK_COMMAND = "Checkout Book";
    private static final String RETURN_BOOK_OPTION = "Return Book: <Title>";
    private static final String RETURN_BOOK_COMMAND = "Return Book";
    private static final String QUIT_OPTION = "Quit";

    public BibliotecaApp(Scanner scanner, OutputStream outputStream) {
        if (scanner == null) throw new IllegalArgumentException("scanner cannot be null");
        if (outputStream == null) throw new IllegalArgumentException("output stream cannot be null");
        this.scanner = scanner;
        this.outputStream = outputStream;
        library = new Library(getDefaultBooks());
    }

    public void run() throws IOException, BibliotecaAppQuitException {
        displayWelcomeMessage(outputStream);
        displayMenuOptions(outputStream);

        while(scanner.hasNextLine())
            selectMenuOption(scanner.nextLine(), outputStream);
    }

    protected void displayWelcomeMessage(OutputStream outputStream) throws IOException {
        writeLine("Welcome to Biblioteca!", outputStream);
    }

    protected void displayMenuOptions(OutputStream outputStream) throws IOException {
        writeLine("Please use one of the following options:", outputStream);
        writeLine(LIST_BOOKS_OPTION, outputStream);
        writeLine(CHECKOUT_BOOK_OPTION, outputStream);
        writeLine(RETURN_BOOK_OPTION, outputStream);
        writeLine(QUIT_OPTION, outputStream);
    }

    protected void selectMenuOption(String option, OutputStream outputStream) throws IOException, BibliotecaAppQuitException {
        option = option == null ? "" : option;

        final String[] input = option.split(":");
        final String command = input[0];

        if (LIST_BOOKS_OPTION.equals(command))
            listBooks(outputStream);
        else if (QUIT_OPTION.equals(command))
            quit(outputStream);
        else if (CHECKOUT_BOOK_COMMAND.equals(command) && input.length == 2)
            checkoutBook(input[1].trim(), outputStream);
        else if (RETURN_BOOK_COMMAND.equals(command) && input.length == 2)
            returnBook(input[1].trim(), outputStream);
        else
            writeLine("Select a valid option!", outputStream);
    }

    protected void listBooks(OutputStream outputStream) throws IOException {
        writeLine("Title, Author, Year", outputStream);
        for( Book book : library.getBooks())
            writeLine(book.getTitle() + ", " + book.getAuthor() + ", " + book.getYear(), outputStream);
    }

    protected void checkoutBook(String title, OutputStream outputStream) throws IOException {
        try {
            library.checkoutBook(title);
            writeLine("Thank you! Enjoy the book.", outputStream);
        } catch (BookNotFoundException e) {
            writeLine("That book is not available.", outputStream);
        } catch (BookNotAvailableException e) {
            writeLine("That book is not available.", outputStream);
        }
    }

    protected void returnBook(String title, OutputStream outputStream) throws IOException {
        try {
            library.returnBook(title);
            writeLine("Thank you for returning the book.", outputStream);
        } catch (BookNotCheckedOutException e) {
            writeLine("That is not a valid book to return.", outputStream);
        } catch (BookNotFoundException e) {
            writeLine("That is not a valid book to return.", outputStream);
        }
    }

    protected void quit(OutputStream outputStream) throws BibliotecaAppQuitException, IOException {
        writeLine("Thank you for using Biblioteca App!", outputStream);
        throw new BibliotecaAppQuitException();
    }

    private void writeLine(String text, OutputStream outputStream) throws IOException {
        outputStream.write((text + "\n").getBytes());
    }

    public static void main(String[] args) throws IOException {
        final BibliotecaApp app = new BibliotecaApp(new Scanner(System.in), System.out);
        try {
            app.run();
        } catch (BibliotecaAppQuitException e) {
            System.exit(0);
        }
    }

    private static SortedSet<Book> getDefaultBooks() {
        final SortedSet<Book> books = new TreeSet<Book>();
        books.add(new Book(1, "Great Expectations", "Charles Dickens", "1860"));
        books.add(new Book(2, "The Pickwick Papers", "Charles Dickens", "1837"));
        books.add(new Book(3, "Bleak House", "Charles Dickens", "1853"));
        return books;
    }

}
