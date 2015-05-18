package models;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class Sensor {
    private int id;
    private String label;
    protected Path port;
    private boolean enabled;
    private List<Observation> observations;
    private List<Notification> notifications;

    public Sensor(int id, String label, Path port) {
        this.id = id;
        this.label = label;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public Observation getObservation() {

        Observation observation = null;

        try {
            Scanner scanner = new Scanner(port);
            float value = scanner.nextFloat();
            scanner.close();

            observation = new Observation(new Timestamp(System.currentTimeMillis()), value);
        } catch (IOException e) {
            System.out.println("models.Sensor.getObservation " + e.getMessage());
        }
        return observation;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
