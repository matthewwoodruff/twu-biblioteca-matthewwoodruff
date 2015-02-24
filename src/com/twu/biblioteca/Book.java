package com.twu.biblioteca;

/**
 * Created by Matt on 23/02/15.
 */
public class Book implements Comparable<Book> {

    private final String title;
    private final String author;
    private final String year;
    private final int id;

    public Book(int id, String title, String author, String year) {
        if(title == null || title.isEmpty()) throw new IllegalArgumentException("title cannot be null or empty");
        if(author == null || author.isEmpty()) throw new IllegalArgumentException("author cannot be null or empty");
        if(year == null || year.isEmpty()) throw new IllegalArgumentException("year cannot be null or empty");
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public int getId() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (!author.equals(book.author)) return false;
        if (!title.equals(book.title)) return false;
        if (!year.equals(book.year)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public int compareTo(Book o) {
        return title.compareTo(o.getTitle());
    }
}
