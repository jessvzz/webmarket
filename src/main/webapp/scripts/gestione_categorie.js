// impl ricerca
document.getElementById('search').addEventListener('input', function() {
    var searchTerm = this.value.toLowerCase(); 
    var categories = document.querySelectorAll('.category-item');
    categories.forEach(function(category) {
        var categoryName = category.textContent.toLowerCase(); 
        if (categoryName.includes(searchTerm)) {
            category.style.display = ''; 
        } else {
            category.style.display = 'none'; 
        }
    });
});

