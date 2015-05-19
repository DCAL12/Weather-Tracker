(function() {
    window.addEventListener("load", main);

    function main() {
        var dataTable = document.getElementById("statistics");
        var request = new XMLHttpRequest();
        request.open("GET", "/data");

        request.onload = function() {

            console.log(this.responseType);
            console.log(this.responseText);
            var sensors = JSON.parse(this.responseText);
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
                    sum: calculateSum(data),
                    average: calculateStandardDeviation(data),
                    standardDeviation: calculateStandardDeviation(data)
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
            });
        };
        request.send();
    }

    function calculateSum(data) {
        return data.reduce(function(num1, num2) {
            return num1 + num2;
        })
    }

    function calculateAverage(data) {
        return calculateSum(data) / data.length
    }

    function calculateVariance(data) {
        // variance = average(squared deviations of each of the values from the mean))
        var deviationsSquared = [],
            mean = calculateAverage(data);

        data.forEach(function(value) {
            deviationsSquared.push(Math.pow((value - mean), 2));
        });
        return calculateAverage(deviationsSquared);
    }

    function calculateStandardDeviation(data) {
        // standard deviation = square_root(variance)
        return Math.sqrt(calculateVariance(data));
    }
}());