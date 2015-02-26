package com.twu.biblioteca;

import com.twu.biblioteca.helper.Menu;
import com.twu.biblioteca.helper.Option;
import com.twu.biblioteca.domain.Book;
import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.domain.LibraryItem;
import com.twu.biblioteca.domain.Movie;
import com.twu.biblioteca.exceptions.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public final class BibliotecaApp {

    private final Scanner scanner;
    private final OutputStream outputStream;
    private final Menu<BibliotecaApp> loggedInMenu;
    private final Menu<BibliotecaApp> loggedOutMenu;

    private Customer customer;

    private Map<String, Customer> customers = new HashMap<>();

    BibliotecaApp(Scanner scanner, OutputStream outputStream, Set<Customer> customers, Library<?>... libraries) {
        if (scanner == null) throw new IllegalArgumentException("scanner cannot be null");
        if (outputStream == null) throw new IllegalArgumentException("output stream cannot be null");
        this.scanner = scanner;
        this.outputStream = outputStream;

        for(final Customer customer : customers)
            this.customers.put(customer.getLibraryNumber(), customer);

        final List<Option<BibliotecaApp>> loggedInOptions = new ArrayList<>();
        final List<Option<BibliotecaApp>> loggedOutOptions = new ArrayList<>();
        loggedOutOptions.add(new LoginOption());
        for(Library<? extends LibraryItem> library : libraries) {
            final Option<BibliotecaApp> listOption = new ListOption(library);
            loggedOutOptions.add(listOption);
            loggedInOptions.add(listOption);
            loggedInOptions.add(new CheckoutOption(library));
            loggedInOptions.add(new ReturnOption(library));
        }
        final Option<BibliotecaApp> quitOption = new QuitOption();
        loggedInOptions.add(new LogoutOption());
        loggedInOptions.add(quitOption);
        loggedOutOptions.add(quitOption);

        loggedInMenu = new Menu<>(this, loggedInOptions);
        loggedOutMenu = new Menu<>(this, loggedOutOptions);
    }

    void run() throws Exception {
        displayWelcomeMessage();
        displayMenuOptions();
        while(scanner.hasNextLine())
            selectMenuOption(scanner.nextLine());
    }

    boolean customerLoggedIn() {
        return customer != null;
    }

    void setCustomer(Customer customer, String password) throws InvalidCredentialsException {
        if(customer == null) throw new InvalidCredentialsException();
        customer.verifyPassword(password);
        this.customer = customer;
    }

    void login(String libraryNumber, String password) throws InvalidCredentialsException, IOException {
        setCustomer(customers.get(libraryNumber), password);
        writeLine("Login Successful!");
        displayMenuOptions();
    }

    void removeCustomer() throws CustomerRequiredException {
        if(!customerLoggedIn()) throw new CustomerRequiredException();
        customer = null;
    }

    void logout() throws IOException, CustomerRequiredException {
        removeCustomer();
        writeLine("Logout Successful!");
        displayMenuOptions();
    }

    void displayWelcomeMessage() throws IOException {
        writeLine("Welcome to Biblioteca!");
    }

    void displayMenuOptions() throws IOException {
        writeLine("Please use one of the following options:");
        for(final Option<?> option : getMenu().getOptions())
            writeLine(option.getDisplay());
    }

    Menu<BibliotecaApp> getMenu() {
        return customerLoggedIn() ? loggedInMenu : loggedOutMenu;
    }

    void selectMenuOption(String option) throws Exception {
        try {
            getMenu().executeCommand(option);
        } catch (CommandNotFoundException e) {
            writeLine("Select a valid option!");
        }
    }

    <T extends LibraryItem> void listItems(Library<T> library) throws IOException {
        final List<T> items = library.getItems();
        writeLine(items.get(0).getCSVHeaders());
        for (T item : items)
            writeLine(item.getCSVRepresentation());
    }

    void checkoutItem(String title, Library library) throws IOException, CustomerRequiredException {
        final String itemName = library.getItemsName().toLowerCase();
        try {
            library.checkoutItemByTitle(title, customer);
            writeLine("Thank you! Enjoy the " + itemName + ".");
        } catch (LibraryItemNotFoundException | LibraryItemNotAvailableException e) {
            writeLine("That " + itemName + " is not available.");
        }
    }

    void returnItem(String title, Library library) throws IOException {
        final String itemName = library.getItemsName().toLowerCase();
        try {
            library.returnItemByTitle(title);
            writeLine("Thank you for returning the " + itemName + ".");
        } catch (LibraryItemNotCheckedOutException | LibraryItemNotFoundException e) {
            writeLine("That is not a valid " + itemName + " to return.");
        }
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
        final BibliotecaApp app =
                new BibliotecaApp(new Scanner(System.in), System.out, Customer.getCustomers(), bookLibrary, movieLibrary);
        try {
            app.run();
        } catch (BibliotecaAppQuitException e) {
            System.exit(0);
        }
    }

    private static class ListOption extends Option<BibliotecaApp> {
        private final Library<?> library;
        private ListOption(final Library<?> library) {
            super("List " + library.getItemsName() + "s", false);
            this.library = library;
        }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.listItems(library);
        }
    }

    private static class CheckoutOption extends Option<BibliotecaApp> {
        private final Library<?> library;
        private CheckoutOption(final Library<?> library) {
            super("Checkout " + library.getItemsName(), "<Title>", true);
            this.library = library;
        }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.checkoutItem(arg, library);
        }
    }

    private static class ReturnOption extends Option<BibliotecaApp> {
        private final Library<?> library;
        private ReturnOption(final Library<?> library) {
            super("Return " + library.getItemsName(), "<Title>", true);
            this.library = library;
        }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.returnItem(arg, library);
        }
    }

    private static class QuitOption extends Option<BibliotecaApp> {
        private QuitOption() { super("Quit", false); }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.quit();
        }
    }

    private static class LoginOption extends Option<BibliotecaApp> {
        private LoginOption() { super("Login", "<Library Number> <Password>", false); }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.quit();
        }
    }

    private static class LogoutOption extends Option<BibliotecaApp> {
        private LogoutOption() { super("Logout", true); }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.quit();
        }
    }

}
