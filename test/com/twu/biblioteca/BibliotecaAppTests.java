package com.twu.biblioteca;

import com.sun.deploy.util.StringUtils;
import com.twu.biblioteca.exceptions.BibliotecaAppQuitException;
import com.twu.biblioteca.exceptions.LibraryItemNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        testOutputStream = new ByteArrayOutputStream();
        inputScanner = new Scanner("");
        app = new BibliotecaApp(inputScanner, outputStream);
    }

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

    @Test
    public void testDisplayWelcomeMessage() throws IOException {
        app.displayWelcomeMessage(testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Welcome to Biblioteca!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testMainMenuDisplaysCorrectly() throws IOException {
        app.displayMenuOptions(testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatMainMenuIsDisplayed(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testListBooksListsAllBooks() throws IOException {
        app.listBooks(testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testListMoviesListAllMovies() throws IOException {
        app.listMovies(testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatMovieListIsDisplayedWithAllMovies(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testInvalidMenuOptionMessage() throws IOException, BibliotecaAppQuitException {
        app.selectMenuOption("Invalid Option", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Select a valid option!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testNullMenuOptionMessage() throws IOException, BibliotecaAppQuitException {
        app.selectMenuOption(null, testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Select a valid option!"));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsListBooksOption() throws IOException, BibliotecaAppQuitException {
        app.selectMenuOption("List Books", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsListMoviesOption() throws IOException, BibliotecaAppQuitException {
        app.selectMenuOption("List Movies", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
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
    public void testUserSelectsQuitOption() throws BibliotecaAppQuitException, IOException {
        app.selectMenuOption("Quit", outputStream);
    }

    @Test
    public void testCustomerChecksOutABookSuccessfully() throws IOException {
        app.checkoutBook("Great Expectations", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutAMovieSuccessfully() throws IOException {
        app.checkoutMovie("Pulp Fiction", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the movie."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutAnUnavailableBook() throws LibraryItemNotFoundException, IOException {
        app.checkoutBook("Great Expectations", outputStream);
        app.checkoutBook("Great Expectations", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That book is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutAnUnavailableMovie() throws LibraryItemNotFoundException, IOException {
        app.checkoutMovie("Pulp Fiction", outputStream);
        app.checkoutMovie("Pulp Fiction", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That movie is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutANonExistingBook() throws IOException {
        app.checkoutBook("Hard Times", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That book is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerChecksOutANonExistingMovie() throws IOException {
        app.checkoutMovie("Django Unchained", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That movie is not available."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCheckedOutBookDoesNotAppearInBookList() throws LibraryItemNotFoundException, IOException, BibliotecaAppQuitException {
        app.checkoutBook("Great Expectations", outputStream);
        app.selectMenuOption("List Books", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Title, Author, Year"));
        assertThat(scanner.nextLine(), is("Bleak House, Charles Dickens, 1853"));
        assertThat(scanner.nextLine(), is("The Pickwick Papers, Charles Dickens, 1837"));
        assertThat(scanner.hasNext(), is(false));
    }

    @Test
    public void testCheckedOutMovieDoesNotAppearInMovieList() throws LibraryItemNotFoundException, IOException, BibliotecaAppQuitException {
        app.checkoutMovie("Pulp Fiction", outputStream);
        app.selectMenuOption("List Movies", testOutputStream);

        final Scanner scanner = getTestOutputScanner();

        assertThat(scanner.nextLine(), is("Title, Director, Year, Rating"));
        assertThat(scanner.nextLine(), is("Kill Bill, Quentin Tarantino, 2003, Unrated"));
        assertThat(scanner.nextLine(), is("Reservoir Dogs, Quentin Tarantino, 1992, 8"));
        assertThat(scanner.hasNext(), is(false));
    }

    @Test
    public void testCustomerSelectsCheckOutBookOptionSuccessfully() throws IOException, BibliotecaAppQuitException {
        app.selectMenuOption("Checkout Book: Great Expectations", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsCheckOutMovieOptionSuccessfully() throws IOException, BibliotecaAppQuitException {
        app.selectMenuOption("Checkout Movie: Pulp Fiction", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you! Enjoy the movie."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsABookSuccessfully() throws LibraryItemNotFoundException, IOException {
        app.checkoutBook("Great Expectations", outputStream);
        app.returnBook("Great Expectations", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsAMovieSuccessfully() throws LibraryItemNotFoundException, IOException {
        app.checkoutMovie("Pulp Fiction", outputStream);
        app.returnMovie("Pulp Fiction", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the movie."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsABookThatHasntBeenCheckedOut() throws IOException {
        app.returnBook("Great Expectations", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid book to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsAMovieThatHasntBeenCheckedOut() throws IOException {
        app.returnMovie("Pulp Fiction", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid movie to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsABookThatDoesntExist() throws IOException {
        app.returnBook("Hard Times", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid book to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerReturnsAMovieThatDoesntExist() throws IOException {
        app.returnMovie("Django Unchained", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("That is not a valid movie to return."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testReturnedBookAppearsInBookList() throws IOException, LibraryItemNotFoundException, BibliotecaAppQuitException {
        app.checkoutBook("Great Expectations", outputStream);
        app.returnBook("Great Expectations", outputStream);
        app.selectMenuOption("List Books", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatBookListIsDisplayedWithAllBooks(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testReturnedMovieAppearsInMovieList() throws IOException, LibraryItemNotFoundException, BibliotecaAppQuitException {
        app.checkoutMovie("Pulp Fiction", outputStream);
        app.returnMovie("Pulp Fiction", outputStream);
        app.selectMenuOption("List Movies", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThatMovieListIsDisplayedWithAllMovies(scanner);
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsReturnBookOptionSuccessfully() throws IOException, BibliotecaAppQuitException {
        app.checkoutBook("Great Expectations", outputStream);
        app.selectMenuOption("Return Book: Great Expectations", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the book."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerSelectsReturnMovieOptionSuccessfully() throws IOException, BibliotecaAppQuitException {
        app.checkoutMovie("Pulp Fiction", outputStream);
        app.selectMenuOption("Return Movie: Pulp Fiction", testOutputStream);

        final Scanner scanner = getTestOutputScanner();
        assertThat(scanner.nextLine(), is("Thank you for returning the movie."));
        assertThat(scanner.hasNextLine(), is(false));
    }

    @Test
    public void testCustomerHasSuccessfulJourneyCheckingABookOutAndIn() throws IOException {
        final List<String> commands = Arrays.asList(
                "List Books",
                "Checkout Book: Great Expectations",
                "Return Book: Great Expectations",
                "Quit");

        inputScanner = new Scanner(StringUtils.join(commands, "\n"));
        app = new BibliotecaApp(inputScanner, outputStream);

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

    private void assertThatMainMenuIsDisplayed(Scanner scanner) {
        assertThat(scanner.nextLine(), is("Please use one of the following options:"));
        assertThat(scanner.nextLine(), is("List Books"));
        assertThat(scanner.nextLine(), is("List Movies"));
        assertThat(scanner.nextLine(), is("Checkout Book: <Title>"));
        assertThat(scanner.nextLine(), is("Checkout Movie: <Title>"));
        assertThat(scanner.nextLine(), is("Return Book: <Title>"));
        assertThat(scanner.nextLine(), is("Return Movie: <Title>"));
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
