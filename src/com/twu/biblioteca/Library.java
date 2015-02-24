package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.LibraryItemNotFoundException;
import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;

import java.util.*;

/**
 * Created by Matt on 23/02/15.
 */
public class Library {

    private final SortedSet<Book> books = new TreeSet<Book>();
    private final SortedSet<Movie> movies = new TreeSet<Movie>();
    private final List<LibraryItem> allItems = new ArrayList<LibraryItem>();

    private final Map<String, Book> bookTitleMap = new HashMap<String, Book>();
    private final Map<String, Movie> movieTitleMap = new HashMap<String, Movie>();

    public Library(Set<Book> books, Set<Movie> movies) {
        if (books == null) throw new IllegalArgumentException("books cannot be null");
        if (movies == null) throw new IllegalArgumentException("movies cannot be null");
        this.books.addAll(books);
        this.movies.addAll(movies);
        allItems.addAll(books);
        allItems.addAll(movies);

        for(final Book book : books)
            bookTitleMap.put(book.getTitle(), book);

        for(final Movie movie : movies)
            movieTitleMap.put(movie.getTitle(), movie);
    }

    public List<Book> getBooks() {
        final List<Book> availableBooks = new ArrayList<Book>();
        for(final Book book : books)
            if (book.isAvailable())
                availableBooks.add(book);
        return availableBooks;
    }

    public List<Movie> getMovies() {
        final List<Movie> availableMovies = new ArrayList<Movie>();
        for(final Movie movie : movies)
            if (movie.isAvailable())
                availableMovies.add(movie);
        return availableMovies;
    }

    public Book findBookByTitle(String title) {
        return bookTitleMap.get(title);
    }

    public void checkoutBook(String title) throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        checkoutItem(findBookByTitle(title));
    }

    public void returnBook(String title) throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        returnItem(findBookByTitle(title));
    }

    public void returnItem(LibraryItem item) throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        verifyItemExists(item);
        item.checkIn();
    }

    private void verifyItemExists(LibraryItem item) throws LibraryItemNotFoundException {
        if(item == null || !allItems.contains(item)) throw new LibraryItemNotFoundException();
    }

    public void checkoutItem(LibraryItem item) throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        verifyItemExists(item);
        item.checkOut();
    }

    public void checkoutMovie(String title) throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        checkoutItem(findMovieByTitle(title));
    }

    public Movie findMovieByTitle(String title) {
        return movieTitleMap.get(title);
    }

    public void returnMovie(String title) throws LibraryItemNotCheckedOutException, LibraryItemNotFoundException {
        returnItem(findMovieByTitle(title));
    }
}
