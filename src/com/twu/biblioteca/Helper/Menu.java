package com.twu.biblioteca.helper;

import com.twu.biblioteca.exceptions.CommandNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 25/02/15.
 */
public class Menu<T> {

    private final T target;
    private final List<Option<T>> options = new ArrayList<>();
    private final Map<String, Option<T>> optionMap = new HashMap<>();

    public Menu(final T target, final List<Option<T>> options) {
        this.target = target;
        this.options.addAll(options);
        for(final Option<T> option : options)
            optionMap.put(option.getCommand(), option);
    }

    public void executeCommand(String command) throws Exception {
        if(command == null) command = "";
        final String[] args = command.split(":");
        final Option<T> option = optionMap.get(args[0]);

        if(option != null)
            option.execute(target, args.length > 1 ? args[1].trim() : null);
        else
            throw new CommandNotFoundException(command);
    }

    public List<Option<T>> getOptions() {
        return new ArrayList<>(options);
    }

}
