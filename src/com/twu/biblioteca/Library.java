package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.BookNotAvailableException;
import com.twu.biblioteca.exceptions.BookNotCheckedOutException;
import com.twu.biblioteca.exceptions.BookNotFoundException;

import java.util.*;

/**
 * Created by Matt on 23/02/15.
 */
public class Library {

    private final Set<Book> books;
    private final Set<Book> checkedOutBooks;

    public Library(Set<Book> books) {
        if (books == null) throw new IllegalArgumentException("books cannot be null");
        this.books = new HashSet<Book>(books);
        this.checkedOutBooks = new HashSet<Book>();
    }

    public List<Book> getBooks() {
        final Set<Book> booksCopy = new TreeSet<Book>(books);
        booksCopy.removeAll(checkedOutBooks);
        return new ArrayList<Book>(booksCopy);
    }

    public Book findBookByTitle(String title) {
        for (Book availableBook : books)
            if(availableBook.getTitle().equals(title))
                return availableBook;
        return null;
    }

    public void checkoutBook(String title) throws BookNotFoundException, BookNotAvailableException {
        checkoutBook(findBookByTitle(title));
    }

    public void checkoutBook(Book book) throws BookNotFoundException, BookNotAvailableException {
        verifyBookExists(book);
        verifyBookIsNotCheckedOut(book);
        checkedOutBooks.add(book);
    }

    public void returnBook(String title) throws BookNotCheckedOutException, BookNotFoundException {
        returnBook(findBookByTitle(title));
    }

    public void returnBook(Book book) throws BookNotCheckedOutException, BookNotFoundException {
        verifyBookExists(book);
        verifyBookIsCheckedOut(book);
        checkedOutBooks.remove(book);
    }

    private void verifyBookExists(Book book) throws BookNotFoundException {
        if(!books.contains(book)) throw new BookNotFoundException(book);
    }

    private void verifyBookIsNotCheckedOut(Book book) throws BookNotAvailableException {
        if(checkedOutBooks.contains(book)) throw new BookNotAvailableException(book);
    }

    private void verifyBookIsCheckedOut(Book book) throws BookNotCheckedOutException {
        if(!checkedOutBooks.contains(book)) throw new BookNotCheckedOutException(book);
    }

}
