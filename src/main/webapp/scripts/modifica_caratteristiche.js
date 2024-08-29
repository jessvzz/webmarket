function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

document.getElementById('add-caratteristica').addEventListener('click', function () {
    const container = document.getElementById('caratteristiche-container');

    const caratteristicaItem = document.createElement('div');
    caratteristicaItem.className = 'caratteristica-item';

    const caratteristicaInput = document.createElement('input');
    caratteristicaInput.type = 'text';
    caratteristicaInput.name = 'nuova-caratteristica'; 
    caratteristicaInput.placeholder = 'Inserisci una caratteristica';

    const removeButton = document.createElement('button');
    removeButton.className = 'cancel-btn';
    removeButton.innerHTML = '✖';
    removeButton.type = 'button';
    
    const addButton = document.createElement('button');
    addButton.className = 'cancel-btn';
    addButton.innerHTML = 'ok';
    addButton.type = 'button';
    

    removeButton.addEventListener('click', function () {
        container.removeChild(caratteristicaItem);
    });
    
    const categoriaId = getQueryParam('n');

    addButton.addEventListener('click', function () {
     const container = document.getElementById('caratteristiche-container');
     const caratteristicaInput = container.querySelector('input[name="nuova-caratteristica"]');

     const formData = new FormData();
     formData.append("action", "createCaratteristica");
     formData.append("nuova-caratteristica", caratteristicaInput.value);
     formData.append("n", categoriaId); 

     fetch("gestisci_caratteristiche", {
         method: "POST",
         body: formData
     }).then(response => {
         if (response.ok) {
             window.location.href = "gestisci_caratteristiche?n=" + categoriaId;
         } else {
             console.error("Failed to create characteristic");
         }
     });
     });

    
    caratteristicaItem.appendChild(caratteristicaInput);
    caratteristicaItem.appendChild(removeButton);
    caratteristicaItem.appendChild(addButton);
 

    container.appendChild(caratteristicaItem);
});

