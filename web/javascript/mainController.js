'use strict';
(function() {
    window.addEventListener('load', main);

    function main() {
        var dataTable = document.getElementById("statistics"),
            message = document.getElementById("message");

        var request = new XMLHttpRequest();
        request.open('GET', '/data');

        message.textContent = "loading...";

        request.onload = function() {

            var sensors = JSON.parse(this.responseText);

            sensors.forEach(function(sensor) {

                var dataPrecision = -1; // negative number indicates decimal places to right

                var dataRow = document.createElement("tr"),
                    labelElement = document.createElement("td");

                labelElement.textContent = sensor.label;
                dataRow.appendChild(labelElement);

                if(sensor.observations) {
                    var countElement = document.createElement("td"),
                        averageElement = document.createElement("td"),
                        stdDevElement = document.createElement("td"),
                        lastObservationElement = document.createElement("td");

                    var data = [],
                        statistics;

                    sensor.observations.forEach(function(observation) {
                        data.push(observation.value);
                    });

                    statistics = {
                        count: data.length,
                        average: statisticsUtility.mean(data),
                        standardDeviation: statisticsUtility.standardDeviation(data)
                    };

                    countElement.textContent = statistics.count;
                    averageElement.textContent = statisticsUtility
                        .round(statistics.average, dataPrecision);
                    stdDevElement.textContent = statisticsUtility
                        .round(statistics.standardDeviation, dataPrecision);

                    sensor.observations.sort(function(obs1,obs2) {
                        // Sort from newest to oldest
                        obs1 = new Date(obs1.timeStamp);
                        obs2 = new Date(obs2.timeStamp);
                        return obs2 - obs1;
                    });

                    lastObservationElement.textContent =
                        statisticsUtility.round(sensor.observations[0].value, dataPrecision)
                        + " on "
                        + sensor.observations[0].timeStamp;

                    dataRow.setAttribute("class",
                        (new Date() - 5000 <= new Date(sensor.observations[0].timeStamp)-0) ? "current" : "stale"
                    );
                    dataRow.appendChild(countElement);
                    dataRow.appendChild(averageElement);
                    dataRow.appendChild(stdDevElement);
                    dataRow.appendChild(lastObservationElement);
                }
                else {
                    var noDataElement = document.createElement("td");
                    noDataElement.textContent = "no data";
                    dataRow.appendChild(noDataElement);
                }
                dataTable.appendChild(dataRow);
            });
            message.textContent = "";
        };
        request.send();
    }
}());