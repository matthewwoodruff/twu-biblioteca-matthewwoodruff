package com.twu.biblioteca;

import com.twu.biblioteca.domain.Book;
import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.exceptions.CustomerRequiredException;
import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;
import com.twu.biblioteca.exceptions.LibraryItemNotFoundException;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Matt on 23/02/15.
 */
public class LibraryTests {

    private Library<Book> library;

    private Book greatExpectations;
    private Book pickwickPapers;
    private Book bleakHouse;

    private Set<Book> books;

    /* todo Annotation not working */
    @Mock
    private Customer customer;

    @Before
    public void setup() {
        customer = mock(Customer.class);

        greatExpectations = mock(Book.class);
        pickwickPapers = mock(Book.class);
        bleakHouse = mock(Book.class);

        when(greatExpectations.getTitle()).thenReturn("Great Expectations");
        when(pickwickPapers.getTitle()).thenReturn("Pickwick Papers");

        books = new HashSet<>();
        books.add(greatExpectations);
        books.add(pickwickPapers);
        library = new Library<>(books, Book.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsNullItems() {
        new Library<>(null, Book.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsNullItemsClass() {
        new Library<>(books, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsEmptyItems() {
        new Library(new HashSet<>(), Book.class);
    }

    @Test
    public void testGetListOfItems() {
        when(greatExpectations.isAvailable()).thenReturn(true);
        when(pickwickPapers.isAvailable()).thenReturn(true);
        assertThat(library.getItems(), is(Arrays.asList(greatExpectations, pickwickPapers)));
    }

    @Test
    public void testVerifyItemExists() throws LibraryItemNotFoundException {
        library.verifyItemExists(greatExpectations);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testVerifyItemExistsThrowsExceptionIfItDoesntExist() throws LibraryItemNotFoundException {
        library.verifyItemExists(mock(Book.class));
    }

    @Test
    public void testCheckoutItemThatExistsAndIsAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        library.checkoutItem(greatExpectations, customer);
        verify(greatExpectations, times(1)).checkOut(customer);
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckoutItemThatExistsButIsUnavailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        doThrow(new LibraryItemNotAvailableException()).when(greatExpectations).checkOut(customer);
        library.checkoutItem(greatExpectations, customer);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutItemThatDoesNotExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        library.checkoutItem(bleakHouse, customer);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutNullItem() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        library.checkoutItem(null, customer);
    }

    @Test
    public void testCheckoutItemByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        library.checkoutItemByTitle("Great Expectations", customer);
        verify(greatExpectations, times(1)).checkOut(customer);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckOutItemByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        library.checkoutItemByTitle("Hard Times", customer);
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckOutItemByTitleThatIsntAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        doThrow(new LibraryItemNotAvailableException()).when(greatExpectations).checkOut(customer);
        library.checkoutItemByTitle("Great Expectations", customer);
    }

    @Test
    public void testCheckedOutItemDoesNotShowInItemList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        when(greatExpectations.isAvailable()).thenReturn(false);
        when(pickwickPapers.isAvailable()).thenReturn(true);
        assertThat(library.getItems(), is(Arrays.asList(pickwickPapers)));
    }

    @Test
    public void testReturnItemThatExistsAndIsCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException, CustomerRequiredException {
        when(greatExpectations.isAvailable()).thenReturn(false);
        library.returnItem(greatExpectations);
        verify(greatExpectations, times(1)).checkIn();
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testVerifyThatItemDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.verifyItemExists(mock(Book.class));
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnItemThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItem(bleakHouse);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnNullItemThrowsNotFoundException() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItem(null);
    }

    @Test
    public void testReturnItemByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException, CustomerRequiredException {
        library.returnItemByTitle("Great Expectations");
        verify(greatExpectations, times(1)).checkIn();
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnItemByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItemByTitle("Hard Times");
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnItemByTitleThatHasntBeenCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        doThrow(LibraryItemNotCheckedOutException.class).when(greatExpectations).checkIn();
        library.returnItemByTitle("Great Expectations");
    }

    @Test
    public void testFindItemByTitle() {
        assertThat(library.findItemByTitle("Great Expectations"), is(greatExpectations));
    }

    @Test
    public void testFindItemByNullTitle() {
        assertThat(library.findItemByTitle(null), is(nullValue()));
    }

    @Test
    public void testFindItemByTitleThatDoesntExist() {
        assertThat(library.findItemByTitle("Hard Times"), is(nullValue()));
    }

    @Test
    public void testGetClassOfItemsInLibrary() {
        assertThat(library.getItemsClass(), CoreMatchers.<Class<Book>>is(Book.class));
    }

    @Test
    public void testGetNameOfItemsInLibrary() {
        assertThat(library.getItemsName(), is("Book"));
    }

}
