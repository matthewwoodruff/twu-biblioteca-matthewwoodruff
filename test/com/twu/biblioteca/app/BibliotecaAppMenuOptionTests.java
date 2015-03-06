package com.twu.biblioteca.app;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.twu.biblioteca.app.BibliotecaAppMenuOption.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by mwoodruf on 06/03/15.
 */
public class BibliotecaAppMenuOptionTests {

    @Mock
    private BibliotecaApp target;
    @Mock
    private Library<?> library;

    @Before
    public void setup() {
        initMocks(this);

        when(library.getItemsName()).thenReturn("Book");
    }

    @Test
    public void testListOptionCallsListItems() throws Exception {
        new ListOption(library).execute(target, null);
        verify(target, times(1)).listItems(library);
    }

    @Test
    public void testListOptionHasCorrectCommandAndDisplay() throws Exception {
        assertThat(new ListOption(library).getCommand(), is("List Books"));
        assertThat(new ListOption(library).getDisplay(), is("List Books"));
    }

    @Test
    public void testListItemDisplaysCorrectlyForLoginStatus() {
        assertThat(new ListOption(library).isDisplayForLoginStatus(false), is(true));
        assertThat(new ListOption(library).isDisplayForLoginStatus(true), is(true));
    }

    @Test
    public void testCheckoutOptionCallsCheckoutItem() throws Exception {
        new CheckoutOption(library).execute(target, "Great Expectations");
        verify(target, times(1)).checkoutItem("Great Expectations", library);
    }

    @Test
    public void testCheckoutOptionDisplaysCorrectlyForLoginStatus() {
        assertThat(new CheckoutOption(library).isDisplayForLoginStatus(false), is(false));
        assertThat(new CheckoutOption(library).isDisplayForLoginStatus(true), is(true));
        assertThat(new CheckoutOption(library).requiresSecure(), is(true));
    }

    @Test
    public void testCheckoutOptionHasCorrectCommandAndDisplay() throws Exception {
        assertThat(new CheckoutOption(library).getCommand(), is("Checkout Book"));
        assertThat(new CheckoutOption(library).getDisplay(), is("Checkout Book: <Title>"));
    }

    @Test
    public void testReturnOptionCallsReturnItem() throws Exception {
        new ReturnOption(library).execute(target, "Great Expectations");
        verify(target, times(1)).returnItem("Great Expectations", library);
    }

    @Test
    public void testReturnOptionDisplaysCorrectlyForLoginStatus() {
        assertThat(new ReturnOption(library).isDisplayForLoginStatus(false), is(false));
        assertThat(new ReturnOption(library).isDisplayForLoginStatus(true), is(true));
        assertThat(new ReturnOption(library).requiresSecure(), is(true));
    }

    @Test
    public void testReturnOptionHasCorrectCommandAndDisplay() throws Exception {
        assertThat(new ReturnOption(library).getCommand(), is("Return Book"));
        assertThat(new ReturnOption(library).getDisplay(), is("Return Book: <Title>"));
    }

    @Test
    public void testMyDetailsOptionCallsViewMyDetails() throws Exception {
        new MyDetailsOption().execute(target, null);
        verify(target, times(1)).viewMyDetails();
    }

    @Test
    public void testMyDetailsOptionDisplaysCorrectlyForLoginStatus() {
        assertThat(new MyDetailsOption().isDisplayForLoginStatus(false), is(false));
        assertThat(new MyDetailsOption().isDisplayForLoginStatus(true), is(true));
        assertThat(new MyDetailsOption().requiresSecure(), is(true));
    }

    @Test
    public void testMyDetailsOptionHasCorrectCommandAndDisplay() throws Exception {
        assertThat(new MyDetailsOption().getCommand(), is("My Details"));
        assertThat(new MyDetailsOption().getDisplay(), is("My Details"));
    }

    @Test
    public void testQuitOptionCallsQuit() throws Exception {
        new QuitOption().execute(target, null);
        verify(target, times(1)).quit();
    }

    @Test
    public void testQuitOptionHasCorrectCommandAndDisplay() throws Exception {
        assertThat(new QuitOption().getCommand(), is("Quit"));
        assertThat(new QuitOption().getDisplay(), is("Quit"));
    }

    @Test
    public void testQuitOptionDisplaysCorrectlyForLoginStatus() {
        assertThat(new QuitOption().isDisplayForLoginStatus(false), is(true));
        assertThat(new QuitOption().isDisplayForLoginStatus(true), is(true));
        assertThat(new QuitOption().requiresSecure(), is(false));
    }

    @Test
    public void testLoginOptionCallsLogin() throws Exception {
        new LoginOption().execute(target, "123-4567 Password1");
        verify(target, times(1)).login("123-4567", "Password1");
    }

    @Test
    public void testLoginOptionHasCorrectCommandAndDisplay() throws Exception {
        assertThat(new LoginOption().getCommand(), is("Login"));
        assertThat(new LoginOption().getDisplay(), is("Login: <Library Number> <Password>"));
    }

    @Test
    public void testLoginOptionDisplaysCorrectlyForLoginStatus() {
        assertThat(new LoginOption().isDisplayForLoginStatus(false), is(true));
        assertThat(new LoginOption().isDisplayForLoginStatus(true), is(false));
        assertThat(new LoginOption().requiresSecure(), is(false));
    }

    @Test
    public void testLogoutOptionCallsLogout() throws Exception {
        new LogoutOption().execute(target, null);
        verify(target, times(1)).logout();
    }

    @Test
    public void testLogoutOptionDisplaysCorrectlyForLoginStatus() {
        assertThat(new LogoutOption().isDisplayForLoginStatus(false), is(false));
        assertThat(new LogoutOption().isDisplayForLoginStatus(true), is(true));
        assertThat(new LogoutOption().requiresSecure(), is(true));
    }

    @Test
    public void testLogoutOptionHasCorrectCommandAndDisplay() throws Exception {
        assertThat(new LogoutOption().getCommand(), is("Logout"));
        assertThat(new LogoutOption().getDisplay(), is("Logout"));
    }

}
