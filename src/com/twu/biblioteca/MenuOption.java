package com.twu.biblioteca;

/**
 * Created by Matt on 25/02/15.
 */
public abstract class MenuOption<T> {

    private final String command;
    private final boolean secure;
    private String display;

    public abstract void execute(T target, String arg) throws Exception;

    protected MenuOption(final String command, final String argumentName, final boolean secure) {
        if (command == null || command.isEmpty()) throw new IllegalArgumentException("command cannot be null or empty");
        this.command = command;
        this.display = argumentName == null ? command : command + ": " + argumentName;
        this.secure = secure;
    }

    protected MenuOption(final String command, final boolean secure) {
        this(command, null, secure);
    }

    public String getCommand() {
        return command;
    }
    public String getDisplay() { return display; }
    public boolean isSecure() { return secure; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuOption that = (MenuOption) o;

        if (!command.equals(that.command)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return command.hashCode();
    }

}
