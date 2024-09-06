document.addEventListener("DOMContentLoaded", function() {
    /**
     * Bottone per tornare indietro.
     * @type {HTMLElement}
     */
    const backButton = document.getElementById("back-button");

    /**
     * Tipo di utente loggato.
     * @type {string}
     */
    const userType = document.querySelector(".container-beige").getAttribute("data-user-type");

    /**
     * Gestisce il redirect in base al tipo di utente.
     */
    backButton.addEventListener("click", function() {
        if (userType === "TECNICO") {
            window.location.href = "homepagetecnico";
        } else if (userType === "ORDINANTE") {
            window.location.href = "homepageordinante";
        }
    });
});