package models;

import java.util.ArrayList;
import java.util.List;

public class Trigger {
    private int id;
    private int sensorID;
    private List<String> recipients = new ArrayList<>();
    private Threshold threshold;

    public Trigger(int id, int sensorID, Threshold threshold) {
        this.id = id;
        this.sensorID = sensorID;
        this.threshold = threshold;
    }

    public Trigger(int sensorID, Threshold threshold) {
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
