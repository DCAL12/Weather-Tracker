package services;

import models.Trigger;
import models.Observation;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Notifier {

    private List<Trigger> activeTriggers = new ArrayList<>();

    public void processNotifications(List<Trigger> triggers,
                              Predicate<Trigger> validator,
                              Consumer<Trigger> processor) {

        triggers.forEach( trigger -> {

            Optional<Trigger> existingActiveTrigger;

            existingActiveTrigger = activeTriggers
                    .stream()
                    .filter(activeTrigger ->
                                    activeTrigger.getSensorID()
                                            == trigger.getSensorID()
                                        && activeTrigger.getThreshold().toString()
                                            .equals(trigger.getThreshold().toString())
                    )
                    .findFirst();

            if (validator.test(trigger) && !existingActiveTrigger.isPresent()) {

                // Trigger is valid and new
                processor.accept(trigger);
                activeTriggers.add(trigger);
            }

            else if (existingActiveTrigger.isPresent()) {
                activeTriggers.remove(trigger);
            }
        });
    }

    public void sendAlert(Trigger trigger, String label, Observation observation) {

        String message = String.format(
                "Weather Tracker Alert:\n " +
                "%s reported a value of %.1f.\n\n" +
                "You receive an alert when %s reports %s",
                label, observation.getValue(),
                label, trigger.getThreshold(),
                trigger.getThreshold().getValue());

        try {
            SmtpMessage.sendEmail(trigger.getRecipients(), "Weather Tracker Alert", message);
        } catch (MessagingException e) {
            System.out.println("services.Notifier.sendAlert Message Error" + e.getMessage());
        }
    }
}
