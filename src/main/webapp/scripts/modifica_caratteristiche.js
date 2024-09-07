/**
 * Aggiunge un listener di click all'elemento con ID 'add-caratteristica' per aggiungere una nuova caratteristica.
 * @event click - Evento di click sull'elemento con ID 'add-caratteristica'.
 */
document.getElementById('add-caratteristica').addEventListener('click', function () {
    /**
     * Contenitore delle caratteristiche.
     * @type {HTMLElement}
     */
    const container = document.getElementById('caratteristiche-container');

    /**
     * Elemento div che rappresenta una nuova caratteristica.
     * @type {HTMLDivElement}
     */
    const caratteristicaItem = document.createElement('div');
    caratteristicaItem.className = 'caratteristica-item';

    /**
     * Input per inserire il nome di una nuova caratteristica.
     * @type {HTMLInputElement}
     */
    const caratteristicaInput = document.createElement('input');
    caratteristicaInput.type = 'text';
    caratteristicaInput.name = 'nuova-caratteristica'; 
    caratteristicaInput.placeholder = 'Inserisci una caratteristica';

    caratteristicaInput.classList.add('flex-1', 'border', 'rounded-extra-large', 'p-2');

    /**
     * Bottone per rimuovere la caratteristica.
     * @type {HTMLButtonElement}
     */
    const removeButton = document.createElement('button');
    removeButton.className = 'cancel-btn';
    removeButton.innerHTML = '✖';
    removeButton.type = 'button';
    
    /**
     * Bottone per aggiungere la caratteristica.
     * @type {HTMLButtonElement}
     */
    const addButton = document.createElement('button');
    addButton.className = 'cancel-btn';
    addButton.innerHTML = 'ok';
    addButton.type = 'submit';
    

    /**
     * Aggiunge un listener di click al bottone di rimozione per rimuovere la caratteristica.
     * @event click - Evento di click sul bottone di rimozione.
     */
    removeButton.addEventListener('click', function () {
        container.removeChild(caratteristicaItem);
    });
    
    
    caratteristicaItem.appendChild(caratteristicaInput);
    caratteristicaItem.appendChild(removeButton);
    caratteristicaItem.appendChild(addButton);
 

    container.appendChild(caratteristicaItem);
});