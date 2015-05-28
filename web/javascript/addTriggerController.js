'use strict';
(function() {

    window.addEventListener('load', main);

    function main() {

        var addTriggerForm = document.querySelector('form'),
            submitButton = document.getElementById("submitAddTrigger");

        addTriggerForm.addEventListener('submit', function(event) {



            submitButton.disabled = true;
            submitButton.setAttribute('value', "processing");

            submitRequest.open('POST', '/triggers/add', true);
            submitRequest.onload = function (requestEvent) {
                if (requestEvent.status !== 200) {
                    alert('Something went wrong...');
                    submitButton.disabled = false;
                    submitButton.setAttribute('value', "submit");
                }
                else {
                    location.reload(true);
                }
            };
            submitRequest.send(formData);
            event.preventDefault();
        }, false);
    }
}());