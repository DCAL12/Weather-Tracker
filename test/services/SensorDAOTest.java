package services;

import mock.SensorMock;
import models.Observation;
import models.Notification;
import models.Sensor;
import models.Threshold;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SensorDAOTest {

    private SensorDAO dao = new SensorDAO();
    private static List<Sensor> testSensors = new ArrayList<>();

    static {
        for (SensorMock.Type type : SensorMock.Type.values()) {
            testSensors.add(new SensorMock(type));
        }
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