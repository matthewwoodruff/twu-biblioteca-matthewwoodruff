package com.twu.biblioteca;

/**
 * Created by Matt on 23/02/15.
 */
public class BookNotAvailableException extends Exception {

    public BookNotAvailableException(Book book) {
        super(book + " not availble to checkoutBook from library");
    }

}
