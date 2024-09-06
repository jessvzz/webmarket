document.addEventListener("DOMContentLoaded", function() {
    /**
     * Elemento HTML che rappresenta il badge dello stato.
     * @type {HTMLElement}
     */
    var statoBadge = document.getElementById("statoBadge");

    /**
     * Testo dello stato ottenuto dal contenuto del badge.
     * @type {string}
     */
    var stato = statoBadge.textContent.trim();

    /**
     * Cambia il colore di sfondo del badge in base allo stato.
     */
    switch(stato) {
        case 'RESPINTO_NON_CONFORME':
            statoBadge.style.backgroundColor = '#FFAEAE';
            break;

        case 'RESPINTO_NON_FUNZIONANTE':
            statoBadge.style.backgroundColor = '#E4CCFF';
            break;

        case 'IN_ATTESA':
            statoBadge.style.backgroundColor = '#FFEECC';
            break;

        case 'RIFIUTATO':
            statoBadge.style.backgroundColor = '#f76a6a';
            break;

        case 'ACCETTATO':
            statoBadge.style.backgroundColor = '#AFF4C6';
            break;
        
        default:
            statoBadge.style.backgroundColor = '#979dac';
    }
});