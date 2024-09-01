function cambiaStatoDettaglioOrdine(statoOrdine) {
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

console.log("Background color: " + backgroundColor); // Aggiunto per debug

return `

<div class="badge-stato" style="background-color: ${backgroundColor}!important;">
${statoOrdine}
</div> 
`;
}


document.addEventListener("DOMContentLoaded", function() {
    const ordine = document.querySelector('.grey-row[stato]');
    if (ordine) {
        const statoOrdine = ordine.getAttribute('stato');
        console.log('Stato ordine:', statoOrdine); // Verifica il valore
        console.log('Background color:', cambiaStatoDettaglioOrdine(statoOrdine));
        ordine.outerHTML = cambiaStatoDettaglioOrdine(statoOrdine);
    }
});
