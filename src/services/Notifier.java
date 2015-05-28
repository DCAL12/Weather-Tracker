package services;

import models.Trigger;
import models.Observation;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Notifier {

    private static List<Trigger> activeTriggers = new ArrayList<>();

    public static void processNotifications(List<Trigger> triggers,
                              Predicate<Trigger> validator,
                              Consumer<Trigger> processor) {

        triggers.forEach( notification -> {

            if (validator.test(notification) && !activeTriggers.contains(notification)) {

                // Trigger is valid and new
                processor.accept(notification);
                activeTriggers.add(notification);
            }

            else if (activeTriggers.contains(notification)) {
                activeTriggers.remove(notification);
            }
        });
    }

    public static void sendAlert(Trigger trigger, String label, Observation observation) {

        String message = String.format(
                "Weather Tracker Alert:\n " +
                "%s reported a value of %.1f.\n\n" +
                "You receive an alert when %s reports %s%.1f.",
                label, observation.getValue(),
                label, trigger.getThreshold().getOperator(),
                trigger.getThreshold().getValue());

        try {
            SmtpMessage.sendEmail(trigger.getRecipients(), "Weather Tracker Alert", message);
        } catch (MessagingException e) {
            System.out.println("services.Notifier.sendAlert Message Error" + e.getMessage());
        }
    }
}
