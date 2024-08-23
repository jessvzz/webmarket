document.addEventListener('DOMContentLoaded', (event) => {
    const deleteButton = document.getElementById('delete-button');
    deleteButton.addEventListener('click', function(event) {
        event.preventDefault(); 

        const confirmed = confirm('Sei sicura di voler eliminare questa categoria?');
        
        if (confirmed) {
            const categoriaKey = deleteButton.getAttribute('data-key'); 
            window.location.href = 'categoria?n=' + categoriaKey + '&action=deleteCategory';
        }
    });
});
