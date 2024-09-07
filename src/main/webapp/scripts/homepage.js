/**
 * Funzione che viene eseguita quando la finestra è completamente caricata.
 * Controlla se il parametro 'success' è presente nella URL e, in tal caso,
 * crea e visualizza un messaggio di successo che scompare dopo 3 secondi.
 */
window.addEventListener("load", function() {
    /**
     * Controlla se il parametro 'success' è presente nella URL.
     * @type {URLSearchParams}
     */
    const urlParams = new URLSearchParams(window.location.search);
    
    if (urlParams.has('success')) {
        /**
         * Crea un elemento div per il messaggio di successo.
         * @type {HTMLDivElement}
         */
        const successLabel = document.createElement("div");
        successLabel.className = "success-msg";
        successLabel.textContent = "Password modificata con successo!";
        
        /**
         * Aggiunge il messaggio di successo al contenitore con l'ID 'success-message'.
         * @type {HTMLElement}
         */
        const container = document.getElementById("success-message");
        container.appendChild(successLabel);
        
        /**
         * Rimuove il messaggio di successo dopo 3 secondi.
         */
        setTimeout(() => {
            successLabel.remove();
        }, 3000);
    }
});