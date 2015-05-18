package models;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Scanner;

public class Sensor {
    private int id;
    private String label;
    protected Path port;
    private int sampleRate;
    private boolean enabled;

    public Sensor(int id, String label, Path port, int sampleRate) {
        this.id = id;
        this.label = label;
        this.port = port;
        this.sampleRate = sampleRate;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public int getSampleRate() {
        return sampleRate;
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
}
