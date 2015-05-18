(function() {

    window.addEventListener("load", main);

    function main() {

        var setupTable = document.getElementById("configureSensors");
        var request = new XMLHttpRequest();
        request.open("GET", "/configure");



        request.onload = function() {

            var sensors = this.responseText;
            sensors.forEach(function(sensor) {

                var dataRow = document.createElement("tr"),
                    statusElement = document.createElement("td"),
                    toggleButton = document.createElement("button"),
                    idElement = document.createElement("td"),
                    labelElement = document.createElement("td");

                toggleButton.onclick = function() {
                    console.log('toggle' + sensor.id);
                }

                toggleButton.textContent = sensor.enabled;
                idElement.textContent = sensor.id;
                labelElement.textContent = sensor.label;

                dataRow.appendChild(statusElement);
                dataRow.appendChild(idElement);
                dataRow.appendChild(labelElement);

                setupTable.appendChild(dataRow);
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