function cambiaStatoDettaglioRichiesta(statoRichiesta) {
    let backgroundColor;

    switch (statoRichiesta) {
        case 'IN_ATTESA':
            backgroundColor = '#FFE8A3'; 
            break;
        case 'PRESA_IN_CARICO':
            backgroundColor = '#ffbf70'; // orange
            break;
        case 'RISOLTA':
            backgroundColor = '#AFF4C6'; // yellow
            break;
        case 'ORDINATA':
            backgroundColor = '#E4CCFF'; // green
            break;
        default:            
        backgroundColor = "#FFFFFF";
}
return `

            <div class="badge-stato w-1/5 py-2 px-3 rounded-md text-base font-semibold text-center" style="background-color: ${backgroundColor}!important;">
                ${statoRichiesta}
            </div> 
`;
}


document.addEventListener("DOMContentLoaded", function() {
    const richiesta = document.querySelector('.badge-stato[data-stato]');
    if (richiesta) {
        const statoRichiesta = richiesta.getAttribute('data-stato');
        console.log('Stato della richiesta:', statoRichiesta); // Verifica il valore
        console.log('Background color:', cambiaStatoDettaglioRichiesta(statoRichiesta));
        richiesta.outerHTML = cambiaStatoDettaglioRichiesta(statoRichiesta);
    }
});