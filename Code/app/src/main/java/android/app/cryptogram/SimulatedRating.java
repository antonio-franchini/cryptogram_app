package android.app.cryptogram;

public class SimulatedRating {
    //private String username;
    private String fistname;
    private String lastname;
    private int solved;
    private int started;
    private int failed;

    public SimulatedRating(String fistname, String lastname, int solved, int started, int failed) {
        this.fistname = fistname;
        this.lastname = lastname;
        this.solved = solved;
        this.started = started;
        this.failed = failed;
    }

    public String getFistname() {
        return fistname;
    }

    public void setFistname(String fistname) {
        this.fistname = fistname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getSolved() {
        return solved;
    }

    public void setSolved(int solved) {
        this.solved = solved;
    }

    public int getStarted() {
        return started;
    }

    public void setStarted(int started) {
        this.started = started;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }
}
