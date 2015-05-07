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

    private WeatherParameter type;
    static final int SECONDS_PER_DAY = 60 * 60 * 24;
    private int sampleRateInSeconds;
    private int elapsedTimeInSeconds = 0;

    public TestSensor(WeatherParameter type, int sampleRateInSeconds) {
        this.type = type;
        this.sampleRateInSeconds = sampleRateInSeconds;
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
}
