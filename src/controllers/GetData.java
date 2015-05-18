package controllers;

import com.google.gson.Gson;
import models.Sensor;
import services.ObservationDAO;
import services.SensorDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetData", urlPatterns = "/data")
public class GetData extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SensorDAO sensorDAO = new SensorDAO();
        ObservationDAO observationDAO = new ObservationDAO();
        List<Sensor> sensors = sensorDAO.getSensors();

        sensors.forEach(sensor -> sensor.setObservations(observationDAO.getObservations(sensor.getId())));

        response.setContentType("application/json");
        Gson gson = new Gson();
        response.getWriter().print(gson.toJson(sensors));
        response.getWriter().close();
    }
}
