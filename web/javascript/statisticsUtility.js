statisticsUtility = {
    sum: function(data) {
        return data.reduce(function(num1, num2) {
            return num1 + num2;
        })
    },
    mean: function(data) {
        return statisticsUtility.sum(data) / data.length
    },
    variance: function(data) {
        // variance = average(squared deviations of each of the values from the mean))
        var deviationsSquared = [],
            mean = statisticsUtility.mean(data);

        data.forEach(function(value) {
            deviationsSquared.push(Math.pow((value - mean), 2));
        });
        return statisticsUtility.mean(deviationsSquared);
    },
    standardDeviation: function(data) {
        // standard deviation = square_root(variance)
        return Math.sqrt(statisticsUtility.variance(data));
    }
};
