package com.twu.biblioteca.app;

import com.twu.biblioteca.app.BibliotecaApp;
import com.twu.biblioteca.app.Library;
import com.twu.biblioteca.app.SecurityContext;
import com.twu.biblioteca.domain.Book;
import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.domain.Movie;
import com.twu.biblioteca.exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Matt on 23/02/15.
 */
public class BibliotecaAppTests {

    private BibliotecaApp app;
    private OutputStream outputStream;
    @Mock
    private Library<Movie> movieLibrary;
    @Mock
    private Library<Book> bookLibrary;
    @Mock
    private Customer customer;
    @Mock
    private SecurityContext securityContext;

    private List<Book> books;
    private List<Movie> movies;

    @Before
    public void setup() throws Exception {
        initMocks(this);

        final Book greatExpectations = mock(Book.class);
        when(greatExpectations.getTitle()).thenReturn("Great Expectations");
        when(greatExpectations.getCSVHeaders()).thenReturn("Title, Author, Year");
        when(greatExpectations.getCSVRepresentation()).thenReturn("Great Expectations, Charles Dickens, 1860");

        final Book pickwickPapers = mock(Book.class);
        when(pickwickPapers.getTitle()).thenReturn("The Pickwick Papers");
        when(pickwickPapers.getCSVHeaders()).thenReturn("Title, Author, Year");
        when(pickwickPapers.getCSVRepresentation()).thenReturn("The Pickwick Papers, Charles Dickens, 1837");

        books = new ArrayList<>();
        books.add(greatExpectations);
        books.add(pickwickPapers);

        when(bookLibrary.getItemsName()).thenReturn("Book");
        when(bookLibrary.getItemsNameLowercase()).thenReturn("book");
        when(bookLibrary.getItems()).thenReturn(books);

        final Movie killBill = mock(Movie.class);
        when(killBill.getTitle()).thenReturn("Kill Bill");
        when(killBill.getCSVHeaders()).thenReturn("Title, Director, Year, Rating");
        when(killBill.getCSVRepresentation()).thenReturn("Kill Bill, Quentin Tarantino, 2003, Unrated");

        final Movie pulpFiction = mock(Movie.class);
        when(pulpFiction.getTitle()).thenReturn("Pulp Fiction");
        when(pulpFiction.getCSVHeaders()).thenReturn("Title, Director, Year, Rating");
        when(pulpFiction.getCSVRepresentation()).thenReturn("Pulp Fiction, Quentin Tarantino, 1994, 9");

        movies = new ArrayList<>();
        movies.add(killBill);
        movies.add(pulpFiction);

        outputStream = new ByteArrayOutputStream();

        when(movieLibrary.getItemsName()).thenReturn("Movie");
        when(movieLibrary.getItemsNameLowercase()).thenReturn("movie");
        when(movieLibrary.getItems()).thenReturn(movies);

        when(customer.viewDetails()).thenReturn("Name: Charles Dickens\nEmail Address: charles@example.com\nPhone: 07712345678");

        app = new BibliotecaApp(new Scanner(""), outputStream, securityContext, bookLibrary, movieLibrary);
    }

    /*
     * Constructor
     */

    @Test
    public void testBibliotecaAppConstructs() {
        new BibliotecaApp(new Scanner(""), new ByteArrayOutputStream(), securityContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBibliotecaAppDisallowsNullOutputStream() {
        new BibliotecaApp(new Scanner(""), null, securityContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBibliotecaAppDisallowsNullScanner() {
        new BibliotecaApp(null, new ByteArrayOutputStream(), securityContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBibliotecaAppDisallowsNullSecurityContext() {
        new BibliotecaApp(null, new ByteArrayOutputStream(), null);
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
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                stubCustomer();
                return null;
            }
        }).when(securityContext).login("123-4567", "Password1");

        app.selectMenuOption("Login: 123-4567 Password1");
        verify(securityContext, times(1)).login("123-4567", "Password1");
        assertThatLoginSuccessMessageAndMainMenuIsDisplayed();
    }

    @Test
    public void testCustomerSelectsLoginOptionWithIncorrectCredentials() throws Exception {
        doThrow(new InvalidCredentialsException()).when(securityContext).login("123-4567", "Password2");
        app.selectMenuOption("Login: 123-4567 Password2");
        verify(securityContext, times(1)).login("123-4567", "Password2");
        assertThatLoginFailedMessageIsDisplayed();
    }

    @Test
    public void testCustomerSelectsLogoutOptionWhilstLoggedIn() throws Exception {
        stubCustomer();
        app.selectMenuOption("Logout");
        verify(securityContext, times(1)).logout();
        assertThatLogoutMessageIsDisplayed();
    }

    @Test
    public void testCustomerSelectsLogoutOptionWhilstNotLoggedIn() throws Exception {
        app.selectMenuOption("Logout");
        verify(customer, times(0)).viewDetails();
        assertThatCustomerSeesAccessDeniedMessage();
    }

    @Test
    public void testCustomerSelectsMyDetailsOptionWhilstLoggedIn() throws Exception {
        stubCustomer();
        app.selectMenuOption("My Details");
        verify(customer, times(1)).viewDetails();
        assertThatCustomersDetailsAreDisplayed();
    }

    @Test
    public void testCustomerSelectsMyDetailsOptionWhilstNotLoggedIn() throws Exception {
        app.selectMenuOption("My Details");
        verify(customer, times(0)).viewDetails();
        assertThatCustomerSeesAccessDeniedMessage();
    }

    @Test
    public void testCustomerSelectsCheckOutItemOptionSuccessfully() throws Exception {
        stubCustomer();
        app.selectMenuOption("Checkout Book: Great Expectations");
        verify(bookLibrary, times(1)).checkoutItemByTitle("Great Expectations", customer);
        assertThatCustomerSeesSuccessfulCheckoutBookMessage();
    }

    @Test
    public void testCustomerAttemptsToCheckOutAnItemWhilstNotLoggedIn() throws Exception {
        app.selectMenuOption("Checkout Book: Great Expectations");
        verify(bookLibrary, times(0)).checkoutItemByTitle("Great Expectations", customer);
        assertThatCustomerSeesAccessDeniedMessage();
    }

    @Test
    public void testCustomerSelectsListBooksOption() throws Exception {
        app.selectMenuOption("List Books");
        verify(bookLibrary, times(1)).getItems();
        assertThatBookListIsDisplayedWithAllBooks();
    }

    @Test
    public void testInvalidMenuOptionMessage() throws Exception {
        app.selectMenuOption("Invalid Option");
        assertThatCustomerSeesUnrecognisedOptionMessage();
    }

    @Test
    public void testCustomerSelectsReturnItemOptionSuccessfully() throws Exception {
        stubCustomer();
        app.selectMenuOption("Return Book: Great Expectations");
        verify(bookLibrary, times(1)).returnItemByTitle("Great Expectations");
        assertThatCustomerSeesReturnBookSuccessMessage();
    }

    @Test
    public void testCustomerAttemptsToReturnAnItemWhilstNotLoggedIn() throws Exception {
        app.selectMenuOption("Return Book: Great Expectations");
        verify(bookLibrary, times(0)).returnItemByTitle("Great Expectations");
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
    public void testMenuDisplaysCorrectlyAfterLoggingIn() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        stubCustomer();
        app.login("123-4567", "Password1");
        verify(securityContext, times(1)).login("123-4567", "Password1");
        assertThatLoginSuccessMessageAndMainMenuIsDisplayed();
    }

    @Test
    public void testMenuDisplaysCorrectlyWhenLoggedIn() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        stubCustomer();
        app.displayMenuOptions();

        final Scanner scanner = getOutputScanner();
        assertThatMainMenuIsDisplayed(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testMenuDisplaysCorrectlyAfterLoggingOut() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        app.logout();
        verify(securityContext, times(1)).logout();

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
        doThrow(new CustomerRequiredException()).when(securityContext).getLoggedInCustomer();
        app.viewMyDetails();
    }

    @Test
    public void testMyDetailsCanBeDisplayedWhenLoggedIn() throws CustomerRequiredException, InvalidCredentialsException, IOException {
        stubCustomer();
        app.viewMyDetails();
        verify(customer, times(1)).viewDetails();
        assertThatCustomersDetailsAreDisplayed();
    }

    /*
     * List Items
     */

    @Test
    public void testListItemsListsAllBooks() throws IOException {
        app.listItems(bookLibrary);
        verify(bookLibrary, times(1)).getItems();
        assertThatBookListIsDisplayedWithAllBooks();
    }

    @Test
    public void testListItemsListsAllMovies() throws IOException {
        app.listItems(movieLibrary);
        verify(movieLibrary, times(1)).getItems();

        final Scanner scanner = getOutputScanner();
        assertThatMovieListIsDisplayedWithAllMovies(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testNullMenuOptionMessage() throws Exception {
        app.selectMenuOption(null);
        assertThatCustomerSeesUnrecognisedOptionMessage();
    }

    /*
     * Checkout item
     */

    @Test(expected = CustomerRequiredException.class)
    public void testCheckOutItemRequiresCustomerToBeLoggedIn() throws IOException, CustomerRequiredException {
        when(securityContext.getLoggedInCustomer()).thenThrow(CustomerRequiredException.class);
        app.checkoutItem("Great Expectations", bookLibrary);
    }

    @Test
    public void testCheckOutAnItemSuccessfully() throws IOException, InvalidCredentialsException, CustomerRequiredException, LibraryItemNotFoundException, LibraryItemNotAvailableException {
        stubCustomer();
        app.checkoutItem("Great Expectations", bookLibrary);
        verify(bookLibrary, times(1)).checkoutItemByTitle("Great Expectations", customer);
        assertThatCustomerSeesSuccessfulCheckoutBookMessage();
    }

    @Test
    public void testCustomerChecksOutAnUnavailableItem() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException, CustomerRequiredException, LibraryItemNotAvailableException {
        stubCustomer();
        doThrow(new LibraryItemNotAvailableException()).when(bookLibrary).checkoutItemByTitle(anyString(), eq(customer));
        app.checkoutItem("Great Expectations", bookLibrary);
        verify(bookLibrary, times(1)).checkoutItemByTitle("Great Expectations", customer);
        assertThatCustomerSeesBookNotAvailableMessage();
    }

    @Test
    public void testCustomerChecksOutANonExistingItem() throws IOException, CustomerRequiredException, InvalidCredentialsException, LibraryItemNotFoundException, LibraryItemNotAvailableException {
        stubCustomer();
        doThrow(new LibraryItemNotFoundException()).when(bookLibrary).checkoutItemByTitle(eq("Hard Times"), eq(customer));
        app.checkoutItem("Hard Times", bookLibrary);
        verify(bookLibrary, times(1)).checkoutItemByTitle("Hard Times", customer);
        assertThatCustomerSeesBookNotAvailableMessage();
    }

    /*
     * Return Item
     */

    @Test
    public void testCustomerReturnsAnItemSuccessfully() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException, CustomerRequiredException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        stubCustomer();
        app.returnItem("Great Expectations", bookLibrary);
        verify(bookLibrary, times(1)).returnItemByTitle("Great Expectations");
        assertThatCustomerSeesReturnBookSuccessMessage();
    }

    @Test
    public void testCustomerReturnsAnItemThatHasntBeenCheckedOut() throws IOException, CustomerRequiredException, InvalidCredentialsException, LibraryItemNotCheckedOutException, LibraryItemNotFoundException {
        stubCustomer();
        doThrow(new LibraryItemNotCheckedOutException()).when(bookLibrary).returnItemByTitle("Great Expectations");
        app.returnItem("Great Expectations", bookLibrary);
        verify(bookLibrary, times(1)).returnItemByTitle("Great Expectations");
        assertThatCustomerSeesInvalidBookReturnMessage();
    }

    @Test
    public void testCustomerReturnsAnItemThatDoesntExist() throws IOException, CustomerRequiredException, InvalidCredentialsException, LibraryItemNotCheckedOutException, LibraryItemNotFoundException {
        stubCustomer();
        doThrow(new LibraryItemNotFoundException()).when(bookLibrary).returnItemByTitle("Hard Times");
        app.returnItem("Hard Times", bookLibrary);
        verify(bookLibrary, times(1)).returnItemByTitle("Hard Times");
        assertThatCustomerSeesInvalidBookReturnMessage();
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
     * Helpers
     */

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
        assertThat(scanner.nextLine(), is("Great Expectations, Charles Dickens, 1860"));
        assertThat(scanner.nextLine(), is("The Pickwick Papers, Charles Dickens, 1837"));
    }

    private void assertThatMovieListIsDisplayedWithAllMovies(Scanner scanner) {
        assertThat(scanner.nextLine(), is("Title, Director, Year, Rating"));
        assertThat(scanner.nextLine(), is("Kill Bill, Quentin Tarantino, 2003, Unrated"));
        assertThat(scanner.nextLine(), is("Pulp Fiction, Quentin Tarantino, 1994, 9"));
    }

    private Scanner getOutputScanner() {
        return getOutputScanner(outputStream);
    }

    private Scanner getOutputScanner(OutputStream outputStream) {
        return new Scanner(outputStream.toString());
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
    }

    private void assertThatCustomersDetailsAreDisplayed() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Name: Charles Dickens"));
        assertThat(scanner.nextLine(), is("Email Address: charles@example.com"));
        assertThat(scanner.nextLine(), is("Phone: 07712345678"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    private void stubCustomer() throws InvalidCredentialsException, IOException, CustomerRequiredException {
        when(securityContext.isCustomerLoggedIn()).thenReturn(true);
        when(securityContext.getLoggedInCustomer()).thenReturn(customer);
    }

    private void assertThatLoginFailedMessageIsDisplayed() {
        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Login Failed! Please try again."));
        assertThat(scanner.hasNextLine(), is(false));
    }

}
