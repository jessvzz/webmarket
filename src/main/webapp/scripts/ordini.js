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

document.addEventListener("DOMContentLoaded", function() {
    const ordini = document.querySelectorAll('.card-row-content[stato]');

    const ordiniContainers = document.querySelectorAll("#ordine-container");

    ordini.forEach(ordine => {
        const statoOrdine = ordine.getAttribute('stato');
        ordine.outerHTML = cambiaStatoOrdini(statoOrdine);
    });
    
    
       ordiniContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");
        container.style.backgroundColor = stato === "IN_ATTESA" ? "#5D54BE" : "#9892d1";
    });

    const filterSelect = document.getElementById("category");
    console.log(filterSelect); // Verifica che l'elemento select sia trovato
    console.log(ordiniContainers); // Verifica che gli ordini siano trovati
    // const ordiniContainers = document.querySelectorAll(".card-row-purple");

    filterSelect.addEventListener("change", function() {
     const selectedValue = this.value;

     ordiniContainers.forEach(container => {
         const stato = container.getAttribute("data-stato");

         if ( selectedValue === "TUTTI" || stato === selectedValue) {
             container.style.display = "block";
         } else {
             container.style.display = "none";
         }
     });
 });
});