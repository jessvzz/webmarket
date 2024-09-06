document.addEventListener("DOMContentLoaded", function() {

    /**
     * Campo di input per la ricerca.
     * @type {HTMLElement}
     */
    const searchInput = document.getElementById("search-input");

    /**
     * Select per il filtraggio delle categorie.
     * @type {HTMLElement}
     */
    const filterSelect = document.getElementById("category");

    /**
     * Contenitori delle richieste.
     * @type {NodeListOf<Element>}
     */
    const richiestaContainers = document.querySelectorAll(".card-row-purple");

    /**
     * Filtra le richieste in base al termine di ricerca e alla categoria selezionata.
     */
    function filterRichieste() {
        const searchTerm = searchInput.value.toLowerCase();
        const selectedCategory = filterSelect.value;

        richiestaContainers.forEach(container => {
            const codice = container.getAttribute("data-codice").toLowerCase();
            const categoria = container.getAttribute("data-categoria");

            const matchesSearch = codice.includes(searchTerm);
            const matchesFilter = selectedCategory === "tutte" || categoria === selectedCategory;

            if (matchesSearch && matchesFilter) {
                container.style.display = "";
            } else {
                container.style.display = "none";
            }
        });
    }

    searchInput.addEventListener("input", filterRichieste);
    filterSelect.addEventListener("change", filterRichieste);

});