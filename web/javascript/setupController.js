(function() {

    window.addEventListener("load", main);

    function main() {

        var setupTable = document.getElementById("configureSensors"),
            message = document.getElementById("message");

        var sensorConfigurationRequest = new XMLHttpRequest();
        sensorConfigurationRequest.open("GET", "/configure");

        message.textContent = "loading...";

        sensorConfigurationRequest.onload = function() {

            var sensors = JSON.parse(this.responseText);

            if(sensors.length > 0) {
                sensors.forEach(function (sensor) {

                    var dataRow = document.createElement("tr"),
                        statusElement = document.createElement("td"),
                        toggleButton = document.createElement("button"),
                        labelElement = document.createElement("td");

                    toggleButton.setState = function(isEnabled) {
                        toggleButton.textContent = isEnabled ? 'enabled' : 'disabled';
                        toggleButton.setAttribute("class", toggleButton.textContent);
                    };
                    toggleButton.setState(sensor.enabled);

                    toggleButton.onclick = function () {
                        var toggleRequest = new XMLHttpRequest();
                        toggleRequest.open("POST", "/configure?sensorID=" + sensor.id);
                        toggleButton.setAttribute("class", "processing");
                        toggleButton.disabled = true;
                        toggleButton.textContent = "processing...";

                        toggleRequest.onreadystatechange = function() {
                            if (toggleRequest.readyState===4 && toggleRequest.status===200) {
                                sensor.enabled = !sensor.enabled;
                                toggleButton.setState(sensor.enabled);
                            }
                            else {
                                toggleButton.setState(sensor.enabled);
                            }
                            toggleButton.disabled = false;
                        };
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
        sensorConfigurationRequest.send();
    }
}());