'use strict';
var triggerTable = function() {

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
                 * sensor        trigger     recipients
                 * temperature   <5          bob@home.net
                 *                           bill@home.net
                 *
                 *               >=30        bob@home.net
                 */

                sensors.forEach(function(sensor) {

                    var sensorRow = document.createElement("tr"),
                        sensorElement = document.createElement("td"),
                        triggerColumn = document.createElement("td"),
                        triggerTable = document.createElement("table"),
                        recipientColumn = document.createElement("td"),
                        recipientTable = document.createElement("table");

                    triggerTable.id = sensor.id + '.triggers';
                    recipientTable.id = sensor.id + '.recipients';

                    sensorElement.textContent = sensor.label;

                    // Create a table row for each sensor trigger
                    if (sensor.triggers) {
                        sensor.triggers.forEach(function(trigger) {
                            var triggerRow = document.createElement("tr"),
                                triggerElement = document.createElement("td"),
                                recipientRow = document.createElement("tr"),
                                recipientElement = document.createElement("td"),
                                recipientList = document.createElement("ul");

                            triggerRow.id = 'trigger:' + trigger.id + '.trigger';
                            recipientRow.id = 'trigger:' + trigger.id + '.recipients';
                            recipientList.id = 'trigger:' + trigger.id + '.recipientsList';

                            triggerElement.textContent =
                                trigger.threshold.operatorSymbol
                                + " "
                                + trigger.threshold.value + " ";
                            triggerElement.insertAdjacentHTML('beforeend',
                                '<button onclick="triggerTable.deleteTrigger(sensor.id, trigger.id)">x</button>');

                            // create a list for each of the trigger's recipients
                            trigger.recipients.forEach(function(recipient) {

                                var recipientItem = document.createElement("li"),
                                    deleteOption =
                                        '<button onclick="triggerTable.deleteRecipient(trigger.id, recipient)">x</button>';

                                recipientItem.textContent = recipient;
                                recipientItem.id = 'trigger:' + trigger.id + '-' + recipient;
                                recipientItem.insertAdjacentHTML('beforeend', deleteOption);
                                recipientList.appendChild(recipientItem);
                            });
                            recipientElement.appendChild(recipientList);
                            recipientRow.appendChild(recipientElement);
                            recipientTable.appendChild(recipientRow);
                            recipientColumn.appendChild(recipientTable);

                            triggerRow.appendChild(triggerElement);
                            triggerTable.appendChild(triggerRow);
                            triggerColumn.appendChild(triggerTable);
                        });
                    }
                    else {
                       triggerColumn.textContent = "none defined";
                    }

                    sensorRow.appendChild(sensorElement);
                    sensorRow.appendChild(triggerColumn);
                    sensorRow.appendChild(recipientColumn);
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

    return {
        deleteRecipient: function(triggerID, email) {
            console.log('deleteRecipient');
            var deleteRecipientRequest = new XMLHttpRequest(),
                data = new FormData(),
                recipientList = document.getElementById('trigger:' + triggerID + '.recipientsList'),
                recipientElement = document.getElementById('trigger:' + triggerID + '-' + email);

            data.append('triggerID', triggerID);
            data.append('email', email);

            deleteRecipientRequest.open('POST', '/triggers/recipients/delete');
            deleteRecipientRequest.onreadystatechange = function () {
                if (deleteRecipientRequest.readyState === 4 && deleteRecipientRequest.status !== 200) {
                    alert('Something went wrong...');
                }
                else if (deleteRecipientRequest.readyState === 4 && deleteRecipientRequest.status === 200) {
                    recipientList.removeChild(recipientElement);
                    alert(email + ' has been removed');
                }
            };
            deleteRecipientRequest.send(data);
        },

        deleteTrigger: function(sensorID, triggerID) {
            console.log('deleteTrigger');
            var deleteTriggerRequest = new XMLHttpRequest(),
                data = new FormData(),
                triggerTable = document.getElementById(sensorID + '.triggers'),
                recipientTable = document.getElementById(sensorID + '.recipients'),
                triggerRow = document.getElementById('trigger:' + triggerID + '.trigger'),
                recipientRow = document.getElementById('trigger:' + triggerID + '.recipients');

            data.append('triggerID', triggerID);

            deleteTriggerRequest.open('POST', '/triggers/delete');
            deleteTriggerRequest.onreadystatechange = function () {
                if (deleteTriggerRequest.readyState === 4 && deleteTriggerRequest.status !== 200) {
                    alert('Something went wrong...');
                }
                else if (deleteTriggerRequest.readyState === 4 && deleteTriggerRequest.status === 200) {
                    triggerTable.removeChild(triggerRow);
                    recipientTable.removeChild(recipientRow);
                    alert('trigger has been removed');
                }
            };
            deleteTriggerRequest.send(data);
        }
    }
}();

