function cambiaStatoDettaglioProposta(statoProposta) {
    let backgroundColor;

    switch (statoProposta) {
        case 'RIFIUTATO':
            backgroundColor = '#FFAEAE'; // red
            break;
        case 'IN_ATTESA':
            backgroundColor = '#FFE8A3'; // orange
            break;
        case 'ORDINATO':
            backgroundColor = '#E4CCFF'; 
            break;
        case 'ACCETTATO':
            backgroundColor = '#AFF4C6'; // green
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
