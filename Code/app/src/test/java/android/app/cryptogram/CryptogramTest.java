package android.app.cryptogram;

import org.junit.Test;

import android.app.cryptogram.domain.Cryptogram;

import static org.junit.Assert.assertEquals;

public class CryptogramTest {

    @Test
    public void testConstructor() {
        String encrypted = "Thx awwlx is rxd.";
        String solution = "The apple is red.";
        String uid = "123";

        Cryptogram c = new Cryptogram(encrypted, solution, uid);

        assertEquals(encrypted, c.getEncrypted());
        assertEquals(solution, c.getSolution());
        assertEquals(uid, c.getUid());
    }

    @Test
    public void testGetterSetterEncrypted() {
        String encrypted = "Strgwthg trws reyb i afdaf a alfj sgj lj grldjjgrr" +
                "gfdffdskahrhf jsdkfj lkeih ljglsjirg ljdlgjri lsjdirghss dsgrgrgt" +
                "sgjkjybvgztev hfrk hkd jg sdfrgr.";
        Cryptogram c = new Cryptogram();
        c.setEncrypted(encrypted);
        assertEquals(c.getEncrypted(), encrypted);
    }

    @Test
    public void testGetterSettersolution() {
        String solution = "Seattle, a city on Puget Sound in the Pacific Northwest, " +
                "is surrounded by water, mountains and evergreen forests, and contains " +
                "thousands of acres of parkland.";
        Cryptogram c = new Cryptogram();
        c.setSolution(solution);
        assertEquals(c.getSolution(), solution);
    }

    @Test
    public void testGetterSetteruid() {
        String uid = "9876";
        Cryptogram c = new Cryptogram();
        c.setUid(uid);
        assertEquals(c.getUid(), uid);
    }

    @Test
    public void testToString()
    {
        String encrypted  = "f lkju iutth jartwerefds.";
        String solution = "I love pizza margherita.";
        String uid  = "123456789";

        String expected = "Cryptogram{" +
                "encrypted='" + encrypted + '\'' +
                ", solution='" + solution + '\'' +
                ", uid='" + uid + '\'' +
                '}';
        Cryptogram c = new Cryptogram(encrypted, solution, uid);
        assertEquals(expected, c.toString());
    }

}