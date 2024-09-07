/**
 * Restituisce un elemento HTML con il colore di sfondo cambiato in base allo stato della proposta.
 * @param {string} statoProposta - Lo stato della proposta (es. 'RIFIUTATO', 'IN_ATTESA', 'ORDINATO', 'ACCETTATO').
 * @returns {string} - Un elemento HTML con lo stile appropriato per lo stato della proposta.
 */
function cambiaStatoProposta(statoProposta) {
    let backgroundColor;

    switch (statoProposta) {
        case 'RIFIUTATO':
            backgroundColor = '#FFAEAE '
            break;
        case 'IN_ATTESA':
            backgroundColor = '#FFE8A3';
            break;
        case 'ORDINATO':
            backgroundColor = '#E4CCFF '
            break;
        case 'ACCETTATO':
            backgroundColor = '#AFF4C6';
            break;
        default:            
        backgroundColor = "#FFFFFF";
}
return `
    <div class="card-row-content" style="background-color: ${backgroundColor};">
        <p class="card-row-text">${statoProposta}</p>
    </div>
`;
}

/**
 * Funzione che viene eseguita quando il contenuto del documento è completamente caricato.
 * Trova gli elementi con la classe 'card-row-content' e l'attributo 'data-state', quindi aggiorna il loro HTML
 * utilizzando la funzione cambiaStatoProposta.
 * Aggiunge anche eventi di hover agli elementi con la classe 'proposta-container'.
 * Aggiunge filtri di ricerca e selezione per le proposte.
 * @event DOMContentLoaded
 */
document.addEventListener("DOMContentLoaded", function() {
    /**
     * NodeList di elementi con la classe 'card-row-content' e l'attributo 'data-state'.
     * @type {NodeList}
     */
    const proposte = document.querySelectorAll('.card-row-content[data-state]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('data-state');
        proposta.outerHTML = cambiaStatoProposta(statoProposta);
    });
    
    
    /**
     * NodeList di elementi con la classe 'proposta-container'.
     * @type {NodeList}
     */
    const propostaContainers = document.querySelectorAll(".proposta-container");

    propostaContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");

        if (stato === "IN_ATTESA") {
            container.style.backgroundColor = "#1E88E5";
            container.addEventListener('mouseenter', () => {
                
                //per l'hover
                container.style.backgroundColor = "#1b54e3"; 
            });

            container.addEventListener('mouseleave', () => {
                container.style.backgroundColor = "#1E88E5"; 
            });
        } else {
            container.style.backgroundColor = "#69b8ff";
            container.addEventListener('mouseenter', () => {
                container.style.backgroundColor = "#1b54e3"; 
            });

            container.addEventListener('mouseleave', () => {
                container.style.backgroundColor = "#69b8ff"; 
            });
        }
    });
    
    /**
     * Input di ricerca per filtrare le proposte.
     * @type {HTMLInputElement}
     */
    const searchInput = document.getElementById('search');
    
    /**
     * Select per filtrare le proposte in base allo stato.
     * @type {HTMLSelectElement}
     */
    const filterSelect = document.getElementById('status');
    
    /**
     * NodeList di elementi con la classe 'proposta-container'.
     * @type {NodeList}
     */
    const proposals = document.querySelectorAll('.proposta-container');

    /**
     * Filtra le proposte in base al termine di ricerca e allo stato selezionato.
     */
    function filterProposte() {
        //prendo valori nella ricerca e nel select (minuscoli)
        const searchTerm = searchInput.value.toLowerCase();
        const selectedStatus = filterSelect.value;

        proposals.forEach(function(proposal) {
            //prendo valori degli attributi
            const codice = proposal.getAttribute('data-codice').toLowerCase();
            const stato = proposal.getAttribute('data-stato');

            // filtro per codice (ricerca)
            //il codice deve includere quello che ho nella ricerca
            const matchCodice = codice.includes(searchTerm);
            
            //filtro per stato (select)
            const matchStato = (selectedStatus === 'tutti' || stato === selectedStatus);

            if (matchCodice && matchStato) {
                proposal.style.display = '';
            } else {
                proposal.style.display = 'none';
            }
        });
    }
    
    // filtro search
    searchInput.addEventListener('input', filterProposte);

    // filtro select
    filterSelect.addEventListener('change', filterProposte);
});