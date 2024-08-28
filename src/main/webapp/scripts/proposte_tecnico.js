function getStatoProposta(statoProposta) {
    let backgroundColor;
    switch (statoProposta) {
        case "IN_ATTESA":
            backgroundColor = "#FFE8A3";
            break;
        case "ACCETTATO":
            backgroundColor = "#AFF4C6";
            break;
        case "RIFIUTATO":
            backgroundColor = "#FFAEAE";
            break;
        case "ORDINATO":
            backgroundColor = "#E4CCFF";
            break;
        default:
            backgroundColor = "#FFFFFF"; // Colore di default
    }
    return `
        <div class="card-row-content" style="background-color: ${backgroundColor};">
            <p class="card-row-text">${statoProposta}</p>
        </div>
    `;
}

document.addEventListener("DOMContentLoaded", function() {
    const proposte = document.querySelectorAll('.card-row-content[stato]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('stato');
        proposta.outerHTML = getStatoProposta(statoProposta);
    });
});