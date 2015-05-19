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
public class GetConfiguration extends HttpServlet {

    private static SensorDAO sensorDAO = new SensorDAO();
    private static List<Sensor> availableSensors = new ArrayList<>();

    static {
        List<Sensor> sensors = sensorDAO.getSensors();
        sensors.forEach(availableSensors::add);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.getWriter().print(BuildJSON.toJSON(availableSensors));
        response.getWriter().close();
    }
}
