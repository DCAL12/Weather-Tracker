package controllers.notifications;

import com.google.gson.Gson;
import models.Sensor;
import services.NotificationDAO;
import services.SensorDAO;

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

        NotificationDAO notificationDAO = new NotificationDAO();
        SensorDAO sensorDAO = new SensorDAO();

        List<Sensor> sensors = sensorDAO.getSensors();
        sensors.forEach(sensor -> sensor.setNotifications(notificationDAO.getNotifications(sensor.getId())));

        response.setContentType("application/json");
        Gson gson = new Gson();
        response.getWriter().print(gson.toJson(sensors));
        response.getWriter().close();

    }
}
