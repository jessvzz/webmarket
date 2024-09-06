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
    const proposte = document.querySelectorAll('.card-row-content[stato]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('stato');
        let p = 'big-badge';
        proposta.outerHTML = getStatoProposta(statoProposta, p);
    });
});

document.addEventListener("DOMContentLoaded", function() {
    const proposte = document.querySelectorAll('.badge-stato[stato]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('stato');
        let p = 'small-badge';
        proposta.outerHTML = getStatoProposta(statoProposta, p);
    });
    
    const propostaContainers = document.querySelectorAll("#proposta-container");

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

    // filtraggio (sia searchbar che select)
    const searchInput = document.getElementById("search-input");
    const filterSelect = document.getElementById("stato");

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