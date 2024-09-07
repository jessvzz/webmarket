/**
 * Aggiunge un listener di input all'elemento con ID 'search' per filtrare le categorie in base al termine di ricerca.
 * @event input - Evento di input sull'elemento con ID 'search'.
 */
document.getElementById('search').addEventListener('input', function() {
    /**
     * Il termine di ricerca inserito dall'utente, convertito in minuscolo.
     * @type {string}
     */
    var searchTerm = this.value.toLowerCase(); 
    
    /**
     * Una NodeList di elementi con la classe 'category-item', ossia le singole categorie.
     * @type {NodeList}
     */
    var categories = document.querySelectorAll('.category-item');
    
    categories.forEach(function(category) {
        /**
         * Il nome della categoria, convertito in minuscolo.
         * @type {string}
         */
        var categoryName = category.textContent.toLowerCase(); 
        
        if (categoryName.includes(searchTerm)) {
            category.style.display = ''; 
        } else {
            category.style.display = 'none'; 
        }
    });
});