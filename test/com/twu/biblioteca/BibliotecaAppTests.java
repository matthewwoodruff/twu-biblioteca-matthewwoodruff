package com.twu.biblioteca;

import com.sun.deploy.util.StringUtils;
import com.twu.biblioteca.exceptions.BibliotecaAppQuitException;
import com.twu.biblioteca.exceptions.InvalidCredentialsException;
import com.twu.biblioteca.exceptions.LibraryItemNotFoundException;
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
    private OutputStream testOutputStream;
    private Scanner inputScanner;
    private Library<?> movieLibrary;
    private Library<?> bookLibrary;
    private Set<Customer> customers;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        testOutputStream = new ByteArrayOutputStream();
        inputScanner = new Scanner("");
        movieLibrary = new Library<>(Movie.getMovies(), Movie.class);
        bookLibrary = new Library<>(Book.getBooks(), Book.class);
        customers = Customer.getCustomers();
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
        app.displayWelcomeMessage(testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Welcome to Biblioteca!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCanDetermineIfCustomerLoggedIn() {
        assertThat(app.customerLoggedIn(), is(false));
    }

    @Test
    public void testCustomerCanLogIn() throws InvalidCredentialsException {
        login();
        assertThat(app.customerLoggedIn(), is(true));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginWithThrowExceptionIfLibraryNumberNotKnown() throws InvalidCredentialsException {
        app.login("234-5678", "Password1");
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginWithThrowExceptionIfIncorrectPasswordEntered() throws InvalidCredentialsException {
        app.login("123-4567", "Password2");
    }

    @Test
    public void testMenuDisplaysCorrectlyWhenLoggedOut() throws IOException, InvalidCredentialsException {
        app.displayMenuOptions(testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatLoginMenuIsDisplayed(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testMenuDisplaysCorrectlyWhenLoggedIn() throws IOException, InvalidCredentialsException {
        login();
        app.displayMenuOptions(testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatMainMenuIsDisplayed(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testListBooksListsAllBooks() throws IOException {
        app.listItems(bookLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testListMoviesListAllMovies() throws IOException {
        app.listItems(movieLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatMovieListIsDisplayedWithAllMovies(scanner);
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

    @Test
    public void testCustomerSelectsListMoviesOption() throws Exception {
        app.selectMenuOption("List Movies");

        final Scanner scanner = getOutputScanner();
        assertThatMovieListIsDisplayedWithAllMovies(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test(expected = BibliotecaAppQuitException.class)
    public void testQuitThrowsException() throws BibliotecaAppQuitException, IOException {
        BibliotecaApp.quit(testOutputStream);
    }

    @Test
    public void testQuitShowsLeavingMessage() throws BibliotecaAppQuitException, IOException {
        try {
            BibliotecaApp.quit(testOutputStream);
            fail();
        } catch (BibliotecaAppQuitException e) {}

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for using Biblioteca App!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test(expected = BibliotecaAppQuitException.class)
    public void testUserSelectsQuitOption() throws Exception {
        app.selectMenuOption("Quit");
    }

    @Test
    public void testCustomerChecksOutABookSuccessfully() throws IOException, InvalidCredentialsException {
        login();
        app.checkoutItem("Great Expectations", bookLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutAMovieSuccessfully() throws IOException, InvalidCredentialsException {
        login();
        app.checkoutItem("Pulp Fiction", movieLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the movie."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutAnUnavailableBook() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException {
        login();
        app.checkoutItem("Great Expectations", bookLibrary, outputStream);
        app.checkoutItem("Great Expectations", bookLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That book is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutAnUnavailableMovie() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException {
        login();
        app.checkoutItem("Pulp Fiction", movieLibrary, outputStream);
        app.checkoutItem("Pulp Fiction", movieLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That movie is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutANonExistingBook() throws IOException {
        app.checkoutItem("Hard Times", bookLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That book is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutANonExistingMovie() throws IOException {
        app.checkoutItem("Django Unchained", movieLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That movie is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCheckedOutBookDoesNotAppearInBookList() throws Exception {
        login();
        app.checkoutItem("Great Expectations", bookLibrary, testOutputStream);
        app.selectMenuOption("List Books");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Title, Author, Year"));
        assertThat(scanner.nextLine(), is("Bleak House, Charles Dickens, 1853"));
        assertThat(scanner.nextLine(), is("The Pickwick Papers, Charles Dickens, 1837"));
        assertThat(scanner.hasNext(), is(false));
    }

    @Test
    public void testCheckedOutMovieDoesNotAppearInMovieList() throws Exception {
        login();
        app.checkoutItem("Pulp Fiction", movieLibrary, testOutputStream);
        app.selectMenuOption("List Movies");

        final Scanner scanner = getOutputScanner();

        assertThat(scanner.nextLine(), is("Title, Director, Year, Rating"));
        assertThat(scanner.nextLine(), is("Kill Bill, Quentin Tarantino, 2003, Unrated"));
        assertThat(scanner.nextLine(), is("Reservoir Dogs, Quentin Tarantino, 1992, 8"));
        assertThat(scanner.hasNext(), is(false));
    }

    @Test
    public void testCustomerSelectsCheckOutBookOptionSuccessfully() throws Exception {
        login();
        app.selectMenuOption("Checkout Book: Great Expectations");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsCheckOutMovieOptionSuccessfully() throws Exception {
        login();
        app.selectMenuOption("Checkout Movie: Pulp Fiction");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the movie."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsABookSuccessfully() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException {
        login();
        app.checkoutItem("Great Expectations", bookLibrary, outputStream);
        app.returnItem("Great Expectations", bookLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsAMovieSuccessfully() throws LibraryItemNotFoundException, IOException, InvalidCredentialsException {
        login();
        app.checkoutItem("Pulp Fiction", movieLibrary, outputStream);
        app.returnItem("Pulp Fiction", movieLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the movie."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsABookThatHasntBeenCheckedOut() throws IOException {
        app.returnItem("Great Expectations", bookLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid book to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsAMovieThatHasntBeenCheckedOut() throws IOException {
        app.returnItem("Pulp Fiction", movieLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid movie to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsABookThatDoesntExist() throws IOException {
        app.returnItem("Hard Times", bookLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid book to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsAMovieThatDoesntExist() throws IOException {
        app.returnItem("Django Unchained", movieLibrary, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid movie to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testReturnedBookAppearsInBookList() throws Exception {
        login();
        app.checkoutItem("Great Expectations", bookLibrary, testOutputStream);
        app.returnItem("Great Expectations", bookLibrary, testOutputStream);

        app.selectMenuOption("List Books");

        final Scanner scanner = getOutputScanner();
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testReturnedMovieAppearsInMovieList() throws Exception {
        login();
        app.checkoutItem("Pulp Fiction", movieLibrary, testOutputStream);
        app.returnItem("Pulp Fiction", movieLibrary, testOutputStream);
        app.selectMenuOption("List Movies");

        final Scanner scanner = getOutputScanner();
        assertThatMovieListIsDisplayedWithAllMovies(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsReturnBookOptionSuccessfully() throws Exception {
        login();
        app.checkoutItem("Great Expectations", bookLibrary, testOutputStream);
        app.selectMenuOption("Return Book: Great Expectations");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsReturnMovieOptionSuccessfully() throws Exception {
        login();
        app.checkoutItem("Pulp Fiction", movieLibrary, testOutputStream);
        app.selectMenuOption("Return Movie: Pulp Fiction");

        final Scanner scanner = getOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the movie."));
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
        login();
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

    private void login() throws InvalidCredentialsException {
        app.login("123-4567", "Password1");
    }

    private Scanner getTestOutputScanner() {
        return getOutputScanner(testOutputStream);
    }

    private Scanner getOutputScanner() {
        return getOutputScanner(outputStream);
    }

    private Scanner getOutputScanner(OutputStream outputStream) {
        return new Scanner(outputStream.toString());
    }

}
