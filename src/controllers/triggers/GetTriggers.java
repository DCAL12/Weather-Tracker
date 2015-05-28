package controllers.triggers;

import models.Sensor;
import services.dataAccess.TriggerDAO;
import services.dataAccess.SensorDAO;
import util.BuildJSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetTriggers", urlPatterns = "/triggers/getTriggers")
public class GetTriggers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TriggerDAO triggerDAO = TriggerDAO.getInstance();
        SensorDAO sensorDAO = SensorDAO.getInstance();

        List<Sensor> sensors = sensorDAO.getSensors();
        sensors.forEach(sensor -> sensor.setTriggers(triggerDAO.getTriggers(sensor.getId())));

        response.setContentType("application/json");
        response.getWriter().print(BuildJSON.toJSON(sensors));
        response.getWriter().close();

    }
}
