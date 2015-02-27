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
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 23/02/15.
 */
public class BibliotecaAppTests {

    private BibliotecaApp app;
    private OutputStream outputStream;
    private Library<?> movieLibrary;
    private Library<?> bookLibrary;
    private Customer customer;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        movieLibrary = new Library<>(Movie.getMovies(), Movie.class);
        bookLibrary = new Library<>(Book.getBooks(), Book.class);
        customer = Customer.getCustomers().iterator().next();
        app = new BibliotecaApp(new Scanner(""), outputStream, bookLibrary, movieLibrary);
    }

    /*
     * Constructor
     */

    @Test
    public void testBibliotecaAppConstructedWithOutputStream() {
        new BibliotecaApp(new Scanner(""), new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBibliotecaAppDisallowsNullOutputStream() {
        new BibliotecaApp(new Scanner(""), null);
    }

    @Test
    public void testBibliotecaAppConstructedWithScanner() {
        new BibliotecaApp(new Scanner(""), new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBibliotecaAppDisallowsNullScanner() {
        new BibliotecaApp(null, new ByteArrayOutputStream());
    }

    /*
     * Welcome message
     */

    @Test
    public void testDisplayWelcomeMessage() throws IOException {
        app.displayWelcomeMessage();

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Welcome to Biblioteca!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    /*
     * Customer Selecting Menu Options
     */

    @Test
    public void testCustomerSelectsLoginOption() throws Exception {
        app.selectMenuOption("Login: 123-4567 Password1");
        assertThatLoginSuccessMessageAndMainMenuIsDisplayed();
    }

    @Test
    public void testCustomerSelectsLoginOptionWithIncorrectPassword() throws Exception {
        app.selectMenuOption("Login: 123-4567 Password2");
        assertThatLoginFailedMessageIsDisplayed();
    }

    @Test
    public void testCustomerSelectsLoginOptionWithIncorrectUsername() throws Exception {
        app.selectMenuOption("Login: 234-4567 Password1");
        assertThatLoginFailedMessageIsDisplayed();
    }

    @Test
    public void testCustomerSelectsLogoutOptionWhilstLoggedIn() throws Exception {
        setCustomer();
        app.selectMenuOption("Logout");
        assertThatLogoutMessageIsDisplayed();
    }

    @Test
    public void testCustomerSelectsLogoutOptionWhilstNotLoggedIn() throws Exception {
        app.selectMenuOption("Logout");
        assertThatCustomerSeesAccessDeniedMessage();
    }

    @Test
    public void testCustomerSelectsMyDetailsOptionWhilstLoggedIn() throws Exception {
        setCustomer();
        app.selectMenuOption("My Details");
        assertThatCustomersDetailsAreDisplayed();
    }

    @Test
    public void testCustomerSelectsMyDetailsOptionWhilstNotLoggedIn() throws Exception {
        app.selectMenuOption("My Details");
        assertThatCustomerSeesAccessDeniedMessage();
    }

    @Test
    public void testCustomerSelectsCheckOutItemOptionSuccessfully() throws Exception {
        setCustomer();
        app.selectMenuOption("Checkout Book: Great Expectations");
        assertThatCustomerSeesSuccessfulCheckoutBookMessage();
    }

    @Test
    public void testCustomerAttemptsToCheckOutAnItemWhilstNotLoggedIn() throws Exception {
        app.selectMenuOption("Checkout Book: Great Expectations");
        assertThatCustomerSeesAccessDeniedMessage();
    }

    @Test
    public void testCustomerSelectsListBooksOption() throws Exception {
        app.selectMenuOption("List Books");
        assertThatBookListIsDisplayedWithAllBooks();
    }

    @Test
    public void testInvalidMenuOptionMessage() throws Exception {
        app.selectMenuOption("Invalid Option");
        assertThatCustomerSeesUnrecognisedOptionMessage();
    }

    @Test
    public void testCustomerSelectsReturnItemOptionSuccessfully() throws Exception {
        setCustomer();
        checkoutGreatExpectations();
        app.selectMenuOption("Return Book: Great Expectations");
        assertThatCustomerSeesReturnBookSuccessMessage();
    }

    @Test
    public void testCustomerAttemptsToReturnAnItemWhilstNotLoggedIn() throws Exception {
        app.selectMenuOption("Return Book: Great Expectations");
        assertThatCustomerSeesAccessDeniedMessage();
    }

    @Test(expected = BibliotecaAppQuitException.class)
    public void testUserSelectsQuitOption() throws Exception {
        app.selectMenuOption("Quit");
    }

    /*
     * Menu Display
     */

    @Test
    public void testMenuDisplaysCorrectlyWhenLoggedOut() throws IOException, InvalidCredentialsException {
        app.displayMenuOptions();

        final Scanner scanner = getOutputScanner();
        assertThatLoginMenuIsDisplayed(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testMenuDisplaysCorrectlyAfterLoggingIn() throws IOException, InvalidCredentialsException {
        app.login("123-4567", "Password1");
        assertThatLoginSuccessMessageAndMainMenuIsDisplayed();
    }

    @Test
    public void testMenuDisplaysCorrectlyWhenLoggedIn() throws IOException, InvalidCredentialsException {
        setCustomer();
        app.displayMenuOptions();

        final Scanner scanner = getOutputScanner();
        assertThatMainMenuIsDisplayed(scanner);
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

    /*
     * My Details
     */

    @Test(expected = CustomerRequiredException.class)
    public void testMyDetailsWhenNotLoggedInThrowsException() throws CustomerRequiredException, IOException {
        app.viewMyDetails();
    }

    @Test
    public void testMyDetailsCanBeDisplayedWhenLoggedIn() throws CustomerRequiredException, InvalidCredentialsException, IOException {
        setCustomer();
        app.viewMyDetails();
        assertThatCustomersDetailsAreDisplayed();
    }

    /*
     * List Items
     */

    @Test
    public void testListItemsListsAllBooks() throws IOException {
        app.listItems(bookLibrary);
        assertThatBookListIsDisplayedWithAllBooks();
    }

    @Test
    public void testListItemsListsAllMovies() throws IOException {
        app.listItems(movieLibrary);

        final Scanner scanner = getOutputScanner();
        assertThatMovieListIsDisplayedWithAllMovies(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testNullMenuOptionMessage() throws Exception {
        app.selectMenuOption(null);
        assertThatCustomerSeesUnrecognisedOptionMessage();
    }

    @Test
    public void testCustomerCheckedOutBookDoesNotAppearInBookList() throws Exception, LibraryItemNotAvailableException {
        checkoutGreatExpectations();
        setCustomer();
        app.listItems(bookLibrary);

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Title, Author, Year"));
        assertThat(scanner.nextLine(), is("Bleak House, Charles Dickens, 1853"));
        assertThat(scanner.nextLine(), is("The Pickwick Papers, Charles Dickens, 1837"));
        assertThat(scanner.hasNext(), is(false));
    }

    /*
     * Checkout item
     */

    @Test(expected = CustomerRequiredException.class)
    public void testCheckOutItemRequiresCustomerToBeLoggedIn() throws IOException, CustomerRequiredException {
        app.checkoutItem("Great Expectations", bookLibrary);
    }

    @Test
    public void testCheckOutAnItemSuccessfully() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        setCustomer();
        app.checkoutItem("Great Expectations", bookLibrary);
        assertThatCustomerSeesSuccessfulCheckoutBookMessage();
    }

    @Test
    public void testCustomerChecksOutAnUnavailableItem() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException, CustomerRequiredException, LibraryItemNotAvailableException {
        checkoutGreatExpectations();
        setCustomer();
        app.checkoutItem("Great Expectations", bookLibrary);
        assertThatCustomerSeesBookNotAvailableMessage();
    }

    @Test
    public void testCustomerChecksOutANonExistingItem() throws IOException, CustomerRequiredException, InvalidCredentialsException {
        setCustomer();
        app.checkoutItem("Hard Times", bookLibrary);
        assertThatCustomerSeesBookNotAvailableMessage();
    }

    /*
     * Return Item
     */

    @Test
    public void testCustomerReturnsAnItemSuccessfully() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException, CustomerRequiredException, LibraryItemNotAvailableException {
        checkoutGreatExpectations();
        setCustomer();
        app.returnItem("Great Expectations", bookLibrary);
        assertThatCustomerSeesReturnBookSuccessMessage();
    }

    @Test
    public void testCustomerReturnsAnItemThatHasntBeenCheckedOut() throws IOException, CustomerRequiredException, InvalidCredentialsException {
        setCustomer();
        app.returnItem("Great Expectations", bookLibrary);
        assertThatCustomerSeesInvalidBookReturnMessage();
    }

    @Test
    public void testCustomerReturnsAnItemThatDoesntExist() throws IOException, CustomerRequiredException, InvalidCredentialsException {
        setCustomer();
        app.returnItem("Hard Times", bookLibrary);
        assertThatCustomerSeesInvalidBookReturnMessage();
    }

    @Test
    public void testReturnedBookAppearsInItemList() throws Exception {
        checkoutGreatExpectations();
        returnGreatExpectations();
        setCustomer();
        app.listItems(bookLibrary);
        assertThatBookListIsDisplayedWithAllBooks();
    }

    /*
     * QUIT
     */

    @Test(expected = BibliotecaAppQuitException.class)
    public void testQuitThrowsException() throws BibliotecaAppQuitException, IOException {
        app.quit();
    }

    @Test
    public void testQuitShowsLeavingMessage() throws BibliotecaAppQuitException, IOException {
        try {
            app.quit();
        } catch (BibliotecaAppQuitException e) {}

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for using Biblioteca App!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    /*
     * Run
     */

    @Test
    public void testCustomerHasSuccessfulJourneyCheckingABookOutAndIn() throws Exception {
        final List<String> commands = Arrays.asList(
                "Login: 123-4567 Password1",
                "List Books",
                "Checkout Book: Great Expectations",
                "Return Book: Great Expectations",
                "Quit");

        final Scanner inputScanner = new Scanner(StringUtils.join(commands, "\n"));
        app = new BibliotecaApp(inputScanner, outputStream, bookLibrary, movieLibrary);

        try {
            app.run();
        } catch (BibliotecaAppQuitException e) {}

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Welcome to Biblioteca!"));
        assertThatLoginMenuIsDisplayed(scanner);
        assertThatLoginSuccessMessageAndMainMenuIsDisplayed(scanner);
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
        assertThat(scanner.nextLine(), is("My Details"));
        assertThat(scanner.nextLine(), is("Logout"));
        assertThat(scanner.nextLine(), is("Quit"));
    }

    private void assertThatBookListIsDisplayedWithAllBooks() {
        final Scanner scanner = getOutputScanner();
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.hasNextLine(), is(false));
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

    private Scanner getOutputScanner() {
        return getOutputScanner(outputStream);
    }

    private Scanner getOutputScanner(OutputStream outputStream) {
        return new Scanner(outputStream.toString());
    }

    private void returnGreatExpectations() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItemByTitle("Great Expectations");
    }

    private void checkoutGreatExpectations() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        bookLibrary.checkoutItemByTitle("Great Expectations", customer);
    }

    private void assertThatCustomerSeesInvalidBookReturnMessage() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid book to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void assertThatCustomerSeesBookNotAvailableMessage() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("That book is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void assertThatCustomerSeesUnrecognisedOptionMessage() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Select a valid option!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void assertThatCustomerSeesSuccessfulCheckoutBookMessage() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void assertThatCustomerSeesAccessDeniedMessage() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("You must be logged in to perform that task."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void assertThatCustomerSeesReturnBookSuccessMessage() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void assertThatLoginSuccessMessageAndMainMenuIsDisplayed() {
        final Scanner scanner = getOutputScanner();
        assertThatLoginSuccessMessageAndMainMenuIsDisplayed(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void assertThatLoginSuccessMessageAndMainMenuIsDisplayed(Scanner scanner) {
        assertThat(scanner.nextLine(), is("Login Successful!"));
        assertThatMainMenuIsDisplayed(scanner);
    }

    private void assertThatLogoutMessageIsDisplayed() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Logout Successful!"));
        assertThat(app.getSecurityContext().isCustomerLoggedIn(), is(false));
    }

    private void assertThatCustomersDetailsAreDisplayed() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Name: Charles Dickens"));
        assertThat(scanner.nextLine(), is("Email Address: charles@example.com"));
        assertThat(scanner.nextLine(), is("Phone: 07712345678"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void setCustomer() throws InvalidCredentialsException, IOException {
        app.getSecurityContext().setCustomer(customer, "Password1");
    }

    private void assertThatLoginFailedMessageIsDisplayed() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Login Failed! Please try again."));
        assertThat(scanner.hasNextLine(), is(false));
    }

}
