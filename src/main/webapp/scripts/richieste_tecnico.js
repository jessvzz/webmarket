document.addEventListener("DOMContentLoaded", function() {
    
    //prendo elementi di ricerca e filtro
    const searchInput = document.getElementById('search');
    
    //container delle proposte
    const proposals = document.querySelectorAll('#richiesta-container');

    function filterProposte() {
        console.log('ciao');
        //prendo valori nella ricerca e nel select (minuscoli)
        const searchTerm = searchInput.value.toLowerCase();

        proposals.forEach(function(proposal) {
            //prendo valori degli attributi
            const codice = proposal.getAttribute('data-codice').toLowerCase();

            // filtro per codice (ricerca)
            //il codice deve includere quello che ho nella ricerca
            const matchCodice = codice.includes(searchTerm);
            
            if (matchCodice) {
                proposal.style.display = '';
            } else {
                proposal.style.display = 'none';
            }
        });
    }
    
    // filtro search
    searchInput.addEventListener('input', filterProposte);
});