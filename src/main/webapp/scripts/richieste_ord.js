function cambiaStatoRichiesta(statoRichiesta) {
    let backgroundColor;

    switch (statoRichiesta) {
        case 'IN_ATTESA':
            backgroundColor = '#FFE8A3'; //giallo
            break;
        case 'PRESA_IN_CARICO':
            backgroundColor = '#ffbf70'; //arancione
            break;
        case 'RISOLTA':
            backgroundColor = '#AFF4C6'; //verde
            break;
        case 'ORDINATA':
            backgroundColor = '#E4CCFF'; // lilla
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

document.addEventListener("DOMContentLoaded", function() {
 
   const richieste = document.querySelectorAll('.card-row-content[stato]');

    richieste.forEach(richiesta => {
        const statoRichiesta = richiesta.getAttribute('stato');
      
        richiesta.outerHTML = cambiaStatoRichiesta(statoRichiesta);
    });

    

    const searchInput = document.getElementById('search');
    const filterSelect = document.getElementById('status');

    const ric = document.querySelectorAll('#richiesta-container');


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

