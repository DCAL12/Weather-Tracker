'use strict';

var statisticsUtility = function() {

    return {
        sum: function (data) {
            return data.reduce(function (num1, num2) {
                return num1 + num2;
            })
        },
        mean: function (data) {
            return statisticsUtility.sum(data) / data.length
        },
        variance: function (data) {
            // variance = average(squared deviations of each of the values from the mean))
            var deviationsSquared = [],
                mean = statisticsUtility.mean(data);

            data.forEach(function (value) {
                deviationsSquared.push(Math.pow((value - mean), 2));
            });
            return statisticsUtility.mean(deviationsSquared);
        },
        standardDeviation: function (data) {
            // standard deviation = square_root(variance)
            return Math.sqrt(statisticsUtility.variance(data));
        },
        round: function (value, exp) {
            return decimalAdjust('round', value, exp)
        }
    };

    function decimalAdjust(type, value, exp) {
        /**
         * FROM MDN Math.Round() Example: Decimal rounding
         * available from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/round
         *
         * Decimal adjustment of a number.
         *
         * @param {String}  type  The type of adjustment.
         * @param {Number}  value The number.
         * @param {Integer} exp   The exponent (the 10 logarithm of the adjustment base).
         * @returns {Number} The adjusted value.
         */

        // If the exp is undefined or zero...
        if (typeof exp === 'undefined' || +exp === 0) {
            return Math[type](value);
        }
        value = +value;
        exp = +exp;

        // If the value is not a number or the exp is not an integer...
        if (isNaN(value) || !(typeof exp === 'number' && exp % 1 === 0)) {
            return NaN;
        }

        // Shift
        value = value.toString().split('e');
        value = Math[type](+(value[0] + 'e' + (value[1] ? (+value[1] - exp) : -exp)));

        // Shift back
        value = value.toString().split('e');
        return +(value[0] + 'e' + (value[1] ? (+value[1] + exp) : exp));
    }
}();