document.addEventListener("DOMContentLoaded", function() {
    var statoBadge = document.getElementById("statoBadge");
    var stato = statoBadge.textContent.trim();

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