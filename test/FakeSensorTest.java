import mock.SensorMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FakeSensorTest {

    double errorMargin = 0.001;
    static int sampleRateInSeconds = 1;
    static List<SensorMock> sensorMocks = new ArrayList<>();

    static {
        for (SensorMock.Type parameter : SensorMock.Type.values()) {
            sensorMocks.add(new SensorMock(parameter));
        }
    }

    @Test
    public void getConsistentMeasurements() throws Exception {

        sensorMocks.forEach((testSensor) -> {
            double initialMeasurement = testSensor.getMeasurement();
            double finalMeasurement = 0;

            for(int i = 0; i< SensorMock.SECONDS_PER_DAY; i++) {
                finalMeasurement = testSensor.getMeasurement();
            }
            assertEquals(testSensor.getType() + ": measurements between days do not match",
                    initialMeasurement, finalMeasurement, errorMargin);
        });
    }

    @Test
    public void getMaxMeasurements() throws Exception {

        sensorMocks.forEach((testSensor) -> {
            double measurement = testSensor.getMeasurement();

            for(int i = 0; i< SensorMock.SECONDS_PER_DAY; i++) {
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

        sensorMocks.forEach((testSensor) -> {
            double measurement = testSensor.getMeasurement();

            for(int i = 0; i< SensorMock.SECONDS_PER_DAY; i++) {
                double nextMeasurement = testSensor.getMeasurement();
                measurement = nextMeasurement < measurement ?
                        nextMeasurement : measurement;
            }
            assertTrue(testSensor.getType() + "Measurement exceeded daily min", measurement >=
                    testSensor.getType().getMinValue() - errorMargin);
        });
    }
}