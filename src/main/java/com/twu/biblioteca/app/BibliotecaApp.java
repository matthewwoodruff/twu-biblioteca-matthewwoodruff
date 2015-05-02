package com.twu.biblioteca.app;

import com.twu.biblioteca.domain.Book;
import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.domain.LibraryItem;
import com.twu.biblioteca.domain.Movie;
import com.twu.biblioteca.exceptions.*;
import com.twu.biblioteca.helper.Menu;
import com.twu.biblioteca.helper.Option;
import com.twu.biblioteca.app.BibliotecaAppMenuOption.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class BibliotecaApp {

    private final Scanner scanner;
    private final OutputStream outputStream;
    private final Menu<BibliotecaApp> menu;

    private final SecurityContext securityContext;

    BibliotecaApp(Scanner scanner, OutputStream outputStream, SecurityContext securityContext, Library<?>... libraries) {
        if (scanner == null) throw new IllegalArgumentException("scanner cannot be null");
        if (outputStream == null) throw new IllegalArgumentException("output stream cannot be null");
        if (securityContext == null) throw new IllegalArgumentException("securityContext cannot be null");
        this.scanner = scanner;
        this.outputStream = outputStream;
        this.securityContext = securityContext;

        final List<Option<BibliotecaApp>> options = new ArrayList<>();
        options.add(new BibliotecaAppMenuOption.LoginOption());
        for(Library<?> library : libraries) {
            options.add(new ListOption(library));
            options.add(new CheckoutOption(library));
            options.add(new ReturnOption(library));
        }
        options.add(new MyDetailsOption());
        options.add(new LogoutOption());
        options.add(new QuitOption());

        menu = new Menu<>(this, securityContext, options);
    }

    void run() throws Exception {
        displayWelcomeMessage();
        displayMenuOptions();
        while(scanner.hasNextLine())
            selectMenuOption(scanner.nextLine());
    }

    void login(String libraryNumber, String password) throws InvalidCredentialsException, IOException {
        securityContext.login(libraryNumber, password);
        writeLine("Login Successful!");
        displayMenuOptions();
    }

    void logout() throws IOException, CustomerRequiredException {
        securityContext.logout();
        writeLine("Logout Successful!");
        displayMenuOptions();
    }

    void displayWelcomeMessage() throws IOException {
        writeLine("Welcome to Biblioteca!");
    }

    void displayMenuOptions() throws IOException {
        writeLine("Please use one of the following options:");
        for(final Option<?> option : menu.getOptions())
            writeLine(option.getDisplay());
    }

    void selectMenuOption(String option) throws Exception {
        try {
            menu.executeCommand(option);
        } catch (CommandNotFoundException e) {
            writeLine("Select a valid option!");
        } catch (CustomerRequiredException e) {
            writeLine("You must be logged in to perform that task.");
        } catch (InvalidCredentialsException e) {
            writeLine("Login Failed! Please try again.");
        }
    }

    <T extends LibraryItem> void listItems(Library<T> library) throws IOException {
        final List<T> items = library.getItems();
        writeLine(items.get(0).getCSVHeaders());
        for (T item : items)
            writeLine(item.getCSVRepresentation());
    }

    void checkoutItem(String title, Library library) throws IOException, CustomerRequiredException {
        try {
            library.checkoutItemByTitle(title, securityContext.getLoggedInCustomer());
            writeLine("Thank you! Enjoy the " + library.getItemsNameLowercase() + ".");
        } catch (LibraryItemNotFoundException | LibraryItemNotAvailableException e) {
            writeLine("That " + library.getItemsNameLowercase() + " is not available.");
        }
    }

    void returnItem(String title, Library library) throws IOException, CustomerRequiredException {
        try {
            library.returnItemByTitle(title);
            writeLine("Thank you for returning the " + library.getItemsNameLowercase() + ".");
        } catch (LibraryItemNotCheckedOutException | LibraryItemNotFoundException e) {
            writeLine("That is not a valid " + library.getItemsNameLowercase() + " to return.");
        }
    }

    void viewMyDetails() throws CustomerRequiredException, IOException {
        writeLine(securityContext.getLoggedInCustomer().viewDetails());
    }

    void quit() throws BibliotecaAppQuitException, IOException {
        writeLine("Thank you for using Biblioteca App!");
        throw new BibliotecaAppQuitException();
    }

    private void writeLine(String text) throws IOException {
        outputStream.write((text + "\n").getBytes());
    }

    public static void main(String[] args) throws Exception {
        final Library<?> movieLibrary = new Library<>(Movie.getMovies(), Movie.class);
        final Library<?> bookLibrary = new Library<>(Book.getBooks(), Book.class);
        final SecurityContext securityContext = new SecurityContext(Customer.getCustomers());
        final BibliotecaApp app =
                new BibliotecaApp(new Scanner(System.in), System.out, securityContext, bookLibrary, movieLibrary);
        try {
            app.run();
        } catch (BibliotecaAppQuitException e) {
            System.exit(0);
        }
    }

}
