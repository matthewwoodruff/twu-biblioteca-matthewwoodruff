package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.LibraryItemNotFoundException;
import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;

import java.util.*;

/**
 * Created by Matt on 23/02/15.
 */
public class Library<T extends LibraryItem> {

    private final SortedSet<T> items = new TreeSet<T>();
    private final Map<String, T> itemsTitleMap = new HashMap<String, T>();

    public Library(Set<T> items) {
        if (items == null) throw new IllegalArgumentException("items cannot be null");
        this.items.addAll(items);
        for(final T item : items)
            itemsTitleMap.put(item.getTitle(), item);
    }

    public List<T> getItems() {
        final List<T> availableItems = new ArrayList<T>();
        for(final T item : items)
            if (item.isAvailable())
                availableItems.add(item);
        return availableItems;
    }

    public T findItemByTitle(String title) {
        return itemsTitleMap.get(title);
    }

    public void checkoutItemByTitle(String title) throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        checkoutItem(findItemByTitle(title));
    }

    public void returnItemByTitle(String title) throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        returnItem(findItemByTitle(title));
    }

    public void returnItem(T item) throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        verifyItemExists(item);
        item.checkIn();
    }

    private void verifyItemExists(T item) throws LibraryItemNotFoundException {
        if(item == null || !items.contains(item)) throw new LibraryItemNotFoundException();
    }

    public void checkoutItem(T item) throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        verifyItemExists(item);
        item.checkOut();
    }
    
}
