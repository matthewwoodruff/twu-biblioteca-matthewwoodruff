package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 24/02/15.
 */
public class MovieTests {

    private Movie pulpFiction;

    @Before
    public void setup() {
        pulpFiction = new Movie("Pulp Fiction", "1994", "Quentin Tarantino", 9);
    }

    @Test
    public void testMovieHasATitle() {
        assertThat(pulpFiction.getTitle(), is("Pulp Fiction"));
    }

    @Test
    public void testMovieHasAYear() {
        assertThat(pulpFiction.getYear(), is("1994"));
    }

    @Test
    public void testMovieHasADirector() {
        assertThat(pulpFiction.getDirector(), is("Quentin Tarantino"));
    }

    @Test
    public void testMovieHasARating() {
        assertThat(pulpFiction.getRating(), is(9));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieCannotBeConstructedWithNullTitle() {
        new Movie(null, "1994", "Quentin Tarantino", 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieCannotBeConstructedWithEmptyTitle() {
        new Movie("", "1994", "Quentin Tarantino", 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieCannotBeConstructedWithNullYear() {
        new Movie("Pulp Fiction", null, "Quentin Tarantino", 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieCannotBeConstructedWithEmptyYear() {
        new Movie("Pulp Fiction", "", "Quentin Tarantino", 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieCannotBeConstructedWithNullDirector() {
        new Movie("Pulp Fiction", "1994", null, 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieCannotBeConstructedWithEmptyDirector() {
        new Movie("Pulp Fiction", "1994", "", 9);
    }

    @Test
    public void testMovieCanBeConstructedWithNullRating() {
        new Movie("Pulp Fiction", "1994", "Quentin Tarantino", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieCannotBeConstructedWithRatingLessThan1() {
        new Movie("Pulp Fiction", "1994", "Quentin Tarantino", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieCannotBeConstructedWithRatingGreaterThan10() {
        new Movie("Pulp Fiction", "1994", "Quentin Tarantino", 11);
    }

    @Test
    public void testCreateUnratedMovie() {
        final Movie movie = Movie.createUnratedMovie("Pulp Fiction", "1994", "Quentin Tarantino");
        assertThat(movie.getRating(), is(nullValue()));
    }

    @Test
    public void testCreateRatedMovie() {
        final Movie movie = Movie.createRatedMovie("Pulp Fiction", "1994", "Quentin Tarantino", 9);
        assertThat(movie.getRating(), is(9));
    }

    @Test
    public void testMovieHasCheckedOutIndication() {
        assertThat(pulpFiction.isCheckedOut(), is(false));
    }

    @Test
    public void testMovieHasAvailableIndication() {
        assertThat(pulpFiction.isAvailable(), is(true));
    }

    @Test
    public void testMovieCanBeCheckedOut() throws LibraryItemNotAvailableException {
        pulpFiction.checkOut();
        assertThat(pulpFiction.isCheckedOut(), is(true));
        assertThat(pulpFiction.isAvailable(), is(false));
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckingOutAnUnavailableMovieThrowsAnException() throws LibraryItemNotAvailableException {
        pulpFiction.checkOut();
        pulpFiction.checkOut();
    }

    @Test
    public void testMovieCanBeCheckedIn() throws LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        pulpFiction.checkOut();
        pulpFiction.checkIn();
        assertThat(pulpFiction.isCheckedOut(), is(false));
        assertThat(pulpFiction.isAvailable(), is(true));
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testCheckingInAMovieThatHasntBeenCheckedOutThrowsAnException() throws LibraryItemNotCheckedOutException {
        pulpFiction.checkIn();
    }

    @Test
    public void testMovieEquality() {
        final Movie movie = new Movie("Pulp Fiction", "1994", "Quentin Tarantino", 9);
        assertThat(pulpFiction, is(movie));
        assertThat(pulpFiction.hashCode(), is(movie.hashCode()));
    }

}
