package mock;

import models.Measurement;
import models.Sensor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SensorMock extends Sensor {

    public enum Type {
        TEMPERATURE (16, 38),
        HUMIDITY (10, 90),
        LIGHT (0, 100);

        private final int minValue;
        private final int maxValue;

        Type(int minValue, int maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public int getMinValue() {
            return minValue;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public int getRange() {
            return maxValue-minValue;
        }
    }

    private static String pathToDataPort = "/mock/ports/";
    public static final int PERIOD = 60 * 60 * 24; // 60 sec * 60 min * 24 hrs = 1 day
    private static final int SAMPLE_RATE_IN_SECONDS = 1;
    private int elapsedTimeInSeconds = 0;
    private Type type;

    public SensorMock(Type type) {
        super(type.ordinal(), Integer.toString(type.ordinal()), type.toString(), false, SAMPLE_RATE_IN_SECONDS);
        this.type = type;

        runSensor();
    }

    private void runSensor() {

        Timer timer = new Timer();
        TimerTask sensorUpdater = new TimerTask() {

            @Override
            public void run() {

                elapsedTimeInSeconds += SAMPLE_RATE_IN_SECONDS;
                double reading = (type.getRange() / 2)
                        * Math.sin(PERIOD * 2 * Math.PI * elapsedTimeInSeconds)
                        +  (type.getRange() / 2) + type.getMinValue();

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(pathToDataPort + type.ordinal()));
                    writer.write(Double.toString(reading));
                } catch (IOException e) {
                    System.out.println("models.SensorMock.runSensor error: " + e.getMessage());
                }
            }
        };
        timer.scheduleAtFixedRate(sensorUpdater, 0, SAMPLE_RATE_IN_SECONDS * 1000);
    }
}
