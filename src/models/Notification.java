package models;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    private int id;
    private List<String> recipients = new ArrayList<>();
    private Threshold threshold;

    public Notification(int id, Threshold threshold) {
        this.id = id;
        this.threshold = threshold;
    }

    public Notification(Threshold threshold) {
        this.threshold = threshold;
    }

    public int getId() {
        return id;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public Threshold getThreshold() {
        return threshold;
    }

    public void addRecipient(String email) {
        recipients.add(email);
    }
}
