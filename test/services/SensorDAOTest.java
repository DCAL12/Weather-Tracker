package services;

import models.Sensor;
import org.junit.Test;
import services.dataAccess.SensorDAO;

import java.util.*;

import static org.junit.Assert.*;

public class SensorDAOTest {

    private SensorDAO dao = SensorDAO.getInstance();
    private static List<Sensor> testSensors = new ArrayList<>();

    static {
        // Test Sensors in Database
        testSensors.add(new Sensor(1, "temperature", "1"));
        testSensors.add(new Sensor(2, "humidity", "2"));
        testSensors.add(new Sensor(3, "light", "3"));
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