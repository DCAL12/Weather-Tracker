package models;

public class TestSensor implements Sensor {

    public enum WeatherParameter {
        TEMPERATURE (16, 38),
        HUMIDITY (10, 90),
        LIGHT (0, 100);

        private final int minValue;
        private final int maxValue;

        WeatherParameter(int minValue, int maxValue) {
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

    public static final int SECONDS_PER_DAY = 60 * 60 * 24;

    private WeatherParameter type;
    private String id;
    private int sampleRateInSeconds;
    private int elapsedTimeInSeconds = 0;

    public TestSensor(WeatherParameter type) {
        this.type = type;
    }

    public void enable(int sampleRateInSeconds) {
        this.sampleRateInSeconds = sampleRateInSeconds;
    }

    public void disable() {
        sampleRateInSeconds = 0;
    }

    public boolean isEnabled() {
        return (sampleRateInSeconds != 0);
    }

    public WeatherParameter getType() {
        return type;
    }

    public double getMeasurement() {

        elapsedTimeInSeconds += sampleRateInSeconds;
        return (type.getRange() / 2)
                * Math.sin(SECONDS_PER_DAY * 2 * Math.PI * elapsedTimeInSeconds)
                +  (type.getRange() / 2) + type.getMinValue();
    }

    @Override
    public String toString() {
        String delimiter = " ";
        return (
            Boolean.toString(isEnabled()) + delimiter +
            id + delimiter +
            type
        );
    }
}
