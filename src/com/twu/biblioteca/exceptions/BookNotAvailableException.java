package com.twu.biblioteca.exceptions;

import com.twu.biblioteca.Book;

/**
 * Created by Matt on 23/02/15.
 */
public class BookNotAvailableException extends Exception {

    public BookNotAvailableException(Book book) {
        super(book + " not availble to checkoutBook from library");
    }

}
