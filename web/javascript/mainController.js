(function() {
    window.addEventListener("load", main);

    function main() {
        var dataTable = document.getElementById("statistics"),
            message = document.getElementById("message");

        var request = new XMLHttpRequest();
        request.open("GET", "/data");

        message.textContent = "loading...";

        request.onload = function() {

            var sensors = JSON.parse(this.responseText);

            if(sensors.observations) {
                sensors.forEach(function(sensor) {

                    var dataRow = document.createElement("tr"),
                        labelElement = document.createElement("td"),
                        sumElement = document.createElement("td"),
                        averageElement = document.createElement("td"),
                        stdDevElement = document.createElement("td");

                    var data = [],
                        statistics = {};

                    sensor.observations.forEach(function(observation) {
                        data.push(observation.value);
                    });

                    statistics = {
                        sum: statisticsUtility.sum(data),
                        average: statisticsUtility.mean(data),
                        standardDeviation: statisticsUtility.standardDeviation(data)
                    };

                    labelElement.textContent = sensor.label;
                    sumElement.textContent = statistics.sum;
                    averageElement.textContent = statistics.average;
                    stdDevElement.textContent = statistics.standardDeviation;

                    dataRow.appendChild(labelElement);
                    dataRow.appendChild(sumElement);
                    dataRow.appendChild(averageElement);
                    dataRow.appendChild(stdDevElement);

                    dataTable.appendChild(dataRow);

                    message.textContent = "";
                });
            }
            else {
                message.textContent = "No data has been collected. Go to setup to enable sensors."
            }
        };
        request.send();
    }
}());