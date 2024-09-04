document.addEventListener("DOMContentLoaded", function() {
    const backButton = document.getElementById("back-button");
    const userType = document.querySelector(".container-beige").getAttribute("data-user-type");

    backButton.addEventListener("click", function() {
        if (userType === "TECNICO") {
            window.location.href = "homepagetecnico";
        } else if (userType === "ORDINANTE") {
            window.location.href = "homepageordinante";
        }
    });
});