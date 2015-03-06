package com.twu.biblioteca.helper;

import com.twu.biblioteca.app.SecurityContext;
import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.exceptions.CommandNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 25/02/15.
 */
public class MenuTests {

    private SimpleClass simpleClass;

    @Before
    public void setup() {
        simpleClass = new SimpleClass();
    }

    @Test
    public void testMenuCanExecuteSimpleCommand() throws Exception {
        buildMenuWithOptions(new SimpleOption()).executeCommand("Simple Command");
        assertThat(simpleClass.bool, is(true));
    }

    @Test
    public void testMenuCanExecuteCommandWithArgument() throws Exception {
        final Menu menu = buildMenuWithOptions(new ArgumentOption());

        menu.executeCommand("Argument Command: Hello World");
        assertThat(simpleClass.string, is("Hello World"));

        menu.executeCommand("Argument Command: Goodbye World");
        assertThat(simpleClass.string, is("Goodbye World"));
    }

    @Test(expected = CommandNotFoundException.class)
    public void testMenuThrowsExceptionWhenAnUnknownCommandIsEntered() throws Exception {
        buildMenuWithOptions(new ArgumentOption()).executeCommand("Simple Command");
    }

    @Test
    public void testMenuHasMenuOptions() {
        final Menu<SimpleClass> menu = buildMenuWithOptions(new SimpleOption(), new ArgumentOption());
        assertThat(menu.getOptions(), is(Arrays.asList(new SimpleOption(), new ArgumentOption())));
    }

    private Menu buildMenuWithOptions(Option<SimpleClass>... opts) {
        return new Menu<>(simpleClass, new SecurityContext(Customer.getCustomers()), Arrays.asList(opts));
    }

    private static class SimpleClass {
        public boolean bool = false;
        public String string;
    }

    private static class SimpleOption extends Option<SimpleClass> {
        public SimpleOption() { super("Simple Command", false); }
        @Override
        public void execute(SimpleClass target, String arg) throws Exception {
            target.bool = true;
        }
    }

    private static class ArgumentOption extends Option<SimpleClass> {
        public ArgumentOption() { super("Argument Command", false); }
        @Override
        public void execute(SimpleClass target, String arg) throws Exception {
            target.string = arg;
        }
    }

}
