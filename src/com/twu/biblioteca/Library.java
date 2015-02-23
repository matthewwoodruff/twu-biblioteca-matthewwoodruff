package com.twu.biblioteca;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 23/02/15.
 */
public class Library {

    private final List<Book> books;
    private final ArrayList<Book> checkedOut = new ArrayList<Book>();

    public Library(List<Book> books) {
        if (books == null) throw new IllegalArgumentException("books cannot be null");
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void checkoutBook(Book book) throws BookNotFoundException, BookNotAvailableException {
        if (book == null) throw new IllegalArgumentException("book cannot be null");
        if (!books.contains(book)) {
            if (checkedOut.contains(book)) throw new BookNotAvailableException(book);
            else throw new BookNotFoundException(book);
        }
        checkedOut.add(book);
        books.remove(book);
    }

    public void returnBook(Book book) throws BookNotCheckedOutException, BookNotFoundException {
        if (book == null) throw new IllegalArgumentException("book cannot be null");
        if(!checkedOut.contains(book)) {
            if(books.contains(book)) throw new BookNotCheckedOutException(book);
            else throw new BookNotFoundException(book);
        }
        books.add(book);
        checkedOut.remove(book);
    }

}
