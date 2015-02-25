package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;

/**
 * Created by Matt on 24/02/15.
 */
public abstract class LibraryItem<T extends LibraryItem> implements Comparable<T> {

    private final String title;
    private final String year;
    private Customer checkedOutBy;

    protected LibraryItem(final String title, final String year) {
        if(title == null || title.isEmpty()) throw new IllegalArgumentException("title cannot be null or empty");
        if(year == null || year.isEmpty()) throw new IllegalArgumentException("year cannot be null or empty");
        this.title = title;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public boolean isCheckedOut() {
        return checkedOutBy != null;
    }

    public void checkOut(Customer checkedOutBy) throws LibraryItemNotAvailableException {
        if(checkedOutBy == null) throw new IllegalArgumentException("checkedOutBy cannot be null");
        if(isCheckedOut()) throw new LibraryItemNotAvailableException();
        this.checkedOutBy = checkedOutBy;
    }

    public Customer getCheckedOutBy() throws LibraryItemNotCheckedOutException {
        verifyItemIsCheckedOut();
        return checkedOutBy;
    }

    private void verifyItemIsCheckedOut() throws LibraryItemNotCheckedOutException {
        if(isAvailable()) throw new LibraryItemNotCheckedOutException();
    }

    public void checkIn() throws LibraryItemNotCheckedOutException {
        verifyItemIsCheckedOut();
        checkedOutBy = null;
    }

    public boolean isAvailable() {
        return !isCheckedOut();
    }

    @Override
    public int compareTo(T o) {
        return title.compareTo(o.getTitle());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LibraryItem that = (LibraryItem) o;

        if (checkedOutBy != null ? !checkedOutBy.equals(that.checkedOutBy) : that.checkedOutBy != null) return false;
        if (!title.equals(that.title)) return false;
        if (!year.equals(that.year)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + (checkedOutBy != null ? checkedOutBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LibraryItem{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", checkedOutBy=" + checkedOutBy +
                '}';
    }

}
