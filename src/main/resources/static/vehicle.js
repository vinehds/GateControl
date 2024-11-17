const API_URL = `http://localhost:8080/vehicles`;

// Carregar os veículos na tabela
async function loadVehicles() {
    const response = await fetch(API_URL);
    const vehicles = await response.json();

    // Requisição para obter os motoristas
    const driversResp = await fetch(`http://localhost:8080/drivers`);
    const drivers = await driversResp.json();

    const list = document.getElementById('vehicle-table');
    list.innerHTML = ''; // Limpar a lista

    if (vehicles.length > 0) {
        vehicles.forEach(vehicle => {
            // Gerar as opções do select de motoristas
            const driverOptions = drivers.map(driver => {
                return `<option value="${driver.id}" ${driver.id === vehicle.owner.id ? 'selected' : ''}>${driver.name}</option>`;
            }).join('');

            list.innerHTML += `
            <tr>
                <td>
                    <select class="editable" id="owner-${vehicle.id}" disabled>
                        ${driverOptions}
                    </select>
                </td>
                <td><input class="editable" type="text" id="plate-${vehicle.id}" value="${vehicle.plate}" disabled /></td>
                <td><input class="editable" type="text" id="model-${vehicle.id}" value="${vehicle.model}" disabled /></td>
                <td><input class="editable" type="text" id="color-${vehicle.id}" value="${vehicle.color}" disabled /></td>
                <td>
                    <button onclick="editVehicle(${vehicle.id})">Editar</button>
                    <button onclick="saveVehicle(${vehicle.id})" style="display:none;">Salvar</button>
                    <button onclick="deleteVehicle(${vehicle.id})">Deletar</button>
                </td>
            </tr>
            `;
        });
    } else {
        list.innerHTML = `<tr><td colspan="5">Nenhum Veículo encontrado no banco de dados!</td></tr>`;
    }
}

// Chamar a função assim que a página for carregada
window.onload = loadVehicles;

// Função para editar veículo
async function editVehicle(id) {
    // Habilitar os campos para edição
    document.getElementById(`owner-${id}`).disabled = false;
    document.getElementById(`plate-${id}`).disabled = false;
    document.getElementById(`model-${id}`).disabled = false;
    document.getElementById(`color-${id}`).disabled = false;

    // Exibir o botão "Salvar"
    const saveButton = document.querySelector(`#vehicle-table tr td button[onclick="saveVehicle(${id})"]`);
    saveButton.style.display = 'inline-block';

    // Esconder o botão "Editar"
    const editButton = document.querySelector(`#vehicle-table tr td button[onclick="editVehicle(${id})"]`);
    editButton.style.display = 'none';
}

// Função para salvar alterações do veículo
async function saveVehicle(id) {
    const ownerId = document.getElementById(`owner-${id}`).value; // Pegando apenas o id do motorista
    const plate = document.getElementById(`plate-${id}`).value;
    const model = document.getElementById(`model-${id}`).value;
    const color = document.getElementById(`color-${id}`).value;

    const vehicleData = {
        owner: { id: ownerId },
        plate: plate,
        model: model,
        color: color
    };

    // Enviar os dados para o backend para salvar as mudanças
    await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(vehicleData)
    });

    // Recarregar os veículos
    loadVehicles();
}

// Função para deletar veículo
async function deleteVehicle(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
        });

        if (response.ok) {
            alert('Veículo deletado com sucesso!');
            loadVehicles(); // Recarregar a lista de veículos após exclusão
        } else {
            alert('Erro ao deletar o Veículo!');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao conectar com o servidor!');
    }
}

// Função para abrir o modal de adição de veículo
function openModal() {
    document.getElementById('modal').style.display = 'block'; // Exibir o modal
    loadDrivers(); // Carregar os motoristas para o select no modal
}

// Função para fechar o modal
function closeModal() {
    document.getElementById('modal').style.display = 'none'; // Esconder o modal
}

// Função para carregar motoristas no modal
async function loadDrivers() {
    const response = await fetch(`http://localhost:8080/drivers`);
    const drivers = await response.json();

    const ownerSelect = document.getElementById('owner');
    ownerSelect.innerHTML = ''; // Limpar as opções anteriores

    drivers.forEach(driver => {
        const option = document.createElement('option');
        option.value = driver.id;
        option.textContent = driver.name;
        ownerSelect.appendChild(option);
    });
}

// Função para adicionar um novo veículo
async function addVehicle() {
    const plate = document.getElementById('plate').value;
    const model = document.getElementById('model').value;
    const color = document.getElementById('color').value;
    const ownerId = document.getElementById('owner').value; // Pegando o id do motorista selecionado

    const vehicleData = {
        owner: { id: ownerId },
        plate: plate,
        model: model,
        color: color
    };

    // Enviar os dados para o backend para adicionar o novo veículo
    await fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(vehicleData)
    });

    // Fechar o modal após adicionar
    closeModal()

    // Recarregar a lista de veículos
    loadVehicles();
}

// Associar evento ao botão de abrir modal
document.getElementById('btn-create').addEventListener('click', openModal);
document.getElementById('close-modal').addEventListener('click', closeModal);
