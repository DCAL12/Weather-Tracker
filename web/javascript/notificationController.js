(function() {

    window.addEventListener("load", main);

    function main() {

        var notificationsTable = document.getElementById("notifications");
        var request = new XMLHttpRequest();
        request.open("GET", "/notifications/getNotifications");

        request.onload = function() {

            var sensors = this.responseText;

            /*Build Notification Table with notifications grouped by sensor:
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
                    notificationElement = document.createElement("td"),
                    notificationTable = document.createElement("table");

                sensorElement.textContent = sensor.label;
                notificationElement.appendChild(notificationTable);

                sensor.notifications.forEach(function(notification) {
                    var notificationRow = document.createElement("tr"),
                        triggerElement = document.createElement("td"),
                        recipientElement = document.createElement("td"),
                        recipientsList = document.createElement("ul");

                    triggerElement.textContent =
                        notification.threshold.operator +
                        notification.threshold.value;

                    notification.recipients.forEach(function(recipient) {
                        var recipientItem = document.createElement("li");
                        recipientItem.textContent = recipient;
                        recipientsList.appendChild(recipientItem);
                    });
                    recipientElement.appendChild(recipientsList);

                    notificationRow.appendChild(triggerElement);
                    notificationRow.appendChild(recipientElement);
                    notificationTable.appendChild(notificationRow);
                });

                sensorRow.appendChild(sensorElement);
                sensorRow.appendChild(notificationElement);
                notificationsTable.appendChild(sensorRow);
            });
        };
        request.send();
    }
}());