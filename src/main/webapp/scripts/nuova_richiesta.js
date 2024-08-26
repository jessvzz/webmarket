function loadSubCategories(parentCategoryId) {
    console.log("selected: "+parentCategoryId);
    
    if (!parentCategoryId) return;
    let chosenCategory = document.getElementById("chosen-category");
    chosenCategory.setAttribute("value", parentCategoryId);
    
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
                let selects = document.querySelectorAll('select');
                selects.forEach(select => {
                    
                        select.disabled = true;
                    
                });
                // nuovo select per le sottocategorie
                let subCategorySelect = document.createElement("select");
                subCategorySelect.className = "filter-select orange";
                subCategorySelect.setAttribute("onchange", "loadSubCategories(this.value)");

                let defaultOption = document.createElement("option");
                defaultOption.text = "Seleziona una Sottocategoria";
                defaultOption.disabled = true;
                defaultOption.selected = true;
                subCategorySelect.appendChild(defaultOption);

                // opzioni per le sottocategorie
                data.forEach(cat => {
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
