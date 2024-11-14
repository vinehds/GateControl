const apiUrl = 'http://localhost:8080'; // URL base para sua API

// Função para exibir todos os Concierges
async function loadConcierges() {
    const response = await fetch(`${apiUrl}/concierges`);
    const concierges = await response.json();
    const list = document.getElementById('concierges-list');
    list.innerHTML = ''; // Limpar a lista
    concierges.forEach(concierge => {
        list.innerHTML += `<li>ID: ${concierge.id}, Nome: ${concierge.name}, Local: ${concierge.location}</li>`;
    });
}

// Função para adicionar um novo Concierge
async function addConcierge() {
    const name = document.getElementById('concierge-name').value;
    const location = document.getElementById('concierge-location').value;
    const response = await fetch(`${apiUrl}/concierges`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, location }),
    });

    if (response.ok) {
        loadConcierges(); // Recarregar a lista de concierges
    }
}

// Função para excluir um Concierge
async function deleteConcierge(id) {
    const response = await fetch(`${apiUrl}/concierges/${id}`, {
        method: 'DELETE',
    });

    if (response.ok) {
        loadConcierges(); // Recarregar a lista de concierges
    }
}

window.onload = () => {
    loadConcierges(); // Carregar os concierges quando a página carregar
};
