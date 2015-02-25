package com.twu.biblioteca;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matt on 24/02/15.
 */
public final class Movie extends LibraryItem {

    private final String director;
    private final Integer rating;

    public Movie(final String title, final String year,
                 final String director, final Integer rating) {
        super(title, year);
        if(director == null || director.isEmpty()) throw new IllegalArgumentException("director cannot be null or empty");
        if(rating != null && (rating < 1 || rating > 10)) throw new IllegalArgumentException("rating must be between 1 and 10");
        this.director = director;
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public Integer getRating() {
        return rating;
    }

    public static Movie createUnratedMovie(String title, String year, String director) {
        return new Movie(title, year, director, null);
    }

    public static Movie createRatedMovie(String title, String year, String director, int rating) {
        return new Movie(title, year, director, rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Movie movie = (Movie) o;

        if (!director.equals(movie.director)) return false;
        if (rating != null ? !rating.equals(movie.rating) : movie.rating != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + director.hashCode();
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
    }

    protected static Set<Movie> getDefaultMovies() {
        final Set<Movie> movies = new HashSet<Movie>();
        movies.add(Movie.createRatedMovie("Pulp Fiction", "Quentin Tarantino", "1994", 9));
        movies.add(Movie.createRatedMovie("Reservoir Dogs", "Quentin Tarantino", "1992", 8));
        movies.add(Movie.createUnratedMovie("Kill Bill", "Quentin Tarantino", "2003"));
        return movies;
    }

}

