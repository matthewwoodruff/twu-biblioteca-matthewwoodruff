package com.twu.biblioteca;

import com.sun.deploy.util.StringUtils;
import com.twu.biblioteca.domain.Book;
import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.domain.Movie;
import com.twu.biblioteca.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 23/02/15.
 */
public class BibliotecaAppTests {

    private BibliotecaApp app;
    private OutputStream outputStream;
    private Scanner inputScanner;
    private Library<?> movieLibrary;
    private Library<?> bookLibrary;
    private Set<Customer> customers;
    private Customer customer;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        inputScanner = new Scanner("");
        movieLibrary = new Library<>(Movie.getMovies(), Movie.class);
        bookLibrary = new Library<>(Book.getBooks(), Book.class);
        customers = Customer.getCustomers();
        customer = customers.iterator().next();
        app = new BibliotecaApp(inputScanner, outputStream, customers, bookLibrary, movieLibrary);
    }

    @Test
    public void testBibliotecaAppConstructedWithOutputStream() {
        new BibliotecaApp(new Scanner(""), new ByteArrayOutputStream(), customers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBibliotecaAppDisallowsNullOutputStream() {
        new BibliotecaApp(new Scanner(""), null, customers);
    }

    @Test
    public void testBibliotecaAppConstructedWithScanner() {
        new BibliotecaApp(new Scanner(""), new ByteArrayOutputStream(), customers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBibliotecaAppDisallowsNullScanner() {
        new BibliotecaApp(null, new ByteArrayOutputStream(), customers);
    }

    @Test
    public void testDisplayWelcomeMessage() throws IOException {
        app.displayWelcomeMessage();

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Welcome to Biblioteca!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCanDetermineIfCustomerLoggedIn() {
        assertThat(app.customerLoggedIn(), is(false));
    }

    @Test
    public void testCanLogIn() throws InvalidCredentialsException, IOException {
        setCustomer();
        assertThat(app.customerLoggedIn(), is(true));
    }

    @Test
    public void testCanLoginAndLogout() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        setCustomer();
        app.logout();
        assertThat(app.customerLoggedIn(), is(false));
    }

    @Test(expected = CustomerRequiredException.class)
    public void testLogoutThrowsExceptionIfNotLoggedIn() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        app.logout();
    }

    @Test
    public void testLogoutMessage() throws InvalidCredentialsException, IOException, CustomerRequiredException {
        setCustomer();
        app.logout();

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Logout Successful!"));
        assertThat(app.customerLoggedIn(), is(false));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginWillThrowExceptionIfLibraryNumberNotKnown() throws InvalidCredentialsException, IOException {
        app.login("234-5678", "Password1");
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginWillThrowExceptionIfIncorrectPasswordEntered() throws InvalidCredentialsException, IOException {
        app.login("123-4567", "Password2");
    }

    @Test
    public void testMenuDisplaysCorrectlyWhenLoggedOut() throws IOException, InvalidCredentialsException {
        app.displayMenuOptions();

        final Scanner scanner = getOutputScanner();
        assertThatLoginMenuIsDisplayed(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testMenuDisplaysCorrectlyAfterLoggingOut() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        setCustomer();
        app.logout();

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Logout Successful!"));
        assertThatLoginMenuIsDisplayed(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testMenuDisplaysCorrectlyWhenLoggedIn() throws IOException, InvalidCredentialsException {
        app.login("123-4567", "Password1");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Login Successful!"));
        assertThatMainMenuIsDisplayed(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }


    @Test
    public void testListBooksListsAllBooks() throws IOException {
        app.listItems(bookLibrary);

        final Scanner scanner = getOutputScanner();
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testInvalidMenuOptionMessage() throws Exception {
        app.selectMenuOption("Invalid Option");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Select a valid option!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testNullMenuOptionMessage() throws Exception {
        app.selectMenuOption(null);

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Select a valid option!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsListBooksOption() throws Exception {
        app.selectMenuOption("List Books");

        final Scanner scanner = getOutputScanner();
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test(expected = BibliotecaAppQuitException.class)
    public void testQuitThrowsException() throws BibliotecaAppQuitException, IOException {
        app.quit();
    }

    @Test
    public void testQuitShowsLeavingMessage() throws BibliotecaAppQuitException, IOException {
        try {
            app.quit();
            fail();
        } catch (BibliotecaAppQuitException e) {}

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for using Biblioteca App!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test(expected = BibliotecaAppQuitException.class)
    public void testUserSelectsQuitOption() throws Exception {
        app.selectMenuOption("Quit");
    }

    @Test(expected = CustomerRequiredException.class)
    public void testCheckOutItemRequiresCustomerToBeLoggedIn() throws IOException, CustomerRequiredException {
        app.checkoutItem("Great Expectations", bookLibrary);
    }

    @Test
    public void testCustomerChecksOutABookSuccessfully() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        setCustomer();
        app.checkoutItem("Great Expectations", bookLibrary);

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutAnUnavailableBook() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException, CustomerRequiredException, LibraryItemNotAvailableException {
        checkoutGreatExpectations();
        setCustomer();
        app.checkoutItem("Great Expectations", bookLibrary);

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("That book is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutANonExistingBook() throws IOException, CustomerRequiredException {
        app.checkoutItem("Hard Times", bookLibrary);

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("That book is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerCheckedOutBookDoesNotAppearInBookList() throws Exception, LibraryItemNotAvailableException {
        checkoutGreatExpectations();
        setCustomer();
        app.selectMenuOption("List Books");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Title, Author, Year"));
        assertThat(scanner.nextLine(), is("Bleak House, Charles Dickens, 1853"));
        assertThat(scanner.nextLine(), is("The Pickwick Papers, Charles Dickens, 1837"));
        assertThat(scanner.hasNext(), is(false));
    }

    @Test
    public void testCustomerSelectsCheckOutBookOptionSuccessfully() throws Exception {
        setCustomer();
        app.selectMenuOption("Checkout Book: Great Expectations");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

//    @Test(expected = CustomerRequiredException.class)
//    public void testReturnItemRequiresCustomerToBeLoggedIn() throws IOException, CustomerRequiredException {
//        app.checkoutItem("Great Expectations", bookLibrary, testOutputStream);
//    }

    @Test
    public void testCustomerReturnsABookSuccessfully() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException, CustomerRequiredException, LibraryItemNotAvailableException {
        checkoutGreatExpectations();
        setCustomer();
        app.returnItem("Great Expectations", bookLibrary);

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void checkoutGreatExpectations() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        bookLibrary.checkoutItemByTitle("Great Expectations", customer);
    }

    @Test
    public void testCustomerReturnsABookThatHasntBeenCheckedOut() throws IOException {
        app.returnItem("Great Expectations", bookLibrary);

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid book to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsABookThatDoesntExist() throws IOException {
        app.returnItem("Hard Times", bookLibrary);

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid book to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testReturnedBookAppearsInBookList() throws Exception {
        checkoutGreatExpectations();
        returnGreatExpectations();
        setCustomer();
        app.selectMenuOption("List Books");

        final Scanner scanner = getOutputScanner();
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsReturnBookOptionSuccessfully() throws Exception {
        setCustomer();
        checkoutGreatExpectations();
        app.selectMenuOption("Return Book: Great Expectations");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerHasSuccessfulJourneyCheckingABookOutAndIn() throws Exception {
        final List<String> commands = Arrays.asList(
                "List Books",
                "Checkout Book: Great Expectations",
                "Return Book: Great Expectations",
                "Quit");

        inputScanner = new Scanner(StringUtils.join(commands, "\n"));
        app = new BibliotecaApp(inputScanner, outputStream, customers, bookLibrary, movieLibrary);
        setCustomer();
        try {
            app.run();
        } catch (BibliotecaAppQuitException e) {}

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Welcome to Biblioteca!"));
        assertThatMainMenuIsDisplayed(scanner);
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the book."));
        assertThat(scanner.nextLine(), is("Thank you for returning the book."));
        assertThat(scanner.nextLine(), is("Thank you for using Biblioteca App!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void assertThatLoginMenuIsDisplayed(Scanner scanner) {
        assertThat(scanner.nextLine(), is("Please use one of the following options:"));
        assertThat(scanner.nextLine(), is("Login: <Library Number> <Password>"));
        assertThat(scanner.nextLine(), is("List Books"));
        assertThat(scanner.nextLine(), is("List Movies"));
        assertThat(scanner.nextLine(), is("Quit"));
    }

    private void assertThatMainMenuIsDisplayed(Scanner scanner) {
        assertThat(scanner.nextLine(), is("Please use one of the following options:"));
        assertThat(scanner.nextLine(), is("List Books"));
        assertThat(scanner.nextLine(), is("Checkout Book: <Title>"));
        assertThat(scanner.nextLine(), is("Return Book: <Title>"));
        assertThat(scanner.nextLine(), is("List Movies"));
        assertThat(scanner.nextLine(), is("Checkout Movie: <Title>"));
        assertThat(scanner.nextLine(), is("Return Movie: <Title>"));
        assertThat(scanner.nextLine(), is("Logout"));
        assertThat(scanner.nextLine(), is("Quit"));
    }

    private void assertThatBookListIsDisplayedWithAllBooks(Scanner scanner) {
        assertThat(scanner.nextLine(), is("Title, Author, Year"));
        assertThat(scanner.nextLine(), is("Bleak House, Charles Dickens, 1853"));
        assertThat(scanner.nextLine(), is("Great Expectations, Charles Dickens, 1860"));
        assertThat(scanner.nextLine(), is("The Pickwick Papers, Charles Dickens, 1837"));
    }

    private void assertThatMovieListIsDisplayedWithAllMovies(Scanner scanner) {
        assertThat(scanner.nextLine(), is("Title, Director, Year, Rating"));
        assertThat(scanner.nextLine(), is("Kill Bill, Quentin Tarantino, 2003, Unrated"));
        assertThat(scanner.nextLine(), is("Pulp Fiction, Quentin Tarantino, 1994, 9"));
        assertThat(scanner.nextLine(), is("Reservoir Dogs, Quentin Tarantino, 1992, 8"));
    }

    private void setCustomer() throws InvalidCredentialsException, IOException {
        app.setCustomer(customer, "Password1");
    }

    private void removeCustomer() throws InvalidCredentialsException, IOException, CustomerRequiredException {
        app.removeCustomer();
    }

    private Scanner getOutputScanner() {
        return getOutputScanner(outputStream);
    }

    private Scanner getOutputScanner(OutputStream outputStream) {
        return new Scanner(outputStream.toString());
    }

    private void returnGreatExpectations() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItemByTitle("Great Expectations");
    }

}
