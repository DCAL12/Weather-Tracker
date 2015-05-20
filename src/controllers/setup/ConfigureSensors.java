package controllers.setup;

import models.Observation;
import models.Sensor;
import services.Notifier;
import services.dataAccess.NotificationDAO;
import services.dataAccess.ObservationDAO;
import services.dataAccess.SensorDAO;
import util.BuildJSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "ConfigureSensors", urlPatterns = "/configure")
public class ConfigureSensors extends HttpServlet {

    private static final long OBSERVATION_SAMPLE_INTERVAL = 1000 * 3; // in milliseconds

    private static SensorDAO sensorDAO = SensorDAO.getInstance();
    private static ObservationDAO observationDAO = ObservationDAO.getInstance();
    private static NotificationDAO notificationDAO = NotificationDAO.getInstance();

    private static Hashtable<Integer, Sensor> availableSensors = new Hashtable<>();
    private static Hashtable<Integer, TimerTask> observationRecorders = new Hashtable<>();
    private static Timer timer = new Timer();

    static {
        sensorDAO.getSensors()
                .forEach(sensor -> availableSensors.put(sensor.getId(), sensor));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int sensorID = Integer.parseInt(request.getParameter("sensorID"));
        Sensor sensor = availableSensors.get(sensorID);

        System.out.println("Toggle Sensor: " + sensor.getLabel());

        if (sensor.isEnabled()) {
            // stop sensor recording
            observationRecorders.get(sensorID).cancel();
            observationRecorders.remove(sensorID);
        }
        else {
            // start sensor recording
            TimerTask task = new TimerTask(){
                @Override
                public void run() {
                    // Record observations at OBSERVATION_SAMPLE_INTERVAL
                    Observation observation = sensor.getObservation();
                    observationDAO.addObservation(sensorID, observation);
                    System.out.println(observation.getValue());

                    // Trigger new, valid notification alerts
                Notifier.processNotifications(
                        notificationDAO.getNotifications(sensorID),
                        notification -> notification.getThreshold().isExceeded(observation.getValue()),
                        validNotification -> validNotification
                                .getRecipients()
                                .forEach(recipient ->
                                        Notifier.sendAlert(validNotification, sensor.getLabel(), observation))
                );
                }
            };
            timer.schedule(task, 0, OBSERVATION_SAMPLE_INTERVAL);
            observationRecorders.put(sensorID, task);
        }
        sensor.toggle();

        response.setContentType("application/json");
        response.getWriter().print("{'status': 'success'}");
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.getWriter().print(BuildJSON.toJSON(availableSensors.values()));
        response.getWriter().close();
    }
}
