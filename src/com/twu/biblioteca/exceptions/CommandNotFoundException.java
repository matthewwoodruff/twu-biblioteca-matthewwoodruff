package com.twu.biblioteca.exceptions;

/**
 * Created by Matt on 25/02/15.
 */
public class CommandNotFoundException extends Exception {

    public CommandNotFoundException(Object object) {
        super(String.valueOf(object));
    }

}
