import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FakeSensorTest {

    double errorMargin = 0.001;
    static int sampleRateInSeconds = 1;
    static List<TestSensor> testSensors = new ArrayList<>();

    static {
        for (TestSensor.WeatherParameter parameter : TestSensor.WeatherParameter.values()) {
            testSensors.add(new TestSensor(parameter, sampleRateInSeconds));
        }
    }

    @Test
    public void getConsistentMeasurements() throws Exception {

        testSensors.forEach((testSensor) -> {
            double initialMeasurement = testSensor.getMeasurement();
            double finalMeasurement = 0;

            for(int i = 0; i<TestSensor.SECONDS_PER_DAY; i++) {
                finalMeasurement = testSensor.getMeasurement();
            }
            assertEquals(testSensor.getType() + ": measurements between days do not match",
                    initialMeasurement, finalMeasurement, errorMargin);
        });
    }

    @Test
    public void getMaxMeasurements() throws Exception {

        testSensors.forEach((testSensor) -> {
            double measurement = testSensor.getMeasurement();

            for(int i = 0; i<TestSensor.SECONDS_PER_DAY; i++) {
                double nextMeasurement = testSensor.getMeasurement();
                measurement = nextMeasurement > measurement ?
                        nextMeasurement : measurement;
            }
            assertTrue(testSensor.getType() + ": measurement exceeded daily max", measurement <=
                    testSensor.getType().getMaxValue() + errorMargin);
        });
    }

    @Test
    public void getMinMeasurements() throws Exception {

        testSensors.forEach((testSensor) -> {
            double measurement = testSensor.getMeasurement();

            for(int i = 0; i<TestSensor.SECONDS_PER_DAY; i++) {
                double nextMeasurement = testSensor.getMeasurement();
                measurement = nextMeasurement < measurement ?
                        nextMeasurement : measurement;
            }
            assertTrue(testSensor.getType() + "Measurement exceeded daily min", measurement >=
                    testSensor.getType().getMinValue() - errorMargin);
        });
    }
}