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
});