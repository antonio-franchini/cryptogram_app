package android.app.cryptogram.domain;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class CryptogramHistory extends SugarRecord<CryptogramHistory> implements Serializable {

    public enum Status{
        IN_PROGRESS("In-Progress"),SOLVED("Solved"),FAILED("Failed");

        private String desc;

        Status(String desc){
            this.desc = desc;
        }
        public String getDesc() {
            return desc;
        }
    };

    private Player player;
    private Cryptogram cryptogram;
    private Date date;
    private String attempt;
    private Status status;


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Cryptogram getCryptogram() {
        return cryptogram;
    }

    public void setCryptogram(Cryptogram cryptogram) {
        this.cryptogram = cryptogram;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
