package com.twu.biblioteca.helper;

/**
 * Created by Matt on 25/02/15.
 */
public abstract class Option<T> {

    private final String command;
    private final Boolean displayState;
    private final boolean requiresSecure;
    private String display;

    public abstract void execute(T target, String arg) throws Exception;

    protected Option(final String command, final String argumentName, final Boolean displayState) {
        if (command == null || command.isEmpty()) throw new IllegalArgumentException("command cannot be null or empty");
        this.command = command;
        this.display = argumentName == null ? command : command + ": " + argumentName;
        this.displayState = displayState;
        this.requiresSecure = Boolean.TRUE.equals(displayState);
    }

    protected Option(final String command, final Boolean displayState) {
        this(command, null, displayState);
    }

    public String getCommand() {
        return command;
    }
    public String getDisplay() { return display; }

    public boolean isDisplayForLoginStatus(boolean loggedIn) { return displayState == null || (displayState == loggedIn);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option that = (Option) o;

        if (!command.equals(that.command)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return command.hashCode();
    }

}
