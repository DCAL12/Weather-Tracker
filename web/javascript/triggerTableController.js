'use strict';
(function() {

    window.addEventListener('load', main);

    function main() {

        var triggersTable = document.getElementById("triggers"),
            message = document.getElementById("message");

        var request = new XMLHttpRequest();
        request.open('GET', '/triggers/getTriggers');

        message.textContent = "loading...";

        request.onload = function() {

            var sensors = JSON.parse(this.responseText);
            if(sensors.length > 0) {

                /*Build Trigger Table with triggers grouped by sensor:
                 *
                 * sensor        notification settings
                 * temperature   <5
                 *                bob@home.net
                 *                bill@home.net
                 *
                 *               >=30
                 *                bob@home.net
                 */

                sensors.forEach(function(sensor) {

                    var sensorRow = document.createElement("tr"),
                        sensorElement = document.createElement("td"),
                        notificationElement = document.createElement("td");

                    notificationElement.id = "sensor:" + sensor.id;
                    sensorElement.textContent = sensor.label;

                    // Create a table for each sensor trigger
                    if (sensor.triggers) {
                        sensor.triggers.forEach(function(trigger) {
                            var notificationTable = document.createElement("table"),
                                triggerHeaderRow = document.createElement("tr"),
                                triggerHeaderElement = document.createElement("th"),
                                deleteTriggerElement = document.createElement("th"),
                                deleteTriggerButton = document.createElement("button");

                            notificationTable.id = "trigger:" + trigger.id;
                            triggerHeaderElement.textContent =
                                trigger.threshold.operatorSymbol
                                + " "
                                + trigger.threshold.value + " ";

                            deleteTriggerButton.textContent = "x";
                            deleteTriggerButton.onclick = function() {
                                deleteTrigger(trigger);
                            };

                            triggerHeaderRow.appendChild(triggerHeaderElement);
                            deleteTriggerElement.appendChild(deleteTriggerButton);
                            triggerHeaderRow.appendChild(deleteTriggerElement);
                            notificationTable.appendChild(triggerHeaderRow);

                            // create a list of each of the trigger's recipients
                            if (trigger.recipients) {
                                trigger.recipients.forEach(function(recipient) {

                                    var recipientRow = document.createElement("tr"),
                                        recipientElement = document.createElement("td"),
                                        deleteRecipientElement = document.createElement("td"),
                                        deleteRecipientButton = document.createElement("button");

                                    recipientRow.id = "recipient:" + trigger.id + "-" + recipient;
                                    recipientElement.textContent = recipient;
                                    deleteRecipientButton.textContent = "x";
                                    deleteRecipientButton.onclick = function() {
                                        deleteRecipient(trigger.id, recipient);
                                    };

                                    recipientRow.appendChild(recipientElement);
                                    deleteRecipientElement.appendChild(deleteRecipientButton);
                                    recipientRow.appendChild(deleteRecipientElement);
                                    notificationTable.appendChild(recipientRow);
                                });
                            }
                            notificationElement.appendChild(notificationTable);
                        });
                    }
                    else {
                       notificationElement.textContent = "none defined";
                    }

                    sensorRow.appendChild(sensorElement);
                    sensorRow.appendChild(notificationElement);
                    triggersTable.appendChild(sensorRow);
                });
                message.textContent = "";
            }
            else {
                message.textContent = "No sensors were found in the system."
            }
        };
        request.send();
    }
    function deleteRecipient(triggerID, email) {
        console.log('deleteRecipient');
        var deleteRecipientRequest = new XMLHttpRequest(),
            recipientRow = document.getElementById("recipient:" + triggerID + "-" + email),
            notificationTable = document.getElementById("trigger:" + triggerID);

        deleteRecipientRequest.open('GET', '/triggers/recipients/delete?triggerID=' + triggerID + '&email=' + email);
        deleteRecipientRequest.onreadystatechange = function () {
            if (deleteRecipientRequest.readyState === 4 && deleteRecipientRequest.status !== 200) {
                alert('Something went wrong...');
            }
            else if (deleteRecipientRequest.readyState === 4 && deleteRecipientRequest.status === 200) {
                notificationTable.removeChild(recipientRow);
                alert(email + ' has been removed');
            }
        };
        deleteRecipientRequest.send();
    }

    function deleteTrigger(trigger) {
        console.log('deleteTrigger: ');
        console.log(trigger);
        var deleteTriggerRequest = new XMLHttpRequest(),
            notificationTable = document.getElementById("trigger:" + trigger.id),
            notificationElement = document.getElementById("sensor:" + trigger.sensorID);

        deleteTriggerRequest.open('GET', '/triggers/delete?triggerID=' + trigger.id);
        deleteTriggerRequest.onreadystatechange = function () {
            if (deleteTriggerRequest.readyState === 4 && deleteTriggerRequest.status !== 200) {
                alert('Something went wrong...');
            }
            else if (deleteTriggerRequest.readyState === 4 && deleteTriggerRequest.status === 200) {
                notificationElement.removeChild(notificationTable);
                alert('trigger has been removed');
            }
        };
        deleteTriggerRequest.send();
    }

}());

