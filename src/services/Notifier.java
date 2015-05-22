package services;

import models.Notification;
import models.Observation;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Notifier {

    private static List<Notification> activeNotifications = new ArrayList<>();

    public static void processNotifications(List<Notification> notifications,
                              Predicate<Notification> validator,
                              Consumer<Notification> processor) {

        notifications.forEach( notification -> {

            if (validator.test(notification) && !activeNotifications.contains(notification)) {

                // Notification is valid and new
                processor.accept(notification);
                activeNotifications.add(notification);
            }

            else if (activeNotifications.contains(notification)) {
                activeNotifications.remove(notification);
            }
        });
    }

    public static void sendAlert(Notification notification, String label, Observation observation) {

        String message = String.format(
                "Weather Tracker Alert:\n " +
                "%s reported a value of %.1f.\n\n" +
                "You receive an alert when %s reports %s%.1f.",
                label, observation.getValue(),
                label, notification.getThreshold().getOperator(),
                notification.getThreshold().getValue());

        try {
            SmtpMessage.sendEmail(notification.getRecipients(), "Weather Tracker Alert", message);
        } catch (MessagingException e) {
            System.out.println("services.Notifier.sendAlert Message Error" + e.getMessage());
        }
    }
}
