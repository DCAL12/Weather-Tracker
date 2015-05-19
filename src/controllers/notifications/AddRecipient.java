package controllers.notifications;

import services.NotificationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AddRecipient", urlPatterns = "/notifications/recipients/add")
public class AddRecipient extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        NotificationDAO notificationDAO = NotificationDAO.getInstance();

        notificationDAO.addRecipient(
                Integer.parseInt(request.getParameter("notificationID")),
                request.getParameter("email")
        );

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
