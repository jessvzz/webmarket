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
        ordine.outerHTML = cambiaStatoOrdini(statoOrdine);
    });
    
    
    const ordiniContainers = document.querySelectorAll("#ordine-container");

    ordiniContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");

        if (stato === "IN_ATTESA") {
            container.style.backgroundColor = "#5D54BE";
        } else {
            container.style.backgroundColor = "#9892d1";
        }
    });
});