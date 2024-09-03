document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    
    if (form) {
        form.addEventListener('submit', function(event) {
            event.preventDefault(); 
            showAlert();
        });
    }
});

function showAlert() {
    const alertBox = document.getElementById('custom-alert');
    if (!alertBox) {
        console.error("Elemento con id 'custom-alert' non trovato!");
        return;
    }
    alertBox.style.display = 'block';

    const closeButton = document.querySelector('.close-btn');
    closeButton.addEventListener('click', function() {
        window.location.href = "homepageordinante"; 
    });
}
