document.addEventListener('DOMContentLoaded', (event) => {
    /**
     * Bottone per eliminare la categoria.
     * @type {HTMLElement}
     */
    const deleteButton = document.getElementById('delete-button');

    /**
     * Aggiunge un listener per l'evento click sul bottone deleteButton.
     * 
     * @param {Event} event - L'evento click.
     */
    deleteButton.addEventListener('click', function(event) {
        event.preventDefault(); 

        /**
         * Conferma se l'utente vuole eliminare la categoria.
         * @type {boolean}
         */
        const confirmed = confirm('Sei sicura di voler eliminare questa categoria?');
        
        if (confirmed) {
            /**
             * Chiave della categoria da eliminare.
             * @type {string}
             */
            const categoriaKey = deleteButton.getAttribute('data-key'); 
            window.location.href = 'categoria?n=' + categoriaKey + '&action=deleteCategory';
        }
    });
});