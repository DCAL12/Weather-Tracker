package controllers.triggers;

import models.Trigger;
import models.Threshold;
import services.dataAccess.SensorDAO;
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
        Optional<Trigger> existingTrigger;

        existingTrigger = triggerDAO.getTriggers(sensorID)
                .stream()
                .filter(trigger ->
                        trigger.getSensorID()
                            == requestedTrigger.getSensorID()
                        && trigger.getThreshold().toString()
                            .equals(requestedTrigger.getThreshold().toString())
                )
                .findFirst();

        if (existingTrigger.isPresent()) {
            System.out.println("Trigger already exists");
            triggerDAO.addRecipient(existingTrigger.get().getId(), email);
        }
        else {
            System.out.println("Creating new trigger");
            triggerDAO.addTrigger(sensorID, requestedTrigger, email);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
