const API_URL = `http://localhost:8080/drivers`;

async function loadDrivers() {
    const response = await fetch(`${API_URL}`);
    const drivers = await response.json();

    const list = document.getElementById('driver-table');
    list.innerHTML = ''; // Limpar a lista

    if (drivers.length > 0) {
        drivers.forEach(driver => {
            list.innerHTML += `
            <tr>
                <td><input class="editable" type="text" id="name-${driver.id}" value="${driver.name}" disabled /></td>
                <td><input class="editable" type="text" id="cnh-${driver.id}" value="${driver.cnh}" disabled /></td>
                <td><input class="editable" type="text" id="phone-${driver.id}" value="${driver.phone}" disabled /></td>
                <td>
                    <button onclick="editDriver(${driver.id})">Editar</button>
                    <button onclick="saveDriver(${driver.id})" style="display:none;">Salvar</button>
                    <button onclick="deleteDriver(${driver.id})">Deletar</button>
                </td>
            </tr>
            `;
        });
    } else {
        list.innerHTML = `<tr><td colspan="4">Nenhum Motorista encontrado no banco de dados!</td></tr>`;
    }
}

function openModal() {
    document.getElementById('modal').style.display = 'block';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
}

document.getElementById('btn-create').addEventListener('click', openModal);
document.getElementById('close-modal').addEventListener('click', closeModal);

document.getElementById('driver-form').addEventListener('submit', async function (e) {
    e.preventDefault();

    const name = document.getElementById('name').value;
    const cnh = document.getElementById('cnh').value;
    const phone = document.getElementById('phone').value;

    const newDriver = { name, cnh, phone };

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newDriver)
        });

        if (response.ok) {
            document.getElementById('success-message').style.display = 'block';
            setTimeout(() => {
                document.getElementById('success-message').style.display = 'none';
            }, 3000);

            closeModal(); // Fechar o modal após salvar
            loadDrivers(); // Recarregar a lista de motoristas
        } else {
            throw new Error('Falha ao adicionar motorista.');
        }
    } catch (error) {
        alert('Erro ao adicionar motorista: ' + error.message);
    }
});

loadDrivers();

function editDriver(id) {
    // Habilitar os campos para edição
    document.getElementById(`name-${id}`).disabled = false;
    document.getElementById(`cnh-${id}`).disabled = false;
    document.getElementById(`phone-${id}`).disabled = false;

    // Exibir o botão "Salvar"
    const saveButton = document.querySelector(`#driver-table tr td button[onclick="saveDriver(${id})"]`);
    saveButton.style.display = 'inline-block';

    // Esconder o botão "Editar"
    const editButton = document.querySelector(`#driver-table tr td button[onclick="editDriver(${id})"]`);
    editButton.style.display = 'none';
}

async function saveDriver(id) {
    const name = document.getElementById(`name-${id}`).value;
    const cnh = document.getElementById(`cnh-${id}`).value;
    const phone = document.getElementById(`phone-${id}`).value;

    const driverData = {
        name: name,
        cnh: cnh,
        phone: phone
    };

    // Enviar os dados para o backend para salvar as mudanças
    await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(driverData)
    });

    // Recarregar os motoristas
    loadDrivers();
}


// Chamar a função assim que a página for carregada
window.onload = loadDrivers;


async function deleteDriver(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
        });

        if (response.ok) {
            alert('Motorista deletado com sucesso!');
            loadDrivers(); // Recarregar a lista
        } else {
            alert('Erro ao deletar o motorista!');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao conectar com o servidor!');
    }
}
