function cambiaStatoRichiesta(statoRichiesta) {
    let backgroundColor;

    switch (statoRichiesta) {
        case 'IN_ATTESA':
            backgroundColor = '#ff6347'; // red
            break;
        case 'PRESA_IN_CARICO':
            backgroundColor = '#ff7f50'; // orange
            break;
        case 'RISOLTA':
            backgroundColor = '#ffd700'; // yellow
            break;
        case 'ORDINATA':
            backgroundColor = '#adff2f'; // green
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

    const richiestaContainers = document.querySelectorAll("#richiesta-container");

    richiestaContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");

        if (stato === "IN_ATTESA") {
            container.style.backgroundColor = "#73c8fa";
        } else {
            container.style.backgroundColor = "#90D5FE";
        }
    });


});