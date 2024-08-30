function getStatoProposta(statoProposta, p) {
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
            backgroundColor = "#FFFFFF";
    }
    if (p === 'big-badge'){return `
        <div class="card-row-content" style="background-color: ${backgroundColor};">
            <p class="card-row-text">${statoProposta}</p>
        </div>
    `;}
     else if (p === 'small-badge'){
         return  `<div class="badge-stato" style="background-color: ${backgroundColor};">${statoProposta}</div>`;

     }
    
}

document.addEventListener("DOMContentLoaded", function() {
    const proposte = document.querySelectorAll('.card-row-content[stato]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('stato');
        let p = 'big-badge';
        proposta.outerHTML = getStatoProposta(statoProposta, p);
    });
});

document.addEventListener("DOMContentLoaded", function() {
    const proposte = document.querySelectorAll('.badge-stato[stato]');
    proposte.forEach(proposta => {
        const statoProposta = proposta.getAttribute('stato');
        let p = 'small-badge';
        proposta.outerHTML = getStatoProposta(statoProposta, p);
    });
});