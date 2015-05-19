(function() {

    window.addEventListener("load", main);

    function main() {

        var setupTable = document.getElementById("configureSensors"),
            message = document.getElementById("message");

        var request = new XMLHttpRequest();
        request.open("GET", "/configure");

        message.textContent = "loading...";

        request.onload = function() {

            var sensors = JSON.parse(this.responseText);

            if(sensors.length > 0) {
                sensors.forEach(function (sensor) {

                    var dataRow = document.createElement("tr"),
                        statusElement = document.createElement("td"),
                        toggleButton = document.createElement("button"),
                        labelElement = document.createElement("td");

                    toggleButton.textContent = sensor.enabled ? 'enabled' : 'disabled';
                    toggleButton.onclick = function () {
                        var toggleRequest = new XMLHttpRequest();
                        request.open("POST", "/configure?sensorID=" + sensor.id);
                        toggleButton.setAttribute("class", "processing");
                        toggleButton.textContent = "processing...";

                        //toggleRequest.onload() = function() {
                        //    var changedSensor = JSON.parse(this.responseText);
                        //};
                        toggleRequest.send();
                    };


                    labelElement.textContent = sensor.label;

                    statusElement.appendChild(toggleButton);
                    dataRow.appendChild(statusElement);
                    dataRow.appendChild(labelElement);

                    setupTable.appendChild(dataRow);
                    message.textContent = "";
                });
            }
            else {
                message.textContent = "No sensors were found in the system."
            }
        };
        request.send();
    }
}());