package controllers;

import models.Observation;
import models.Sensor;
import services.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@WebServlet(name = "SetupSensors", urlPatterns = "/setup")
public class SetupSensors extends HttpServlet {

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
        TaskDispatcher task;

        if (observationRecorders.containsKey(sensorID)) {
            task = observationRecorders.get(sensorID);
            task.clearTask();
            observationRecorders.remove(sensorID);
        }
        else {

            Sensor sensor = availableSensors.get(sensorID);
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
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HTMLPageBuilder html = new HTMLPageBuilder(getServletContext(), "/views/setup.html");
        html.setContent("{{title}}", "Setup");

        StringBuilder tableRows = new StringBuilder();
        String sensorRowDetailTemplate = html.readTemplate("/views/sensorDetail.html");

        availableSensors.forEach( (sensor) -> {
            String tableRow = sensorRowDetailTemplate;
            tableRow = tableRow.replace("{{enabled}}",
                    Boolean.toString(observationRecorders.containsKey(sensor.getId())));
            tableRow = tableRow.replace("{{ID}}", Integer.toString(sensor.getId()));
            tableRow = tableRow.replace("{{label}}", sensor.getLabel());
            tableRows.append(tableRow);
        });

        html.setContent("{{sensors}}", tableRows.toString());

        response.setContentType("text/html");
        response.getWriter().println(html);
        response.getWriter().close();
    }
}
