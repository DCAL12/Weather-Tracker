package services;

import mock.SensorMock;
import models.Measurement;
import models.Notification;
import models.Sensor;
import models.Threshold;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SensorDAOTest {

    private SensorDAO dao = new SensorDAO();
    private static List<Sensor> testSensors = new ArrayList<>();
    private Notification testNotification = new Notification("test@test.net",
            new Threshold(Threshold.Operator.GREATER_THAN_OR_EQUAL, 10.1f));

    static {
        for (SensorMock.Type type : SensorMock.Type.values()) {
            testSensors.add(new SensorMock(type));
        }
    }

    @Test
    public void testGetSensors() throws Exception {
        List<Sensor> daoSensors = dao.getAllSensors();
        assertTrue("getAllSensors error", daoSensors.containsAll(testSensors));
    }

    @Test
    public void testGetSensorDetails() throws Exception {
        testSensors.forEach( testSensor -> {
            Sensor daoSensor = dao.getSensorDetails(testSensor.getId());
            assertEquals("getSensorDetails error", testSensor, daoSensor);
        });
    }

    @Test
    public void testToggleSensor() throws Exception {
        testSensors.forEach( testSensor -> {
            Sensor daoSensor = dao.getSensorDetails(testSensor.getId());
            boolean initialState = daoSensor.isEnabled();

            dao.toggleSensor(daoSensor.getId());
            boolean finalState = daoSensor.isEnabled();

            assertNotSame("toggleSensor error", initialState, finalState);
        });
    }

    @Test
    public void testRecordData() throws Exception {
        Timer timer = new Timer();

        testSensors.forEach(testSensor -> {
            Sensor daoSensor = dao.getSensorDetails(testSensor.getId());
            List<Measurement> measurements = new ArrayList<>();

            // restart the recording to clear previous data
            dao.toggleSensor(daoSensor.getId());

            if (!daoSensor.isEnabled()) {
                dao.toggleSensor(daoSensor.getId());
            }

            Date recordStartTime = new Date();

            // Delay 2 seconds to ensure some data has been recorded
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    measurements.addAll(dao.getSensorDetails(testSensor.getId()).getMeasurements());
                }
            }, 2000);

            assertTrue("recordData error: no data captured", measurements.size() > 0);
            assertTrue("recordData error: previous data not cleared on start",
                    measurements.get(0).getTimeStamp().after(recordStartTime));
        });
    }

    @Test
    public void testAddNotification() throws Exception {
        testSensors.forEach( testSensor -> {
            Sensor daoSensor = dao.getSensorDetails(testSensor.getId());

            dao.addNotification(daoSensor.getId(), testNotification);
            List<Notification> notifications = dao.getSensorDetails(daoSensor.getId()).getNotifications();

            assertTrue("addNotification error", notifications.contains(testNotification));
        });
    }

    @Test
    public void testDeleteNotification() throws Exception {
        testSensors.forEach( testSensor -> {
            Sensor daoSensor = dao.getSensorDetails(testSensor.getId());
            List<Notification> notifications = daoSensor.getNotifications();

            int testNotificationId = notifications.get(notifications.indexOf(testNotification)).getId();
            dao.deleteNotification(testNotificationId);

            notifications = dao.getSensorDetails(testSensor.getId()).getNotifications();

            assertFalse("addNotification error", notifications.contains(testNotification));
        });
    }
}