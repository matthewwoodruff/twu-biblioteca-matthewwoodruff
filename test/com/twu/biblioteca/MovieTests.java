package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
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
}
