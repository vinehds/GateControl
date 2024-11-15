const API_URL = 'http://localhost:8080';

// Função para exibir todas as portarias
async function loadConcierges() {
    const response = await fetch(`${API_URL}/concierges`);
    const concierges = await response.json();
    const list = document.getElementById('concierges-list');

    list.innerHTML = ''; // Limpar a lista

    if (concierges.length > 0) {
        concierges.forEach(concierge => {
            list.innerHTML += `
            <li>
                ID: ${concierge.id}, Nome: ${concierge.name}, Local: ${concierge.location}
                <button class="delete-button" id="delete-${concierge.id}" onclick="deleteConcierge(${concierge.id})">Excluir</button>
            </li>
            `;
        });
    } else {
        list.innerHTML += `<li>Nenhuma portaria encontrada</li>`;
    }
}

window.onload = () => {
    loadConcierges(); // Carregar as portarias quando a página carregar
};

// Modal para adicionar portaria
const modalAdd = document.getElementById('addModal');
const btnAdd = document.getElementById('btnAdd');
const closeModalAdd = document.querySelector('#addModal .close');

// Abre o modal ao clicar no botão "Adicionar"
btnAdd.addEventListener('click', () => {
    modalAdd.style.display = 'flex';
});

// Fecha o modal ao clicar no botão de fechar
closeModalAdd.addEventListener('click', () => {
    modalAdd.style.display = 'none';
});

// Fecha o modal ao clicar fora da área do conteúdo
window.addEventListener('click', (event) => {
    if (event.target === modalAdd) {
        modalAdd.style.display = 'none';
    }
});

// Lógica para salvar os dados do formulário de adicionar
document.getElementById('addForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    // Captura os dados do formulário
    const name = document.getElementById('name').value;
    const location = document.getElementById('location').value;

    // Envia os dados para o backend
    await fetch(`${API_URL}/concierges`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, location })
    });

    // Fecha o modal e recarrega a lista
    modalAdd.style.display = 'none';
    loadConcierges();
});

// Modal para editar portaria
const modalEdit = document.getElementById('editModal');
const btnEdit = document.getElementById('btnUpt');
const closeModalEdit = document.querySelector('#editModal .close');

// Abre o modal ao clicar no botão "Editar"
btnEdit.addEventListener('click', () => {
    modalEdit.style.display = 'flex';
});

// Fecha o modal ao clicar no botão de fechar
closeModalEdit.addEventListener('click', () => {
    modalEdit.style.display = 'none';
});

// Fecha o modal ao clicar fora da área do conteúdo
window.addEventListener('click', (event) => {
    if (event.target === modalEdit) {
        modalEdit.style.display = 'none';
    }
});

// Lógica para salvar os dados do formulário de editar
document.getElementById('editForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    // Captura os dados do formulário
    const id = document.getElementById('id').value;
    const name = document.getElementById('editName').value;
    const location = document.getElementById('editLocation').value;

    // Envia os dados para o backend
    await fetch(`${API_URL}/concierges/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, location })
    });

    // Fecha o modal e recarrega a lista
    modalEdit.style.display = 'none';
    loadConcierges();
});

//excluir

    function deleteConcierge(id) {
        // Confirmar a exclusão
        if (!confirm(`Tem certeza que deseja excluir o concierge com ID ${id}?`)) {
            return;
        }

        // Fazer a requisição DELETE
        fetch(`${API_URL}/concierges/${id}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (response.status === 204) {
                // Remover o item da lista (atualizar a interface)
                const itemToRemove = document.getElementById(`delete-${id}`).parentElement;
                itemToRemove.remove();

                // Feedback para o usuário
                alert(`Concierge com ID ${id} foi excluído com sucesso.`);
            } else {
                throw new Error(`Erro ao excluir o concierge. Status: ${response.status}`);
            }
        })
        .catch(error => {
            console.error(error);
            alert('Não foi possível excluir o concierge.');
        });
    }

