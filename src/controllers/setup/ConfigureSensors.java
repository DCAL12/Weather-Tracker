package controllers.setup;

import models.Observation;
import models.Sensor;
import services.*;
import util.BuildJSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@WebServlet(name = "ConfigureSensors", urlPatterns = "/configure")
public class ConfigureSensors extends HttpServlet {

    private static final long OBSERVATION_SAMPLE_INTERVAL = 1000 * 5; // in milliseconds

    private static SensorDAO sensorDAO = new SensorDAO();
    private static ObservationDAO observationDAO = new ObservationDAO();
    private static NotificationDAO notificationDAO = new NotificationDAO();

    private static List<Sensor> availableSensors = new ArrayList<>();
    private static Hashtable<Integer, TaskDispatcher> observationRecorders = new Hashtable<>();

    static {
        List<Sensor> sensors = sensorDAO.getSensors();
        sensors.forEach(availableSensors::add);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int sensorID = Integer.parseInt(request.getParameter("sensorID"));
        Sensor sensor = availableSensors.get(sensorID);
        TaskDispatcher task;

        if (sensor.isEnabled()) {
            // stop sensor recording
            task = observationRecorders.get(sensorID);
            task.clearTask();
            observationRecorders.remove(sensorID);
        }
        else {
            // start sensor recording
            task = new TaskDispatcher( () -> {

                // Record observations at OBSERVATION_SAMPLE_INTERVAL
                Observation observation = sensor.getObservation();
                observationDAO.addObservation(sensorID, observation);

                // Trigger new, valid notification alerts
                Notifier.processNotifications(
                        notificationDAO.getNotifications(sensorID),
                        notification -> notification.getThreshold().isExceeded(observation.getValue()),
                        validNotification -> validNotification
                                .getRecipients()
                                .forEach(recipient ->
                                        Notifier.sendAlert(validNotification, sensor.getLabel(), observation))
                        );
            }, OBSERVATION_SAMPLE_INTERVAL);

            observationRecorders.put(sensorID, task);
        }
        sensor.toggle();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.getWriter().print(BuildJSON.toJSON(availableSensors));
        response.getWriter().close();
    }
}
