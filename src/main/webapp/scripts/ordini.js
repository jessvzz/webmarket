/**
 * Restituisce un elemento HTML con il colore di sfondo cambiato in base allo stato dell'ordine.
 * @param {string} statoOrdine - Lo stato dell'ordine (es. 'RESPINTO_NON_CONFORME', 'RESPINTO_NON_FUNZIONANTE', 'IN_ATTESA', 'ACCETTATO', 'RIFIUTATO').
 * @returns {string} - Un elemento HTML con lo stile appropriato per lo stato dell'ordine.
 */
function cambiaStatoOrdini(statoOrdine) {
    let backgroundColor;

    switch (statoOrdine) {
        case 'RESPINTO_NON_CONFORME':
            backgroundColor = '#FFAEAE';
            break;
        case 'RESPINTO_NON_FUNZIONANTE':
            backgroundColor = '#ffbf70';
            break;
        case 'IN_ATTESA':
            backgroundColor = '#FFE8A3';
            break;
        case 'ACCETTATO':
            backgroundColor = '#AFF4C6';
            break;
        case 'RIFIUTATO':
                backgroundColor = '#de533e'; 
                break;


        default:            
        backgroundColor = "#FFFFFF";
}
return `
    <div class="card-row-content" style="background-color: ${backgroundColor};">
        <p class="card-row-text">${statoOrdine}</p>
    </div>
`;
}

/**
 * Funzione che viene eseguita quando il contenuto del documento è completamente caricato.
 * Trova gli elementi con la classe 'card-row-content' e l'attributo 'data-state', quindi aggiorna il loro HTML
 * utilizzando la funzione cambiaStatoOrdini.
 * Aggiunge anche eventi di hover agli elementi con la classe 'card-row-purple'.
 * Aggiunge filtri di ricerca e selezione per gli ordini.
 * @event DOMContentLoaded
 */
document.addEventListener("DOMContentLoaded", function() {

    /**
     * NodeList di elementi con la classe 'card-row-content' e l'attributo 'data-state'.
     * @type {NodeList}
     */
    const ordini = document.querySelectorAll('.card-row-content[data-state]');
    ordini.forEach(ordine => {
        const statoOrdine = ordine.getAttribute('data-state');
        ordine.outerHTML = cambiaStatoOrdini(statoOrdine);
    });
    
    /**
     * NodeList di elementi con la classe 'card-row-purple'.
     * @type {NodeList}
     */
    const ordineContainers = document.querySelectorAll(".card-row-purple");

       ordineContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");

    if (stato === "IN_ATTESA") { 
        container.style.backgroundColor = "#6659f0";
        container.addEventListener('mouseenter', () => {
                        //per l'hover 
                        container.style.backgroundColor = "#4f40f5"; 
                    });
        
                    container.addEventListener('mouseleave', () => {
                        container.style.backgroundColor = "#6659f0"; 
                    });
    } else {
        container.style.backgroundColor = "#958df7";
        container.addEventListener('mouseenter', () => {
            container.style.backgroundColor = "#4f40f5"; 
        });

        container.addEventListener('mouseleave', () => {
            container.style.backgroundColor = "#958df7"; 
        });
    }
});

    /**
     * Input di ricerca per filtrare gli ordini.
     * @type {HTMLInputElement}
     */
    const searchInput = document.getElementById('search');
    
    /**
     * Select per filtrare gli ordini in base allo stato.
     * @type {HTMLSelectElement}
     */
    const filterSelect = document.getElementById("status");
  
    /**
     * NodeList di elementi con la classe 'ordine-container'.
     * @type {NodeList}
     */
    const ord = document.querySelectorAll('.ordine-container');

    /**
     * Filtra gli ordini in base al termine di ricerca e allo stato selezionato.
     */
    function filterOrdini() {
        
        const searchTerm = searchInput.value.toLowerCase();
        const selectedStatus = filterSelect.value;

        ord.forEach(function(o) {
         
            const codice = o.getAttribute('data-codice').toLowerCase();
            const stato = o.getAttribute('data-stato');
             // filtro per codice (proposta)
            const matchCodice = codice.includes(searchTerm);
             //filtro per stato (select)
            const matchStato = (selectedStatus === 'tutti' || stato === selectedStatus);
            if (matchCodice && matchStato) {
                o.style.display = '';
            } else {
                o.style.display = 'none';
            }
        });
    }
        // filtro search
        searchInput.addEventListener('input', filterOrdini);

        // filtro select
        filterSelect.addEventListener('change', filterOrdini);

        
        });