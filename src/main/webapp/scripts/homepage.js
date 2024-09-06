window.addEventListener("load", function() {

    // Controlla se il parametro success è presente nella URL
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('success')) {
        alert("Password modificata con successo!");
    }

});