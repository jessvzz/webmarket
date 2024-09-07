/**
 * Restituisce un elemento HTML con il colore di sfondo cambiato in base allo stato della richiesta.
 * @param {string} statoRichiesta - Lo stato della richiesta (es. 'IN_ATTESA', 'PRESA_IN_CARICO', 'RISOLTA', 'ORDINATA').
 * @returns {string} - Un elemento HTML con lo stile appropriato per lo stato della richiesta.
 */
function cambiaStatoRichiesta(statoRichiesta) {
    let backgroundColor;

    switch (statoRichiesta) {
        case 'IN_ATTESA':
            backgroundColor = '#FFE8A3';
            break;
        case 'PRESA_IN_CARICO':
            backgroundColor = '#ffbf70';
            break;
        case 'RISOLTA':
            backgroundColor = '#AFF4C6';
            break;
        case 'ORDINATA':
            backgroundColor = '#E4CCFF';
            break;
        default:            
        backgroundColor = "#FFFFFF";
}
return `
    <div class="card-row-content" style="background-color: ${backgroundColor};">
        <p class="card-row-text">${statoRichiesta}</p>
    </div>
`;
}

/**
 * Funzione che viene eseguita quando il contenuto del documento è completamente caricato.
 * Trova gli elementi con la classe 'card-row-content' e l'attributo 'data-state', quindi aggiorna il loro HTML
 * utilizzando la funzione cambiaStatoRichiesta.
 * Aggiunge filtri di ricerca e selezione per le richieste.
 * @event DOMContentLoaded
 */
document.addEventListener("DOMContentLoaded", function() {
 
   /**
    * NodeList di elementi con la classe 'card-row-content' e l'attributo 'data-state'.
    * @type {NodeList}
    */
   const richieste = document.querySelectorAll('.card-row-content[data-state]');

    richieste.forEach(richiesta => {
        const statoRichiesta = richiesta.getAttribute('data-state');
      
        richiesta.outerHTML = cambiaStatoRichiesta(statoRichiesta);
    });

    

    /**
     * Input di ricerca per filtrare le richieste.
     * @type {HTMLInputElement}
     */
    const searchInput = document.getElementById('search');
    
    /**
     * Select per filtrare le richieste in base allo stato.
     * @type {HTMLSelectElement}
     */
    const filterSelect = document.getElementById('status');

    /**
     * NodeList di elementi con la classe 'richiesta-container'.
     * @type {NodeList}
     */
    const ric = document.querySelectorAll('.richiesta-container');


    /**
     * Filtra le richieste in base al termine di ricerca e allo stato selezionato.
     */
    function filterRichieste() {
        
        const searchTerm = searchInput.value.toLowerCase();
        const selectedStatus = filterSelect.value;

        ric.forEach(function(r) {
         
            const codice = r.getAttribute('data-codice').toLowerCase();
            const stato = r.getAttribute('data-stato');
             // filtro per codice (ricerca)
            const matchCodice = codice.includes(searchTerm);
             //filtro per stato (select)
            const matchStato = (selectedStatus === 'tutti' || stato === selectedStatus);
            if (matchCodice && matchStato) {
                r.style.display = '';
            } else {
                r.style.display = 'none';
            }
        });
    }
    
            // filtro search
            searchInput.addEventListener('input', filterRichieste);

            // filtro select
            filterSelect.addEventListener('change', filterRichieste);
        });