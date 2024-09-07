/**
 * Carica le sottocategorie in base all'ID della categoria principale selezionata.
 * @param {string} parentCategoryId - L'ID della categoria principale selezionata.
 */
function loadSubCategories(parentCategoryId) {
    console.log("selected: "+parentCategoryId);
    
    if (!parentCategoryId) return;
    /**
     * Elemento HTML per la categoria scelta.
     * @type {HTMLElement}
     */
    let chosenCategory = document.getElementById("chosen-category");
    chosenCategory.setAttribute("value", parentCategoryId);
    
    
    /**
     * Contenitore per le sottocategorie.
     * @type {HTMLElement}
     */
    let container = document.getElementById("subcategories-container");
    
   
    fetch(`nuova_richiesta?n=${parentCategoryId}`)
            .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.length > 0) {
                /**
                 * NodeList di elementi select nel documento.
                 * @type {NodeList}
                 */
                let selects = document.querySelectorAll('select');
                selects.forEach(select => {
                    
                        select.disabled = true;
                    
                });
                /**
                 * Elemento select per le sottocategorie.
                 * @type {HTMLSelectElement}
                 */
                let subCategorySelect = document.createElement("select");
                subCategorySelect.classList.add('standard-select', 'mb-4');
                subCategorySelect.setAttribute("onchange", "loadSubCategories(this.value)");

                /**
                 * Opzione predefinita per il select delle sottocategorie.
                 * @type {HTMLOptionElement}
                 */
                let defaultOption = document.createElement("option");
                defaultOption.text = "Seleziona una Sottocategoria";
                defaultOption.disabled = true;
                defaultOption.selected = true;
                subCategorySelect.appendChild(defaultOption);

                data.forEach(cat => {
                    /**
                     * Opzione per una sottocategoria.
                     * @type {HTMLOptionElement}
                     */
                    let option = document.createElement("option");
                    option.value = cat.key;
                    option.text = cat.nome;
                    subCategorySelect.appendChild(option);
                });

                
                container.appendChild(subCategorySelect);
            }
        })
        .catch(error => {
            console.error("Errore nel caricamento delle sottocategorie:", error);
        });
     
     

}