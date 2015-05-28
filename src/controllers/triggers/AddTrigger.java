package controllers.triggers;

import models.Trigger;
import models.Threshold;
import services.dataAccess.TriggerDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AddTrigger", urlPatterns = "/triggers/add")
public class AddTrigger extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TriggerDAO triggerDAO = TriggerDAO.getInstance();

        int sensorID = Integer.parseInt(request.getParameter("sensorID"));
        String operator = request.getParameter("operator");
        float thresholdValue = Float.parseFloat(request.getParameter("thresholdValue"));
        String email = request.getParameter("email");

        Trigger requestedTrigger =
                new Trigger(sensorID,
                        new Threshold(Threshold.Operator.valueOf(operator), thresholdValue));
        Optional<Trigger> existingNotification;

        existingNotification = triggerDAO.getTriggers(sensorID)
                .stream()
                .filter(notification ->
                        notification.getSensorID()
                            == requestedTrigger.getSensorID()
                        && notification.getThreshold()
                            .equals(requestedTrigger.getThreshold())
                )
                .findFirst();

        if (existingNotification.isPresent()) {
            triggerDAO.addRecipient(existingNotification.get().getId(), email);
        }
        else {
            triggerDAO.addTrigger(sensorID, requestedTrigger, email);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
