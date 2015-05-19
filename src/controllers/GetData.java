package controllers;

import models.Observation;
import models.Sensor;
import services.ObservationDAO;
import services.SensorDAO;
import util.BuildJSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetData", urlPatterns = "/data")
public class GetData extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SensorDAO sensorDAO = new SensorDAO();
        ObservationDAO observationDAO = new ObservationDAO();
        List<Sensor> sensors = sensorDAO.getSensors();

        sensors.forEach(sensor -> {
            System.out.println(sensor.getLabel());
            System.out.println("JSON:");
            System.out.println(BuildJSON.toJSON(sensor));
            List<Observation> observations = observationDAO.getObservations(sensor.getId());
            if (observations.size() > 0) {
                sensor.setObservations(observations);
            }
        });

        System.out.println("JSON:");
        System.out.println(BuildJSON.toJSON(sensors));
        response.setContentType("application/json");
        response.getWriter().print(BuildJSON.toJSON(sensors));
        response.getWriter().close();
    }
}
