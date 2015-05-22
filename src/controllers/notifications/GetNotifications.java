package controllers.notifications;

import models.Sensor;
import services.dataAccess.NotificationDAO;
import services.dataAccess.SensorDAO;
import util.BuildJSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetNotifications", urlPatterns = "/notifications/getNotifications")
public class GetNotifications extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        NotificationDAO notificationDAO = NotificationDAO.getInstance();
        SensorDAO sensorDAO = SensorDAO.getInstance();

        List<Sensor> sensors = sensorDAO.getSensors();
        sensors.forEach(sensor -> sensor.setNotifications(notificationDAO.getNotifications(sensor.getId())));

        response.setContentType("application/json");
        System.out.println(BuildJSON.toJSON(sensors));
        response.getWriter().print(BuildJSON.toJSON(sensors));
        response.getWriter().close();

    }
}
