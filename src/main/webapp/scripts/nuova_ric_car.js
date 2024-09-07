/**
 * Aggiunge un listener per l'evento DOMContentLoaded per eseguire il codice quando il DOM è completamente caricato.
 * @event DOMContentLoaded
 */
document.addEventListener('DOMContentLoaded', function() {
    /**
     * Seleziona il primo elemento form nel documento.
     * @type {HTMLFormElement}
     */
    const form = document.querySelector('form');
    
    if (form) {
        /**
         * Aggiunge un listener per l'evento submit del form per prevenire l'invio predefinito e mostrare un alert.
         * @event submit
         * @param {Event} event - L'evento submit.
         */
        form.addEventListener('submit', function(event) {
            event.preventDefault(); 
            showAlert();
        });
    }
});

/**
 * Mostra un alert personalizzato e aggiunge un listener per il pulsante di chiusura.
 */
function showAlert() {
    /**
     * Seleziona l'elemento con ID 'custom-alert'.
     * @type {HTMLElement}
     */
    const alertBox = document.getElementById('custom-alert');
    if (!alertBox) {
        console.error("Elemento con id 'custom-alert' non trovato!");
        return;
    }
    alertBox.style.display = 'block';

    /**
     * Seleziona il primo elemento con la classe 'close-btn' nel documento.
     * @type {HTMLElement}
     */
    const closeButton = document.querySelector('.close-btn');
    closeButton.addEventListener('click', function() {
        window.location.href = "homepageordinante"; 
    });
}