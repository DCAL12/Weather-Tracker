(function() {

    window.addEventListener("load", main);

    function main() {

        var notificationsTable = document.getElementById("notifications"),
            message = document.getElementById("message");

        var request = new XMLHttpRequest();
        request.open("GET", "/notifications/getNotifications");

        message.textContent = "loading...";

        request.onload = function() {

            var sensors = JSON.parse(this.responseText);
            if(sensors.length > 0) {

                /*Build Notification Table with notifications grouped by sensor:
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

                    sensorElement.textContent = sensor.label;

                    sensor.notifications.forEach(function (notification) {
                        var triggerRow = document.createElement("tr"),
                            triggerElement = document.createElement("td"),
                            recipientRow = document.createElement("tr"),
                            recipientElement = document.createElement("td"),
                            recipientList = document.createElement("ul");

                        triggerElement.textContent =
                            notification.threshold.operatorSymbol
                            + " "
                            + notification.threshold.value;

                        notification.recipients.forEach(function (recipient) {
                            var recipientItem = document.createElement("li");
                            recipientItem.textContent = recipient;
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
}());