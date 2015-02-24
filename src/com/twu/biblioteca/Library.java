package com.twu.biblioteca;

import java.util.*;

/**
 * Created by Matt on 23/02/15.
 */
public class Library {

    private final Set<Book> books;
    private final Set<Book> checkedOut;

    public Library(Set<Book> books) {
        if (books == null) throw new IllegalArgumentException("books cannot be null");
        this.books = new HashSet<Book>(books);
        this.checkedOut = new HashSet<Book>();
    }

    public List<Book> getBooks() {
        final Set<Book> booksCopy = new HashSet<Book>(books);
        booksCopy.removeAll(checkedOut);
        final List<Book> availableBooks = new ArrayList<Book>(booksCopy);
        Collections.sort(availableBooks);
        return availableBooks;
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
        if (!books.contains(book)) throw new BookNotFoundException(book);
        else if (checkedOut.contains(book)) throw new BookNotAvailableException(book);
        checkedOut.add(book);
    }

    public void returnBook(Book book) throws BookNotCheckedOutException, BookNotFoundException {
        if(!books.contains(book)) throw new BookNotFoundException(book);
        else if(!checkedOut.contains(book)) throw new BookNotCheckedOutException(book);
        checkedOut.remove(book);
    }

}
