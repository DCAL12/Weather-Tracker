(function() {

    window.addEventListener('load', main);

    function main() {

        var notificationsTable = document.getElementById("triggers"),
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

                sensors.forEach(function (sensor) {

                    var sensorRow = document.createElement("tr"),
                        sensorElement = document.createElement("td"),
                        triggerColumn = document.createElement("td"),
                        triggerTable = document.createElement("table"),
                        recipientColumn = document.createElement("td"),
                        recipientTable = document.createElement("table");

                    triggerTable.id = sensor.id + '.triggers';
                    recipientTable.id = sensor.id + '.recipients';

                    sensorElement.textContent = sensor.label;

                    // Create a table row for each sensor notification
                    if (sensor.notifications) {
                        sensor.notifications.forEach(function (notification) {
                            var triggerRow = document.createElement("tr"),
                                triggerElement = document.createElement("td"),
                                recipientRow = document.createElement("tr"),
                                recipientElement = document.createElement("td"),
                                recipientList = document.createElement("ul");

                            triggerRow.id = 'notification:' + notification.id + '.trigger';
                            recipientRow.id = 'notification:' + notification.id + '.recipients';
                            recipientList.id = 'notification:' + notification.id + '.recipientsList';

                            triggerElement.textContent =
                                notification.threshold.operatorSymbol
                                + " "
                                + notification.threshold.value + " ";
                            triggerElement.insertAdjacentHTML('beforeend',
                                '<span onclick="deleteNotification(sensor.id, notification.id)">X</span>');

                            // create a list for each of the notification's recipients
                            notification.recipients.forEach(function (recipient) {

                                var recipientItem = document.createElement("li"),
                                    deleteOption =
                                        '<span onclick="deleteRecipient(notification.id, recipient)">X</span>';

                                recipientItem.textContent = recipient;
                                recipientItem.id = 'notification:' + notification.id + '-' + recipient;
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
                    notificationsTable.appendChild(sensorRow);
                });
                message.textContent = "";
            }
            else {
                message.textContent = "No sensors were found in the system."
            }
        };
        request.send();
    }


        function deleteRecipient(notificationID, email) {

            var deleteRecipientRequest = new XMLHttpRequest(),
                data = new FormData(),
                recipientList = document.getElementById('notification:' + notificationID + '.recipientsList'),
                recipientElement = document.getElementById('notification:' + notificationID + '-' + email);

            data.append('notificationID', notificationID);
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
        }

        function deleteNotification(sensorID, notificationID) {

            var deleteNotificationRequest = new XMLHttpRequest(),
                data = new FormData(),
                triggerTable = document.getElementById(sensorID + '.triggers'),
                recipientTable = document.getElementById(sensorID + '.recipients'),
                triggerRow = document.getElementById('notification:' + nofificationID + '.trigger'),
                recipientRow = document.getElementById('notification:' + nofificationID + '.recipients');

            data.append('notificationID', notificationID);

            deleteNotificationRequest.open('POST', '/triggers/delete');
            deleteNotificationRequest.onreadystatechange = function () {
                if (deleteNotificationRequest.readyState === 4 && deleteNotificationRequest.status !== 200) {
                    alert('Something went wrong...');
                }
                else if (deleteNotificationRequest.readyState === 4 && deleteNotificationRequest.status === 200) {
                    triggerTable.removeChild(triggerRow);
                    recipientTable.removeChild(recipientRow);
                    alert('notification has been removed');
                }
            };
            deleteNotificationRequest.send(data);
        }
}());

