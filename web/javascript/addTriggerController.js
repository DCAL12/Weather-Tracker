(function() {

    window.addEventListener('load', main);

    function main() {

        var addTriggerForm = document.getElementById("addTrigger"),
            submitButton = document.getElementById("submitAddTrigger");

        addTriggerForm.addEventListener('submit', function(event) {

            var submitRequest = new XMLHttpRequest(),
                formData = new FormData(addTriggerForm);

            event.preventDefault();

            submitButton.disabled = true;
            submitButton.setAttribute('value', "processing");

            submitRequest.open('POST', '/triggers/add');
            submitRequest.onreadystatechange = function () {
                if (submitRequest.readyState === 4 && submitRequest.status !== 200) {
                    alert('Something went wrong...');
                    submitButton.disabled = false;
                    submitButton.setAttribute('value', "submit");
                }
                else if (submitRequest.readyState === 4 && submitRequest.status === 200) {
                    location.reload(true);
                }
            };
            submitRequest.send(formData);
        }, false);
    }
}());