function loadSubCategories(parentCategoryId) {
    console.log("selected: "+parentCategoryId);
    if (!parentCategoryId) return;
    
    //questo per ora lo commento così non mi da problemi
    /*
    fetch(`nuova_richiesta?n=${parentCategoryId}`)
            .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
           
            //let subCategorySelect = document.createElement("select");
            
        })
        .catch(error => {
            console.error("Errore nel caricamento delle sottocategorie:", error);
        });
     * 
     */
}
