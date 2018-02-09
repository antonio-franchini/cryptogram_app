package android.app.cryptogram.domain;


import com.orm.SugarRecord;

import java.io.Serializable;

public class Cryptogram extends SugarRecord<Cryptogram> implements Serializable{

    private String encrypted;
    private String solution;
    private String uid;

    public Cryptogram() {
    }

    public Cryptogram(String encrypted, String solution, String uid) {
        this.encrypted = encrypted;
        this.solution = solution;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "Cryptogram{" +
                "encrypted='" + encrypted + '\'' +
                ", solution='" + solution + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
