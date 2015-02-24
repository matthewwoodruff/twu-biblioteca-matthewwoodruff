package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.LibraryItemNotFoundException;
import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;

import java.util.*;

/**
 * Created by Matt on 23/02/15.
 */
public class Library {

    private final SortedSet<Book> books;
    private final SortedSet<Movie> movies;
    private final Map<String, Book> bookMap;

    public Library(Set<Book> books, Set<Movie> movies) {
        if (books == null) throw new IllegalArgumentException("books cannot be null");
        if (movies == null) throw new IllegalArgumentException("movies cannot be null");
        this.books = new TreeSet<Book>(books);
        this.movies = new TreeSet<Movie>(movies);

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

    public void checkoutBook(String title) throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        checkout(findBookByTitle(title));
    }

    public void checkout(Book book) throws LibraryItemNotAvailableException, LibraryItemNotFoundException {
        verifyBookExists(book);
        book.checkOut();
    }

    public void returnBook(String title) throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        returnItem(findBookByTitle(title));
    }

    public void returnItem(Book book) throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        verifyBookExists(book);
        book.checkIn();
    }

    private void verifyBookExists(Book book) throws LibraryItemNotFoundException {
        if(book == null || !books.contains(book)) throw new LibraryItemNotFoundException();
    }

    private void verifyMovieExists(Movie movie) throws LibraryItemNotFoundException {
        if(movie == null || !movies.contains(movie)) throw new LibraryItemNotFoundException();
    }

    public List<Movie> getMovies() {
        return new ArrayList<Movie>(movies);
    }

    public void checkoutItem(Movie movie) throws LibraryItemNotAvailableException, LibraryItemNotFoundException {
        verifyMovieExists(movie);
        movie.checkOut();
    }

}
