package android.app.cryptogram;

import org.junit.Test;

import android.app.cryptogram.domain.Player;

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    @Test
    public void testConstructor() {
        String username = "mariob";
        String firstname = "Mario";
        String lastname = "Balotelli";

        Player p = new Player(username, firstname, lastname);

        assertEquals(username, p.getUsername());
        assertEquals(firstname, p.getFirstname());
        assertEquals(lastname, p.getLastname());
    }

    @Test
    public void testGetterSetterUsername() {
        String username = "afranchi";
        Player p = new Player();
        p.setUsername(username);
        assertEquals(p.getUsername(), username);
    }

    @Test
    public void testGetterSetterFirstname() {
        String firstname = "Mike";
        Player p = new Player();
        p.setFirstname(firstname);
        assertEquals(p.getFirstname(), firstname);
    }

    @Test
    public void testGetterSetterLastname() {
        String lastname = "Jordan";
        Player p = new Player();
        p.setLastname(lastname);
        assertEquals(p.getLastname(), lastname);
    }

    @Test
    public void testToString()
    {
        String username  = "enriquei";
        String firstname = "Enrique";
        String lastname  = "Iglesias";

        String expected = "Player{" +
                "username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';

        Player p = new Player(username, firstname, lastname);
        assertEquals(expected, p.toString());
    }

}