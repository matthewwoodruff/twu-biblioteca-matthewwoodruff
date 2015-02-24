package com.twu.biblioteca.exceptions;

import com.twu.biblioteca.Book;

/**
 * Created by Matt on 23/02/15.
 */
public class BookNotCheckedOutException extends Exception {

    public BookNotCheckedOutException(Book book) {
        super(book + " not checked out form library");
    }

}
