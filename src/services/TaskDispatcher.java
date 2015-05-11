package services;

import models.Sensor;

import java.util.*;

public class TaskDispatcher {

    private static Timer timer = new Timer();
    private static Hashtable<Integer, TimerTask> sensorRecorders = new Hashtable<>();


    public static void addSensorRecorder(Sensor sensor, SensorDAO dao) {

        if (sensorRecorders.containsKey(sensor.getId())) {
            System.out.println("services.TaskDispatcher.addSensorRecorder error: that task is already running");
            return;
        }

        TimerTask sensorRecorder = new TimerTask() {
            @Override
            public void run() {
                dao.recordData(sensor.getId(), sensor.getCurrentMeasurement());
            }
        };

        sensorRecorders.put(sensor.getId(), sensorRecorder);
        timer.schedule(sensorRecorder, sensor.getSampleRate() * 1000);
    }

    public static void stopRecording(int sensorID) {
        TimerTask sensorRecorder = sensorRecorders.get(sensorID);
        if (sensorRecorder != null) {
            sensorRecorder.cancel();
            sensorRecorders.remove(sensorID);
        }
    }
}
