package com.twu.biblioteca.app;

import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.domain.LibraryItem;
import com.twu.biblioteca.exceptions.CustomerRequiredException;
import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;
import com.twu.biblioteca.exceptions.LibraryItemNotFoundException;

import java.util.*;

/**
 * Created by Matt on 23/02/15.
 */
public class Library<T extends LibraryItem> {

    private final List<T> items = new ArrayList<>();
    private final Map<String, T> itemsTitleMap = new HashMap<>();
    private final Class<T> itemsClass;

    public Library(Set<T> items, Class<T> itemsClass) {
        if(items == null || items.isEmpty()) throw new IllegalArgumentException("items cannot be null or empty");
        if(itemsClass == null) throw new IllegalArgumentException("itemsClass cannot be null");
        this.itemsClass = itemsClass;
        this.items.addAll(items);
        Collections.sort(this.items);
        for(final T item : items)
            itemsTitleMap.put(item.getTitle(), item);
    }

    List<T> getItems() {
        final List<T> availableItems = new ArrayList<>();
        for(final T item : items)
            if (item.isAvailable())
                availableItems.add(item);
        return availableItems;
    }

    T findItemByTitle(String title) {
        return itemsTitleMap.get(title);
    }

    void checkoutItemByTitle(String title, Customer checkedOutBy) throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        checkoutItem(findItemByTitle(title), checkedOutBy);
    }

    void returnItemByTitle(String title) throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        returnItem(findItemByTitle(title));
    }

    void returnItem(T item) throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        verifyItemExists(item);
        item.checkIn();
    }

    void verifyItemExists(T item) throws LibraryItemNotFoundException {
        if(item == null || !items.contains(item))
            throw new LibraryItemNotFoundException();
    }

    void checkoutItem(T item, Customer checkedOutBy) throws LibraryItemNotFoundException, LibraryItemNotAvailableException, CustomerRequiredException {
        verifyItemExists(item);
        item.checkOut(checkedOutBy);
    }

    public Class<T> getItemsClass() {
        return itemsClass;
    }

    public String getItemsName() {
        return getItemsClass().getSimpleName();
    }

    public String getItemsNameLowercase() {
        return getItemsName().toLowerCase();
    }
}
