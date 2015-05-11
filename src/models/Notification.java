package models;

public class Notification {
    private int id;
    private String email;
    private Threshold threshold;

    public Notification(int id, String email, Threshold threshold) {
        this.id = id;
        this.email = email;
        this.threshold = threshold;
    }

    public Notification(String email, Threshold threshold) {
        this.email = email;
        this.threshold = threshold;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Threshold getThreshold() {
        return threshold;
    }
}
