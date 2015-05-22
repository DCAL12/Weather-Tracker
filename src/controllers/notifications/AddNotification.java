package controllers.notifications;

import models.Notification;
import models.Threshold;
import services.dataAccess.NotificationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AddNotification", urlPatterns = "/notifications/add")
public class AddNotification extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        NotificationDAO notificationDAO = NotificationDAO.getInstance();

        int sensorID = Integer.parseInt(request.getParameter("sensorID"));
        String operator = request.getParameter("operator");
        float thresholdValue = 10.2f;
        String email = request.getParameter("email");

        Notification requestedNotification =
                new Notification(new Threshold(Threshold.Operator.valueOf(operator), thresholdValue));
        Optional<Notification> existingNotification = null;

        existingNotification = notificationDAO.getNotifications(sensorID)
                .stream()
                .filter(requestedNotification::equals)
                .findFirst();

        if (existingNotification.isPresent()) {
            notificationDAO.addRecipient(existingNotification.get().getId(), email);
        }
        else {
            notificationDAO.addNotification(sensorID, requestedNotification, email);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
