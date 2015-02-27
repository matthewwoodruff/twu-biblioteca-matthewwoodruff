package com.twu.biblioteca.helper;

import com.twu.biblioteca.exceptions.BibliotecaAppQuitException;
import com.twu.biblioteca.exceptions.CommandNotFoundException;
import com.twu.biblioteca.exceptions.CustomerRequiredException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 25/02/15.
 */
public class Menu<T> {

    private final T target;
    private final SecurityStatus securityStatus;
    private final List<Option<T>> options = new ArrayList<>();
    private final Map<String, Option<T>> optionMap = new HashMap<>();

    public Menu(final T target, final SecurityStatus securityStatus, final List<Option<T>> options) {
        this.target = target;
        this.securityStatus = securityStatus;
        this.options.addAll(options);
        for(final Option<T> option : options)
            optionMap.put(option.getCommand(), option);
    }

    private void verifyOptionCanBeExecuted(Option<T> option) throws CustomerRequiredException {
        if(option.requiresSecure() && !securityStatus.isCustomerLoggedIn())
            throw new CustomerRequiredException();
    }

    public void executeCommand(String command) throws Exception {
        if(command == null) command = "";
        final String[] args = command.split(":");
        final Option<T> option = optionMap.get(args[0]);

        if(option == null)
            throw new CommandNotFoundException();

        verifyOptionCanBeExecuted(option);

        option.execute(target, args.length > 1 ? args[1].trim() : null);
    }

    public List<Option<T>> getOptions() {
        final boolean loggedIn = securityStatus.isCustomerLoggedIn();
        final List<Option<T>> validOptions = new ArrayList<>();
        for(final Option option : options)
            if(option.isDisplayForLoginStatus(loggedIn))
                validOptions.add(option);
        return validOptions;
    }

}
