package models;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Sensor {
    private static String pathToDataPort = "";
    private int id;
    private String portName;
    private String label;
    private boolean enabled;
    private int sampleRate;
    private List<Measurement> measurements;
    private List<Notification> notifications;

    public Sensor(int id, String portName, String label, boolean enabled, int sampleRate) {
        this.id = id;
        this.portName = portName;
        this.label = label;
        this.enabled = enabled;
        this.sampleRate = sampleRate;
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

    public int getSampleRate() {
        return sampleRate;
    }

    public Measurement getCurrentMeasurement() {

        Measurement measurement = null;
        Path port = Paths.get(pathToDataPort + portName);

        try {
            Scanner scanner = new Scanner(port);
            double value = scanner.nextDouble();
            scanner.close();

            measurement = new Measurement(new Date(System.currentTimeMillis()), value);
        } catch (IOException e) {
            System.out.println("models.Sensor.getCurrentMeasurement error: " + e.getMessage());
        }

        return measurement;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
