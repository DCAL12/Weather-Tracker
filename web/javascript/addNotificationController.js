(function() {

    window.addEventListener("load", main);

    function main() {

        var addNotificationForm = document.getElementById("addNotification"),
            submitButton = document.getElementById("submitAddNotification");

        addNotificationForm.addEventListener('submit', function (event) {

            var submitRequest = new XMLHttpRequest(),
                formData = new FormData(addNotificationForm);

            event.preventDefault();

            submitButton.disabled = true;
            submitButton.setAttribute("value", "processing");

            submitRequest.open("POST", "/notifications/add");
            submitRequest.onreadystatechange = function () {
                if (submitRequest.readyState === 4 && submitRequest.status !== 200) {
                    alert('Something went wrong...');
                    submitButton.disabled = false;
                    submitButton.setAttribute("value", "submit");
                }
                else if (submitRequest.readyState === 4 && submitRequest.status === 200) {
                    location.reload(true);
                }
            };
            submitRequest.send(formData);
        }, false);
    }
}());