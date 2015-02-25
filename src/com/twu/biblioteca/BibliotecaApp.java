package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.BibliotecaAppQuitException;
import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;
import com.twu.biblioteca.exceptions.LibraryItemNotFoundException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public final class BibliotecaApp {

    private final Scanner scanner;
    private final OutputStream outputStream;
    private final Library<Book> bookLibrary;
    private final Library<Movie> movieLibrary;

    private final Customer customer = new Customer("Charles", "Dickens", "charles@example.com", "Password1", "123-4567");

    private static final String LIST_BOOKS_OPTION = "List Books";
    private static final String LIST_MOVIES_OPTION = "List Movies";
    private static final String CHECKOUT_BOOK_OPTION = "Checkout Book: <Title>";
    private static final String CHECKOUT_BOOK_COMMAND = "Checkout Book";
    private static final String RETURN_BOOK_OPTION = "Return Book: <Title>";
    private static final String RETURN_BOOK_COMMAND = "Return Book";
    private static final String CHECKOUT_MOVIE_OPTION = "Checkout Movie: <Title>";
    private static final String CHECKOUT_MOVIE_COMMAND = "Checkout Movie";
    private static final String RETURN_MOVIE_OPTION = "Return Movie: <Title>";
    private static final String RETURN_MOVIE_COMMAND = "Return Movie";
    private static final String QUIT_OPTION = "Quit";

    public BibliotecaApp(Scanner scanner, OutputStream outputStream) {
        if (scanner == null) throw new IllegalArgumentException("scanner cannot be null");
        if (outputStream == null) throw new IllegalArgumentException("output stream cannot be null");
        this.scanner = scanner;
        this.outputStream = outputStream;
        bookLibrary = new Library<Book>(Book.getDefaultBooks());
        movieLibrary = new Library<Movie>(Movie.getDefaultMovies());
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
        writeLine(LIST_MOVIES_OPTION, outputStream);
        writeLine(CHECKOUT_BOOK_OPTION, outputStream);
        writeLine(CHECKOUT_MOVIE_OPTION, outputStream);
        writeLine(RETURN_BOOK_OPTION, outputStream);
        writeLine(RETURN_MOVIE_OPTION, outputStream);
        writeLine(QUIT_OPTION, outputStream);
    }

    protected void selectMenuOption(String option, OutputStream outputStream) throws IOException, BibliotecaAppQuitException {
        option = option == null ? "" : option;

        final String[] input = option.split(":");
        final String command = input[0];

        if (LIST_BOOKS_OPTION.equals(command))
            listBooks(outputStream);
        else if (LIST_MOVIES_OPTION.equals(command))
            listMovies(outputStream);
        else if (QUIT_OPTION.equals(command))
            quit(outputStream);
        else if (CHECKOUT_BOOK_COMMAND.equals(command) && input.length == 2)
            checkoutBook(input[1].trim(), outputStream);
        else if (CHECKOUT_MOVIE_COMMAND.equals(command) && input.length == 2)
            checkoutMovie(input[1].trim(), outputStream);
        else if (RETURN_BOOK_COMMAND.equals(command) && input.length == 2)
            returnBook(input[1].trim(), outputStream);
        else if (RETURN_MOVIE_COMMAND.equals(command) && input.length == 2)
            returnMovie(input[1].trim(), outputStream);
        else
            writeLine("Select a valid option!", outputStream);
    }

    protected void listBooks(OutputStream outputStream) throws IOException {
        listItems(bookLibrary, Book.getCSVHeaders(), outputStream);
    }

    public void listMovies(OutputStream outputStream) throws IOException {
        listItems(movieLibrary, Movie.getCSVHeaders(), outputStream);
    }

    private static void listItems(Library<?> library, String csvHeader, OutputStream outputStream) throws IOException {
        writeLine(csvHeader, outputStream);
        for(final LibraryItem<?> item : library.getItems())
            writeLine(item.getCSVRepresentation(), outputStream);
    }

    protected void checkoutBook(String title, OutputStream outputStream) throws IOException {
        checkoutItem(title, bookLibrary, "book", outputStream);
    }

    protected void checkoutMovie(String title, OutputStream outputStream) throws IOException {
        checkoutItem(title, movieLibrary, "movie", outputStream);
    }

    private void checkoutItem(String title, Library library, String itemName, OutputStream outputStream) throws IOException {
        try {
            library.checkoutItemByTitle(title, customer);
            writeLine("Thank you! Enjoy the " + itemName + ".", outputStream);
        } catch (LibraryItemNotFoundException e) {
            writeLine("That " + itemName + " is not available.", outputStream);
        } catch (LibraryItemNotAvailableException e) {
            writeLine("That " + itemName + " is not available.", outputStream);
        }
    }

    protected void returnBook(String title, OutputStream outputStream) throws IOException {
        returnItem(title, bookLibrary, "book", outputStream);
    }

    protected void returnMovie(String title, OutputStream outputStream) throws IOException {
        returnItem(title, movieLibrary, "movie", outputStream);
    }

    private void returnItem(String title, Library library, String itemName, OutputStream outputStream) throws IOException {
        try {
            library.returnItemByTitle(title);
            writeLine("Thank you for returning the " + itemName + ".", outputStream);
        } catch (LibraryItemNotCheckedOutException e) {
            writeLine("That is not a valid " + itemName + " to return.", outputStream);
        } catch (LibraryItemNotFoundException e) {
            writeLine("That is not a valid " + itemName + " to return.", outputStream);
        }
    }

    protected static void quit(OutputStream outputStream) throws BibliotecaAppQuitException, IOException {
        writeLine("Thank you for using Biblioteca App!", outputStream);
        throw new BibliotecaAppQuitException();
    }

    private static void writeLine(String text, OutputStream outputStream) throws IOException {
        outputStream.write((text + "\n").getBytes());
    }

    public static void mLain(String[] args) throws IOException {
        final BibliotecaApp app = new BibliotecaApp(new Scanner(System.in), System.out);
        try {
            app.run();
        } catch (BibliotecaAppQuitException e) {
            System.exit(0);
        }
    }

}
