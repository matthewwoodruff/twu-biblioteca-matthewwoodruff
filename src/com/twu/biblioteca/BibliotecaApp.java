package com.twu.biblioteca;

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

    public BibliotecaApp(Scanner scanner, OutputStream outputStream, Set<Customer> customers, Library<?>... libraries) {
        if (scanner == null) throw new IllegalArgumentException("scanner cannot be null");
        if (outputStream == null) throw new IllegalArgumentException("output stream cannot be null");
        this.scanner = scanner;
        this.outputStream = outputStream;

        for(final Customer customer : customers)
            this.customers.put(customer.getLibraryNumber(), customer);

        final List<MenuOption<BibliotecaApp>> loggedInOptions = new ArrayList<>();
        final List<MenuOption<BibliotecaApp>> loggedOutOptions = new ArrayList<>();
        loggedOutOptions.add(new LoginOption());
        for(Library<? extends LibraryItem> library : libraries) {
            final MenuOption<BibliotecaApp> listOption = new ListOption(library);
            loggedOutOptions.add(listOption);
            loggedInOptions.add(listOption);
            loggedInOptions.add(new CheckoutOption(library));
            loggedInOptions.add(new ReturnOption(library));
        }
        final MenuOption<BibliotecaApp> quitOption = new QuitOption();
        loggedInOptions.add(new LogoutOption());
        loggedInOptions.add(quitOption);
        loggedOutOptions.add(quitOption);

        loggedInMenu = new Menu<>(this, loggedInOptions);
        loggedOutMenu = new Menu<>(this, loggedOutOptions);
    }

    public void run() throws Exception {
        displayWelcomeMessage(outputStream);
        displayMenuOptions(outputStream);
        while(scanner.hasNextLine())
            selectMenuOption(scanner.nextLine());
    }

    protected boolean customerLoggedIn() {
        return customer != null;
    }

    protected void login(String libraryNumber, String password) throws InvalidCredentialsException {
        final Customer customer = customers.get(libraryNumber);
        if(customer == null) throw new InvalidCredentialsException();
        customer.verifyPassword(password);
        this.customer = customer;
    }

    protected void displayWelcomeMessage(OutputStream outputStream) throws IOException {
        writeLine("Welcome to Biblioteca!", outputStream);
    }

    protected void displayMenuOptions(OutputStream outputStream) throws IOException {
        writeLine("Please use one of the following options:", outputStream);
        for(final MenuOption<?> option : getMenu().getOptions())
            writeLine(option.getDisplay(), outputStream);
    }

    protected Menu<BibliotecaApp> getMenu() {
        return customerLoggedIn() ? loggedInMenu : loggedOutMenu;
    }

    protected void selectMenuOption(String option) throws Exception {
        try {
            loggedInMenu.executeCommand(option);
        } catch (CommandNotFoundException e) {
            writeLine("Select a valid option!", outputStream);
        }
    }

    private void listItems(Library<?> library) throws IOException {
        listItems(library, outputStream);
    }

    protected <T extends LibraryItem> void listItems(Library<T> library, OutputStream outputStream) throws IOException {
        final List<T> items = library.getItems();
        writeLine(items.get(0).getCSVHeaders(), outputStream);
        for (T item : items)
            writeLine(item.getCSVRepresentation(), outputStream);
    }

    private void checkoutItem(String title, Library library) throws IOException {
        checkoutItem(title, library, outputStream);
    }

    protected void checkoutItem(String title, Library library, OutputStream outputStream) throws IOException {
        final String itemName = library.getItemsName().toLowerCase();
        try {
            library.checkoutItemByTitle(title, customer);
            writeLine("Thank you! Enjoy the " + itemName + ".", outputStream);
        } catch (LibraryItemNotFoundException | LibraryItemNotAvailableException e) {
            writeLine("That " + itemName + " is not available.", outputStream);
        }
    }

    private void returnItem(String title, Library library) throws IOException {
        returnItem(title, library, outputStream);
    }

    protected void returnItem(String title, Library library, OutputStream outputStream) throws IOException {
        final String itemName = library.getItemsName().toLowerCase();
        try {
            library.returnItemByTitle(title);
            writeLine("Thank you for returning the " + itemName + ".", outputStream);
        } catch (LibraryItemNotCheckedOutException | LibraryItemNotFoundException e) {
            writeLine("That is not a valid " + itemName + " to return.", outputStream);
        }
    }

    private void quit() throws IOException, BibliotecaAppQuitException {
        quit(outputStream);
    }

    protected static void quit(OutputStream outputStream) throws BibliotecaAppQuitException, IOException {
        writeLine("Thank you for using Biblioteca App!", outputStream);
        throw new BibliotecaAppQuitException();
    }

    private static void writeLine(String text, OutputStream outputStream) throws IOException {
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

    private static class ListOption extends MenuOption<BibliotecaApp> {
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

    private static class CheckoutOption extends MenuOption<BibliotecaApp> {
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

    private static class ReturnOption extends MenuOption<BibliotecaApp> {
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

    private static class QuitOption extends MenuOption<BibliotecaApp> {
        private QuitOption() { super("Quit", false); }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.quit();
        }
    }

    private static class LoginOption extends MenuOption<BibliotecaApp> {
        private LoginOption() { super("Login", "<Library Number> <Password>", false); }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.quit();
        }
    }

    private static class LogoutOption extends MenuOption<BibliotecaApp> {
        private LogoutOption() { super("Logout", true); }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.quit();
        }
    }

}
