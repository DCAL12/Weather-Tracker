package mock;

import models.Observation;
import models.Sensor;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class SensorMock extends Sensor {

    public enum Type {
        TEMPERATURE (16, 38, 30),
        HUMIDITY (10, 90, 20),
        LIGHT (0, 100, 25);

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

    private static final int PERIOD_SECONDS = 86400/24/60; //86,400 seconds = 1 day
    private static final int SAMPLE_RATE_MILLISECONDS = 1000 * 1;
    private long elapsedSeconds = 0;
    private Type type;
    private float currentObservation;

    public SensorMock(Type type) {
        super(type.ordinal(), type.toString().toLowerCase(), null);
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

            float periodMin = type.minValue;
            float periodMax = type.maxValue;

            @Override
            public void run() {

                /*
                * Sample observations modeled as a sine wave function, repeating daily:
                *
                * y = sensor reading with respect to x
                * x = time in seconds
                * r = daily range
                * p = period
                * m = daily minimum reading
                *
                * y = 1/2r * sin(2*pi*x/p) + 1/2r + m
                * */

                if (elapsedSeconds % PERIOD_SECONDS == 0) {

                    periodMin = type.minValue + type.minValue
                            * (float) (Math.random() * (float) type.variationPercentage/100);
                    periodMax = type.maxValue - type.maxValue
                            * (float) (Math.random() * (float) type.variationPercentage/100);
                }

                elapsedSeconds += (float) SAMPLE_RATE_MILLISECONDS / 1000;
                currentObservation = (float)
                        (0.5 * (periodMax - periodMin)
                        * Math.sin(2 * Math.PI * (float) elapsedSeconds / PERIOD_SECONDS)
                        + 0.5 * (periodMax - periodMin) + periodMin);
            }
        };
        timer.schedule(sensorUpdater, 0, SAMPLE_RATE_MILLISECONDS);
    }
}
