document.addEventListener('DOMContentLoaded', function() {
    /**
     * Seleziona tutti gli elementi con la classe 'textfield-grey'.
     * @type {NodeListOf<Element>}
     */
    const inputs = document.querySelectorAll('.textfield-grey');

    /**
     * Bottone per l'invio della proposta.
     * @type {HTMLElement}
     */
    const btnInvio = document.getElementById('btn-invio');

    /**
     * Campo per le note.
     * @type {HTMLElement}
     */
    const noteField = document.getElementById('note');

    /**
     * Controlla se tutti i campi di input sono riempiti (eccetto il campo note) e mostra/nasconde il bottone di invio.
     */
    function checkInputs() {
        let allFilled = true;
        inputs.forEach(input => {
            if (input !== noteField && input.value.trim() === '') {
                allFilled = false;
            }
        });
        if (allFilled) {
            btnInvio.classList.remove('hidden');
        } else {
            btnInvio.classList.add('hidden');
        }
    }

    /**
     * Aggiunge un listener per l'evento input su ogni campo di input per controllare se tutti i campi sono riempiti.
     */
    inputs.forEach(input => {
        input.addEventListener('input', checkInputs);
    });

    // Controlla inizialmente se tutti i campi sono riempiti.
    checkInputs();
});