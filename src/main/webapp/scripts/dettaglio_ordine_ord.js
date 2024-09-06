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


return `
<div class="badge-statoOrdini w-1/5 py-2 px-3 rounded-md text-base font-semibold text-center" style="background-color: ${backgroundColor};">
  ${statoOrdine}  
</div> 
`;
}


document.addEventListener("DOMContentLoaded", function() {
    const ordine = document.querySelector('.badge-statoOrdini[stato]');
    if (ordine) {
        const statoOrdine = ordine.getAttribute('stato');
        console.log('Stato ordine:', statoOrdine); // Verifica il valore
        console.log('Background color:', cambiaStatoDettaglioOrdine(statoOrdine));
        ordine.outerHTML = cambiaStatoDettaglioOrdine(statoOrdine);
    }
});
