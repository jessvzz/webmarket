function cambiaStatoDettaglioProposta(statoProposta) {
    let backgroundColor;

    switch (statoProposta) {
        case 'RIFIUTATO':
            backgroundColor = '#ff6347'; // red
            break;
        case 'IN_ATTESA':
            backgroundColor = '#ff7f50'; // orange
            break;
        case 'ORDINATO':
            backgroundColor = '#ffd700'; // yellow
            break;
        case 'ACCETTATO':
            backgroundColor = '#adff2f'; // green
            break;
        default:            
        backgroundColor = "#FFFFFF";
}
return `

            <div class="badge-stato w-1/5 py-2 px-3 rounded-md text-base font-semibold text-center" style="background-color: ${backgroundColor};">
                ${statoProposta}
            </div> 
`;
}



document.addEventListener("DOMContentLoaded", function() {
    const proposta = document.querySelector('.badge-stato[stato]');
    if (proposta) {
        const statoProposta = proposta.getAttribute('stato');
        proposta.outerHTML = cambiaStatoDettaglioProposta(statoProposta);
    }
});
