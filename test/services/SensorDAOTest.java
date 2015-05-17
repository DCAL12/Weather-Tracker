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
        testSensors.add(new Sensor(1, "temperature", Paths.get("port"), 1000));
    }

    @Test
    public void testGetSensors() throws Exception {
        List<Sensor> daoSensors = dao.getSensors();
        System.out.println(daoSensors);
        System.out.println(testSensors);
        assertTrue("getSensors error", daoSensors.equals(testSensors));
    }

    @Test
    public void testGetSensor() throws Exception {
        testSensors.forEach( testSensor -> {
            Sensor daoSensor = dao.getSensor(testSensor.getId());
            assertTrue("getSensor error", daoSensor.equals(testSensor));
        });
    }
}