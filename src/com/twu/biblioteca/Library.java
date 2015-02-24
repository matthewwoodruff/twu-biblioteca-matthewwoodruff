package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.BookNotAvailableException;
import com.twu.biblioteca.exceptions.BookNotCheckedOutException;
import com.twu.biblioteca.exceptions.BookNotFoundException;

import java.util.*;

/**
 * Created by Matt on 23/02/15.
 */
public class Library {

    private final SortedSet<Book> books;
    private final Map<String, Book> bookMap;

    public Library(Set<Book> books) {
        if (books == null) throw new IllegalArgumentException("books cannot be null");
        this.books = new TreeSet<Book>(books);
        bookMap = new HashMap<String, Book>();
        for(final Book book : books)
            bookMap.put(book.getTitle(), book);
    }

    public List<Book> getBooks() {
        final List<Book> availableBooks = new ArrayList<Book>();
        for(final Book book : books)
            if (book.isAvailable())
                availableBooks.add(book);
        return availableBooks;
    }

    public Book findBookByTitle(String title) {
        return bookMap.get(title);
    }

    public void checkoutBook(String title) throws BookNotFoundException, BookNotAvailableException {
        checkoutBook(findBookByTitle(title));
    }

    public void checkoutBook(Book book) throws BookNotFoundException, BookNotAvailableException {
        verifyBookExists(book);
        book.checkOut();
    }

    public void returnBook(String title) throws BookNotCheckedOutException, BookNotFoundException {
        returnBook(findBookByTitle(title));
    }

    public void returnBook(Book book) throws BookNotCheckedOutException, BookNotFoundException {
        verifyBookExists(book);
        book.checkIn();
    }

    private void verifyBookExists(Book book) throws BookNotFoundException {
        if(book == null || !books.contains(book)) throw new BookNotFoundException();
    }

}
