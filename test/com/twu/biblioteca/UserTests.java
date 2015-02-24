package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 24/02/15.
 */
public class UserTests {

    private User user;

    @Before
    public void setup() {
        user = new User("Matthew", "Woodruff");
    }

    @Test
    public void testUserHasAFirstName() {
        assertThat(user.getFirstName(), is("Matthew"));
    }

    @Test
    public void testUserHasLastName() {
        assertThat(user.getLastName(), is("Woodruff"));
    }

}
