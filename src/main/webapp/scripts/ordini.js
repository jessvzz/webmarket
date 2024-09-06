function cambiaStatoOrdini(statoOrdine) {
    let backgroundColor;

    switch (statoOrdine) {
        case 'RESPINTO_NON_CONFORME':
            backgroundColor = '#ff6347'; // red
            break;
        case 'RESPINTO_NON_FUNZIONANTE':
            backgroundColor = '#ff7f50'; // orange
            break;
        case 'IN_ATTESA':
            backgroundColor = '#ffd700'; // yellow
            break;
        case 'ACCETTATO':
            backgroundColor = '#adff2f'; // green
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

/*
function sortOrdini(ordini) {
    return Array.from(ordini).sort((a, b) => {
        const statoA = a.getAttribute('data-stato');
        const statoB = b.getAttribute('data-stato');
        const priority = {
            "IN_ATTESA": 1,
            "RESPINTO_NON_CONFORME": 2,
            "RESPINTO_NON_FUNZIONANTE": 2,
            "ACCETTATO": 3,
            "RIFIUTATO": 3
        };

        return priority[statoA] - priority[statoB];
    });
}
*/

document.addEventListener("DOMContentLoaded", function() {

    const ordini = document.querySelectorAll('.card-row-content[stato]');
    ordini.forEach(ordine => {
        const statoOrdine = ordine.getAttribute('stato');
        ordine.outerHTML = cambiaStatoOrdini(statoOrdine);
    });
    
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
        container.style.backgroundColor = "#8076f5";
        container.addEventListener('mouseenter', () => {
            container.style.backgroundColor = "#4f40f5"; 
        });

        container.addEventListener('mouseleave', () => {
            container.style.backgroundColor = "#8076f5"; 
        });
    }
});

    const searchInput = document.getElementById('search');
    const filterSelect = document.getElementById("status");
  
    const ord = document.querySelectorAll('#ordine-container');


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

        //const ords = document.querySelectorAll('.card-row-purple');
        //const sortedOrdini = sortOrdini(ords);

        //const rowsContainer = document.querySelector('.rows-container');
        //sortedOrdini.forEach(ord => rowsContainer.appendChild(ord));
        });

