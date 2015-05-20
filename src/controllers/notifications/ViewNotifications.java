package controllers.notifications;

import util.HTMLPageBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ManageNotifications", urlPatterns = "/notifications")
public class ViewNotifications extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HTMLPageBuilder html = new HTMLPageBuilder(getServletContext(), "/views/notifications.html");
        html.setContent("{{title}}", "Manage Notifications");

        response.setContentType("text/html");
        response.getWriter().println(html);
        response.getWriter().close();
    }
}