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

@WebServlet(name = "UpdateNotification", urlPatterns = "/notifications/update")
public class UpdateNotification extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        NotificationDAO notificationDAO = NotificationDAO.getInstance();

        notificationDAO.updateNotification(
                Integer.parseInt(request.getParameter("notificationID")),
                new Notification(
                        new Threshold(
                                Threshold.Operator.valueOf(request.getParameter("operator")),
                                Float.parseFloat(request.getParameter("threshold"))
                        )
                )
        );
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
