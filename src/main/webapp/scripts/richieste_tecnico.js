document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.getElementById("search-input");
    const filterSelect = document.getElementById("category");
    const richiestaContainers = document.querySelectorAll(".card-row-purple");

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