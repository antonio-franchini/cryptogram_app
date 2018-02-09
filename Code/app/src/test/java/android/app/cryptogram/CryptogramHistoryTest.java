package android.app.cryptogram;

import org.junit.Test;

import java.util.Date;

import android.app.cryptogram.domain.Cryptogram;
import android.app.cryptogram.domain.CryptogramHistory;
import android.app.cryptogram.domain.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CryptogramHistoryTest {

    @Test
    public void testConstructor() {
        CryptogramHistory c = new CryptogramHistory();
        assertNotNull(c);
    }

    @Test
    public void testGetterSetterPlayer() {
        CryptogramHistory c = new CryptogramHistory();
        Player p = new Player();
        c.setPlayer(p);
        assertEquals(c.getPlayer(), p);
    }

    @Test
    public void testGetterSettersolution() {
        CryptogramHistory ch = new CryptogramHistory();
        Cryptogram c = new Cryptogram();
        ch.setCryptogram(c);
        assertEquals(ch.getCryptogram(), c);
    }

    @Test
    public void testGetterSetterDate() {
        CryptogramHistory c = new CryptogramHistory();
        Date d = new Date();
        c.setDate(d);
        assertEquals(c.getDate(), d);
    }

    @Test
    public void testGetterSetterAttempt() {
        String attempt = "99";
        CryptogramHistory c = new CryptogramHistory();
        c.setAttempt(attempt);
        assertEquals(c.getAttempt(), attempt);
    }

    @Test
    public void testGetterSetterStatus() {
        CryptogramHistory.Status status;
        CryptogramHistory c = new CryptogramHistory();

        status = CryptogramHistory.Status.IN_PROGRESS;
        c.setStatus(status);
        assertEquals(c.getStatus(), status);

        status = CryptogramHistory.Status.SOLVED;
        c.setStatus(status);
        assertEquals(c.getStatus(), status);

        status = CryptogramHistory.Status.FAILED;
        c.setStatus(status);
        assertEquals(c.getStatus(), status);
    }

}