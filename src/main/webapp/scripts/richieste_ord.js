function cambiaStatoRichiesta(statoRichiesta) {
    let backgroundColor;

    switch (statoRichiesta) {
        case 'IN_ATTESA':
            backgroundColor = '#ff6347';
            break;
        case 'PRESA_IN_CARICO':
            backgroundColor = '#ff7f50';
            break;
        case 'RISOLTA':
            backgroundColor = '#ffd700';
            break;
        case 'ORDINATA':
            backgroundColor = '#adff2f';
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

function sortRichieste(richieste) {
    return Array.from(richieste).sort((a, b) => {
        const statoA = a.getAttribute('data-stato');
        const statoB = b.getAttribute('data-stato');
        const priority = {
            "IN_ATTESA": 1,
            "ORDINATA": 2,
            "PRESA_IN_CARICO": 3,
            "RISOLTA": 4
        };

        return priority[statoA] - priority[statoB];
    });
}

document.addEventListener("DOMContentLoaded", function() {
    console.log("Il DOM è caricato!");

    const richieste = document.querySelectorAll('.card-row-content[stato]');
    console.log("Numero di richieste trovate:", richieste.length);

    richieste.forEach(richiesta => {
        const statoRichiesta = richiesta.getAttribute('stato');
        console.log("Stato della richiesta corrente:", statoRichiesta);
        richiesta.outerHTML = cambiaStatoRichiesta(statoRichiesta);
    });

    const richiestaContainers = document.querySelectorAll(".card-row-skyblue");

    richiestaContainers.forEach(container => {
        const stato = container.getAttribute("data-stato");

        if (stato === "IN_ATTESA") { 
            console.log("è nell'if di stato==In attesa!");

            container.style.backgroundColor = "#1685c7";
        } else {
            container.style.backgroundColor = "#90D5FE";
        }
    });


    // Implementazione del filtro per lo stato
    const filterSelect = document.getElementById("status");
    filterSelect.addEventListener("change", function() {
        const selectedValue = this.value;
        richiestaContainers.forEach(container => {
            const stato = container.getAttribute("data-stato");
            if (selectedValue === "tutti" || stato === selectedValue) {
                container.style.display = "block";
            } else {
                container.style.display = "none";
            }
        });
});

    // Riordinamento righe per importanza stato
    const reqs = document.querySelectorAll('.card-row-skyblue');
    const sortedRichieste = sortRichieste(reqs);

    const rowsContainer = document.querySelector('.rows-container');
    sortedRichieste.forEach(req => rowsContainer.appendChild(req));
});