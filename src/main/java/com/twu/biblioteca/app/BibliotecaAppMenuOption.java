package com.twu.biblioteca.app;

import com.twu.biblioteca.exceptions.CommandNotFoundException;
import com.twu.biblioteca.helper.Option;

/**
 * Created by mwoodruf on 06/03/15.
 */
public class BibliotecaAppMenuOption {

    static class ListOption extends Option<BibliotecaApp> {
        private final Library<?> library;
        ListOption(final Library<?> library) {
            super("List " + library.getItemsName() + "s", null);
            this.library = library;
        }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.listItems(library);
        }
    }

    static class CheckoutOption extends Option<BibliotecaApp> {
        private final Library<?> library;
        CheckoutOption(final Library<?> library) {
            super("Checkout " + library.getItemsName(), "<Title>", true);
            this.library = library;
        }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.checkoutItem(arg, library);
        }
    }

    static class ReturnOption extends Option<BibliotecaApp> {
        private final Library<?> library;
        ReturnOption(final Library<?> library) {
            super("Return " + library.getItemsName(), "<Title>", true);
            this.library = library;
        }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.returnItem(arg, library);
        }
    }

    static class MyDetailsOption extends Option<BibliotecaApp> {
        MyDetailsOption() {
            super("My Details", true);
        }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.viewMyDetails();
        }
    }

    static class QuitOption extends Option<BibliotecaApp> {
        QuitOption() { super("Quit", null); }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.quit();
        }
    }

    static class LoginOption extends Option<BibliotecaApp> {
        LoginOption() { super("Login", "<Library Number> <Password>", false); }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            final String[] args = arg.split(" ");
            if(args.length != 2) throw new CommandNotFoundException();
            target.login(args[0], args[1]);
        }
    }

    static class LogoutOption extends Option<BibliotecaApp> {
        LogoutOption() { super("Logout", true); }
        @Override
        public void execute(BibliotecaApp target, String arg) throws Exception {
            target.logout();
        }
    }

}
