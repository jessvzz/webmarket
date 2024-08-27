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
    
    caratteristicaItem.appendChild(caratteristicaInput);
    caratteristicaItem.appendChild(removeButton);
    caratteristicaItem.appendChild(addButton);
 

    container.appendChild(caratteristicaItem);
});

