package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 23/02/15.
 */
public class BibliotecaAppTests {

    private BibliotecaApp app;
    private OutputStream outputStream;
    private Scanner inputScanner;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
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
    public void testWelcomeMessageOnStartup() throws IOException {
        final Scanner scanner = runApp();
        assertThat(scanner.nextLine(), is("Welcome to Biblioteca!"));
    }

    @Test
    public void testMainMenuDisplayedAfterWelcomeMessage() throws IOException {
        final Scanner scanner = runApp();
        scanner.nextLine();
        assertThat(scanner.nextLine(), is("Please use one of the following options:"));
        assertThat(scanner.nextLine(), is("List Books"));
        assertThat(scanner.nextLine(), is("Quit"));
    }

    @Test
    public void testListBooksListsAllBooks() throws IOException {
        app.listBooks();
        final Scanner scanner = getOutputScanner();

        assertThat(scanner.nextLine(), is("Title, Author, Year"));
        assertThat(scanner.nextLine(), is("Bleak House, Charles Dickens, 1853"));
        assertThat(scanner.nextLine(), is("Great Expectations, Charles Dickens, 1860"));
        assertThat(scanner.nextLine(), is("The Pickwick Papers, Charles Dickens, 1837"));
    }

    @Test
    public void testInvalidMenuOptionMessage() throws IOException, BibliotecaAppQuitException {
        app.selectMenuOption("Invalid Option");
        assertThat(getOutputScanner().nextLine(), is("Select a valid option!"));

        app.selectMenuOption(null);
        assertThat(getOutputScanner().nextLine(), is("Select a valid option!"));
    }

    @Test
    public void testCustomerSelectsListBooksOption() throws IOException, BibliotecaAppQuitException {
        app.selectMenuOption("List Books");
        final Scanner scanner = getOutputScanner();

        assertThat(scanner.nextLine(), is("Title, Author, Year"));
        assertThat(scanner.nextLine(), is("Bleak House, Charles Dickens, 1853"));
        assertThat(scanner.nextLine(), is("Great Expectations, Charles Dickens, 1860"));
        assertThat(scanner.nextLine(), is("The Pickwick Papers, Charles Dickens, 1837"));
    }

    @Test(expected = BibliotecaAppQuitException.class)
    public void testQuitThrowsException() throws BibliotecaAppQuitException {
        app.quit();
    }

    @Test(expected = BibliotecaAppQuitException.class)
    public void testUserSelectsQuitOption() throws BibliotecaAppQuitException, IOException {
        app.selectMenuOption("Quit");
    }

    @Test
    public void testCustomerChecksOutABookSuccessfully() throws IOException {
        app.checkoutBook("Great Expectations");
        assertThat(getOutputScanner().nextLine(), is("Thank you! Enjoy the book."));
    }

    @Test
    public void testCustomerChecksOutAnUnavailableBook() {

    }

    private Scanner getOutputScanner() {
        return new Scanner(outputStream.toString());
    }

    private Scanner runApp() throws IOException {
        app.run();
        return getOutputScanner();
    }

}
