package mock;

import models.Observation;
import models.Sensor;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class SensorMock extends Sensor {

    public enum Type {
        TEMPERATURE (16, 38, 3),
        HUMIDITY (10, 90, 1),
        LIGHT (0, 100, 8);

        private final int minValue;
        private final int maxValue;
        private final int variationPercentage;

        Type(int minValue, int maxValue, int variationPercentage) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.variationPercentage = variationPercentage;
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

    public static final int PERIOD = 60 * 60 * 24; // 60 sec * 60 min * 24 hrs = 1 day
    private static final int SAMPLE_RATE_IN_SECONDS = 1;
    private int elapsedTimeInSeconds = 0;
    private Type type;
    private float currentObservation;

    public SensorMock(Type type) {
        super(type.ordinal() + 1, type.toString(), null);
        this.type = type;

        runSensor();
    }

    @Override
    public Observation getObservation() {

        return new Observation(new Timestamp(System.currentTimeMillis()), currentObservation);
    }

    private void runSensor() {

        Timer timer = new Timer();
        TimerTask sensorUpdater = new TimerTask() {

            @Override
            public void run() {

                /*
                * Sample observations modeled as a sine wave function, repeating daily:
                *
                * y = sensor reading with respect to x
                * x = time in seconds
                * r = daily range
                * p = period = 24 hours
                * m = daily minimum reading
                *
                * y = 1/2r * sin(2p*pi*x) + 1/2r + m
                * */

                elapsedTimeInSeconds += SAMPLE_RATE_IN_SECONDS;
                currentObservation = (float) ((type.getRange() / 2)
                                        * Math.sin(PERIOD * 2 * Math.PI * elapsedTimeInSeconds)
                                        +  (type.getRange() / 2) + type.getMinValue());

                // Introduce random variation
                currentObservation += Math.random() * (type.variationPercentage / 100);
            }
        };
        timer.scheduleAtFixedRate(sensorUpdater, 0, SAMPLE_RATE_IN_SECONDS * 1000);
    }
}
