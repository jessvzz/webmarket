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


document.addEventListener("DOMContentLoaded", function() {
    const proposte = document.querySelectorAll('.card-row-content[data-state]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('data-state');
        proposta.outerHTML = cambiaStatoProposta(statoProposta);
    });
    
    
    
    const propostaContainers = document.querySelectorAll("#proposta-container");

    propostaContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");

        if (stato === "IN_ATTESA") {
            container.style.backgroundColor = "#1E88E5";
            container.addEventListener('mouseenter', () => {
                
                //per l'hover
                container.style.backgroundColor = "#6ab8fc"; 
            });

            container.addEventListener('mouseleave', () => {
                container.style.backgroundColor = "#1E88E5"; 
            });
        } else {
            container.style.backgroundColor = "#4ea5f2";
            container.addEventListener('mouseenter', () => {
                container.style.backgroundColor = "#6ab8fc"; 
            });

            container.addEventListener('mouseleave', () => {
                container.style.backgroundColor = "#4ea5f2"; 
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
});




