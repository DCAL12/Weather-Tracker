package services;

import models.Sensor;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

public class SensorDAOTest {

    private SensorDAO dao = new SensorDAO();
    private static List<Sensor> testSensors = new ArrayList<>();

    static {
        // Test Sensors in Database
        testSensors.add(new Sensor(1, "temperature", Paths.get("1")));
        testSensors.add(new Sensor(2, "humidity", Paths.get("2")));
        testSensors.add(new Sensor(3, "light", Paths.get("3")));
    }

    @Test
    public void testGetSensors() throws Exception {
        List<Sensor> daoSensors = dao.getSensors();
        assertTrue("getSensors error", daoSensors.size() > 0);
    }

    @Test
    public void testGetSensor() throws Exception {
        testSensors.forEach( testSensor -> {
            Sensor daoSensor = dao.getSensor(testSensor.getId());
            assertTrue("getSensor error", testSensor.getLabel().equals(daoSensor.getLabel()));
        });
    }
}