document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('.textfield-grey');
    const btnInvio = document.getElementById('btn-invio');
    const noteField = document.getElementById('note');

    function checkInputs() {
        let allFilled = true;
        inputs.forEach(input => {
            if (input !== noteField && input.value.trim() === '') {
                allFilled = false;
            }
        });
        if (allFilled) {
            btnInvio.classList.remove('hidden');
        } else {
            btnInvio.classList.add('hidden');
        }
    }

    inputs.forEach(input => {
        input.addEventListener('input', checkInputs);
    });

    checkInputs();
});