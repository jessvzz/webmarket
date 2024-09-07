/**
 * Restituisce l'HTML per lo stato della proposta con il colore di sfondo appropriato.
 * 
 * @param {string} statoProposta - Lo stato della proposta.
 * @param {string} p - Il tipo di badge ('big-badge' o 'small-badge').
 * @return {string} L'HTML con il colore di sfondo appropriato.
 */
function getStatoProposta(statoProposta, p) {
    let backgroundColor;
    switch (statoProposta) {
        case "IN_ATTESA":
            backgroundColor = "#FFE8A3";
            break;
        case "ACCETTATO":
            backgroundColor = "#AFF4C6";
            break;
        case "RIFIUTATO":
            backgroundColor = "#FFAEAE";
            break;
        case "ORDINATO":
            backgroundColor = "#E4CCFF";
            break;
        default:
            backgroundColor = "#FFFFFF";
    }
    if (p === 'big-badge'){return `
        <div class="card-row-content" style="background-color: ${backgroundColor};">
            <p class="card-row-text">${statoProposta}</p>
        </div>
    `;}
     else if (p === 'small-badge'){
         return  `<div class="badge-stato" style="background-color: ${backgroundColor};">${statoProposta}</div>`;

     }
}

document.addEventListener("DOMContentLoaded", function() {
    /**
     * Seleziona tutti gli elementi con la classe 'card-row-content' e attributo 'stato'.
     * @type {NodeListOf<Element>}
     */
    const proposte = document.querySelectorAll('.card-row-content[data-state]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('data-state');
        let p = 'big-badge';
        proposta.outerHTML = getStatoProposta(statoProposta, p);
    });
});

document.addEventListener("DOMContentLoaded", function() {
    /**
     * Seleziona tutti gli elementi con la classe 'badge-stato' e attributo 'stato'.
     * @type {NodeListOf<Element>}
     */
    const proposte = document.querySelectorAll('.badge-stato[data-state]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('data-state');
        let p = 'small-badge';
        proposta.outerHTML = getStatoProposta(statoProposta, p);
    });
    
    /**
     * Seleziona tutti gli elementi con l'ID 'proposta-container'.
     * @type {NodeListOf<Element>}
     */
    const propostaContainers = document.querySelectorAll(".proposta-container");

    propostaContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");

        if (stato === "ACCETTATO" ) {
            container.style.backgroundColor = "#67c4f1";
            container.addEventListener("mouseover", function() {
                container.style.backgroundColor = "#31a8e3";
            });
            container.addEventListener("mouseout", function() {
                container.style.backgroundColor = "#67c4f1";
            });
        }
    });

    /**
     * Campo di input per la ricerca.
     * @type {HTMLElement}
     */
    const searchInput = document.getElementById("search-input");

    /**
     * Select per il filtraggio dello stato.
     * @type {HTMLElement}
     */
    const filterSelect = document.getElementById("stato");

    /**
     * Filtra le proposte in base al termine di ricerca e allo stato selezionato.
     */
    function filterProposte() {
        const searchTerm = searchInput.value.toLowerCase();
        const selectedStato = filterSelect.value;

        propostaContainers.forEach(container => {
            const codice = container.getAttribute("data-codice").toLowerCase();
            const stato = container.getAttribute("data-stato");

            const matchesSearch = codice.includes(searchTerm);
            const matchesFilter = selectedStato === "tutti" || stato === selectedStato;

            if (matchesSearch && matchesFilter) {
                container.style.display = "";
            } else {
                container.style.display = "none";
            }
        });
    }

    searchInput.addEventListener("input", filterProposte);
    filterSelect.addEventListener("change", filterProposte);
});