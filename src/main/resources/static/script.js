const API_URL = 'http://localhost:8080'; // URL base para sua API

// Função para exibir todos os Concierges
async function loadConcierges() {

    const response = await fetch(`${API_URL}/concierges`);
    const concierges = await response.json();
    const list = document.getElementById('concierges-list');

    list.innerHTML = ''; // Limpar a lista

    if(concierges.length > 0){
        concierges.forEach(concierge => {
            list.innerHTML += `<li>ID: ${concierge.id}, Nome: ${concierge.name}, Local: ${concierge.location}</li>`;
        });
    } else {
        list.innerHTML += `<li>Nenhum registro encontrado</li>`;
    }
}

window.onload = () => {
    loadConcierges(); // Carregar os concierges quando a página carregar
};
