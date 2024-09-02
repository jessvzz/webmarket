document.addEventListener("DOMContentLoaded", function() {
    var statoBadge = document.getElementById("statoBadge");
    var stato = statoBadge.textContent.trim();

    switch(stato) {
        case 'IN_ATTESA':
            statoBadge.classList.add('grey-storico');
            break;
        case 'ACCETTATO':
            statoBadge.classList.add('green-storico');
            break;
        
        default:
            statoBadge.classList.add('orange-storico');
    }
});

