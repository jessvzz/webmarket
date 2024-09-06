/**
 * Aggiunge un listener per l'evento load che esegue il codice al caricamento della finestra.
 */
window.addEventListener("load", function() {

    /**
     * Controlla se il parametro success è presente nella URL.
     * @type {URLSearchParams}
     */
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('success')) {
        alert("Password modificata con successo!");
    }

});