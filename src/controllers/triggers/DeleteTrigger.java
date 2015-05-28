package controllers.triggers;

import services.dataAccess.TriggerDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteTrigger", urlPatterns = "/triggers/delete")
public class DeleteTrigger extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TriggerDAO triggerDAO = TriggerDAO.getInstance();
        triggerDAO.deleteTrigger(Integer.parseInt(request.getParameter("notificationID")));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
