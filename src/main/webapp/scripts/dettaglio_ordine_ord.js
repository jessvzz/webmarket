/**
 * Restituisce un elemento HTML con il colore di sfondo appropriato.
 * 
 * @param {string} statoOrdine - Lo stato dell'ordine.
 * @return {string} L'elemento HTML con il colore di sfondo appropriato.
 */
function cambiaStatoDettaglioOrdine(statoOrdine) {
    let backgroundColor;

    switch (statoOrdine) {
        case 'RESPINTO_NON_CONFORME':
            backgroundColor = '#ff6347';
            break;
        case 'RESPINTO_NON_FUNZIONANTE':
            backgroundColor = '#ff7f50';
            break;
        case 'IN_ATTESA':
            backgroundColor = '#ffd700';
            break;
        case 'ACCETTATO':
            backgroundColor = '#adff2f';
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
    /**
     * Elemento HTML che rappresenta lo stato dell'ordine.
     * @type {HTMLElement}
     */
    const ordine = document.querySelector('.badge-statoOrdini[stato]');
    if (ordine) {
        /**
         * Stato dell'ordine ottenuto dall'attributo stato dell'elemento.
         * @type {string}
         */
        const statoOrdine = ordine.getAttribute('stato');
        console.log('Stato ordine:', statoOrdine); // Verifica il valore
        console.log('Background color:', cambiaStatoDettaglioOrdine(statoOrdine));
        ordine.outerHTML = cambiaStatoDettaglioOrdine(statoOrdine);
    }
});