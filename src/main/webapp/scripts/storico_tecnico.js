function getStatoOrdine(statoOrdine) {
    let backgroundColor;

    switch (statoOrdine) {
        case 'RESPINTO_NON_CONFORME':
            backgroundColor = '#FFAEAE'; 
            break;
        case 'RESPINTO_NON_FUNZIONANTE':
            backgroundColor = '#E4CCFF'; 
            break;
        case 'IN_ATTESA':
            backgroundColor = '#FFEECC'; 
            break;
        case 'ACCETTATO':
            backgroundColor = '#AFF4C6';
            break;
        case 'RIFIUTATO':
                backgroundColor = '#f76a6a'; 
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


 

document.addEventListener("DOMContentLoaded", function() {

    const ordini = document.querySelectorAll('.card-row-content[stato]');


    ordini.forEach(ordine => {
        const statoOrdine = ordine.getAttribute('stato');
        ordine.outerHTML = getStatoOrdine(statoOrdine);
    });
    
    const ordineContainers = document.querySelectorAll(".card-row-orange");

       ordineContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");
    //     container.style.backgroundColor = stato === "IN_ATTESA" ? "#5D54BE" : "#9892d1";
    // });
    if (stato === "RESPINTO_NON_CONFORME" || stato === "RESPINTO_NON_FUNZIONANTE") {
        container.style.backgroundColor = "#F67956";
        container.addEventListener("mouseover", function() {
            container.style.backgroundColor = "#f55e33";
        });
        container.addEventListener("mouseout", function() {
            container.style.backgroundColor = "#F67956";
        });
    } else {
        container.style.backgroundColor = "#e6a693";
        container.addEventListener("mouseover", function() {
            container.style.backgroundColor = "#F67956";
        });
        container.addEventListener("mouseout", function() {
            container.style.backgroundColor = "#e6a693";
        });
    }
});

    const searchInput = document.getElementById('search');
    const filterSelect = document.getElementById("stato");
  
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

        const ords = document.querySelectorAll('.card-row-orange');
        //const sortedOrdini = sortOrdini(ords);

        //const rowsContainer = document.querySelector('.rows-container');
        //sortedOrdini.forEach(ord => rowsContainer.appendChild(ord));
        });
