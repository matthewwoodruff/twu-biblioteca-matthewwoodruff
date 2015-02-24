package com.twu.biblioteca;

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
    
}
