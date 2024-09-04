function getStatoOrdine(statoOrdine) {
    let backgroundColor;
    switch (statoOrdine) {
        case "ACCETTATO":
            backgroundColor = "#AFF4C6";
            break;
        case "RESPINTO_NON_CONFORME":
            backgroundColor = "#FFAEAE";
            break;
        case "RESPINTO_NON_FUNZIONANTE":
            backgroundColor = "#E4CCFF";
            break;
        case "IN_ATTESA":
            backgroundColor = "#FFEECC";
            break;
        case "RIFIUTATO":
            backgroundColor = "#f76a6a";
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

function sortOrdini(ordini) {
    return Array.from(ordini).sort((a, b) => {
        const statoA = a.getAttribute('data-stato');
        const statoB = b.getAttribute('data-stato');
        const priority = {
            "RESPINTO_NON_CONFORME": 1,
            "RESPINTO_NON_FUNZIONANTE": 1,
            "ACCETTATO": 2,
            "RIFIUTATO": 2,
            "IN_ATTESA": 3
        };

        return priority[statoA] - priority[statoB];
    });
}

document.addEventListener("DOMContentLoaded", function() {
    const ordini = document.querySelectorAll('.card-row-content[stato]');
    ordini.forEach(ordine => {
        const statoOrdine = ordine.getAttribute('stato');
        ordine.outerHTML = getStatoOrdine(statoOrdine);
    });

    const ords = document.querySelectorAll('.card-row-orange');
    const sortedOrdini = sortOrdini(ords);

    const rowsContainer = document.querySelector('.rows-container');
    sortedOrdini.forEach(ord => rowsContainer.appendChild(ord));
});