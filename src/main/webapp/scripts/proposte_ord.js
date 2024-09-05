function cambiaStatoProposta(statoProposta) {
    let backgroundColor;

    switch (statoProposta) {
        case 'RIFIUTATO':
            backgroundColor = '#ff6347'; // red
            break;
        case 'IN_ATTESA':
            backgroundColor = '#ff7f50'; // orange
            break;
        case 'ORDINATO':
            backgroundColor = '#ffd700'; // yellow
            break;
        case 'ACCETTATO':
            backgroundColor = '#adff2f'; // green
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

function sortProposte(proposte) {
    return Array.from(proposte).sort((a, b) => {
        const statoA = a.getAttribute('data-stato');
        const statoB = b.getAttribute('data-stato');
        const priority = {
            "IN_ATTESA": 1,
            "ORDINATO": 2,
            "ACCETTATO": 3,
            "RIFIUTATO": 3
        };

        return priority[statoA] - priority[statoB];
    });
}

document.addEventListener("DOMContentLoaded", function() {
    const proposte = document.querySelectorAll('.card-row-content[stato]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('stato');
        proposta.outerHTML = cambiaStatoProposta(statoProposta);
    });
    
    
    
    const propostaContainers = document.querySelectorAll("#proposta-container");

    propostaContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");

        if (stato === "IN_ATTESA") {
            container.style.backgroundColor = "#e78e52";
            container.addEventListener('mouseenter', () => {
                
                //per l'hover
                container.style.backgroundColor = "#ff9966"; 
            });

            container.addEventListener('mouseleave', () => {
                container.style.backgroundColor = "#e78e52"; 
            });
        } else {
            container.style.backgroundColor = "#edb995";
            container.addEventListener('mouseenter', () => {
                container.style.backgroundColor = "#e78e52"; 
            });

            container.addEventListener('mouseleave', () => {
                container.style.backgroundColor = "#edb995"; 
            });
        }
    });
    
    //prendo elementi di ricerca e filtro
    const searchInput = document.getElementById('search');
    const filterSelect = document.getElementById('status');
    
    //container delle proposte
    const proposals = document.querySelectorAll('#proposta-container');

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

    const props = document.querySelectorAll('.card-row-salm');
    const sortedProposte = sortProposte(props);

    const rowsContainer = document.querySelector('.rows-container');
    sortedProposte.forEach(ord => rowsContainer.appendChild(ord));
});




