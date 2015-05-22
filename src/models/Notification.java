package models;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    private int id;
    private int sensorID;
    private List<String> recipients = new ArrayList<>();
    private Threshold threshold;

    public Notification(int id, int sensorID, Threshold threshold) {
        this.id = id;
        this.sensorID = sensorID;
        this.threshold = threshold;
    }

    public Notification(int sensorID, Threshold threshold) {
        this.sensorID = sensorID;
        this.threshold = threshold;
    }

    public int getId() {
        return id;
    }

    public int getSensorID() {
        return sensorID;
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
