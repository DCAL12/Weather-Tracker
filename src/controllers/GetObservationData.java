package controllers;

import models.Observation;
import models.Sensor;
import services.dataAccess.ObservationDAO;
import services.dataAccess.SensorDAO;
import util.BuildJSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetData", urlPatterns = "/data")
public class GetObservationData extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SensorDAO sensorDAO = SensorDAO.getInstance();
        ObservationDAO observationDAO = ObservationDAO.getInstance();
        List<Sensor> sensors = sensorDAO.getSensors();

        sensors.forEach(sensor -> {
            List<Observation> observations = observationDAO.getObservations(sensor.getId());
            if (observations.size() > 0) {
                sensor.setObservations(observations);
            }
        });
        response.setContentType("application/json");
        response.getWriter().print(BuildJSON.toJSON(sensors));
        response.getWriter().close();
    }
}
