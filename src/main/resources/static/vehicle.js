const API_URL = `http://localhost:8080/vehicles`;

async function loadVehicles() {
    const response = await fetch(API_URL);
    const vehicles = await response.json();

    const driversResp = await fetch(`http://localhost:8080/drivers`);
    const drivers = await driversResp.json();

    const list = document.getElementById('vehicle-table');
    list.innerHTML = '';

    if (vehicles.length > 0) {
        vehicles.forEach(vehicle => {
            const driverOptions = drivers.map(driver => {
                return `<option value="${driver.id}" ${driver.id === vehicle.owner.id ? 'selected' : ''}>${driver.name}</option>`;
            }).join('');

            list.innerHTML += `
            <tr id="vehicle-row-${vehicle.id}">
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

window.onload = loadVehicles;

async function editVehicle(id) {
    document.getElementById(`owner-${id}`).disabled = false;
    document.getElementById(`plate-${id}`).disabled = false;
    document.getElementById(`model-${id}`).disabled = false;
    document.getElementById(`color-${id}`).disabled = false;

    const saveButton = document.querySelector(`#vehicle-table tr td button[onclick="saveVehicle(${id})"]`);
    saveButton.style.display = 'inline-block';

    const editButton = document.querySelector(`#vehicle-table tr td button[onclick="editVehicle(${id})"]`);
    editButton.style.display = 'none';
}

async function saveVehicle(id) {
    const ownerId = document.getElementById(`owner-${id}`).value;
    const plate = document.getElementById(`plate-${id}`).value;
    const model = document.getElementById(`model-${id}`).value;
    const color = document.getElementById(`color-${id}`).value;

    const vehicleData = {
        owner: { id: ownerId },
        plate: plate,
        model: model,
        color: color
    };

    await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(vehicleData)
    });

    updateVehicleRow(id, vehicleData);

    document.getElementById(`owner-${id}`).disabled = true;
    document.getElementById(`plate-${id}`).disabled = true;
    document.getElementById(`model-${id}`).disabled = true;
    document.getElementById(`color-${id}`).disabled = true;

    const saveButton = document.querySelector(`#vehicle-table tr td button[onclick="saveVehicle(${id})"]`);
    saveButton.style.display = 'none';
    const editButton = document.querySelector(`#vehicle-table tr td button[onclick="editVehicle(${id})"]`);
    editButton.style.display = 'inline-block';
}

function updateVehicleRow(id, vehicleData) {
    const row = document.querySelector(`#vehicle-row-${id}`);
    row.querySelector(`#plate-${id}`).value = vehicleData.plate;
    row.querySelector(`#model-${id}`).value = vehicleData.model;
    row.querySelector(`#color-${id}`).value = vehicleData.color;
    row.querySelector(`#owner-${id}`).value = vehicleData.owner.name;
}

async function deleteVehicle(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
        });

        if (response.ok) {
            alert('Veículo deletado com sucesso!');
            loadVehicles();
        } else {
            alert('Erro ao deletar o Veículo!');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao conectar com o servidor!');
    }
}

function openModal() {
    document.getElementById('modal').style.display = 'block';
    loadDrivers();
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
}

async function loadDrivers() {
    const response = await fetch(`http://localhost:8080/drivers`);
    const drivers = await response.json();

    const ownerSelect = document.getElementById('owner');
    ownerSelect.innerHTML = '';

    drivers.forEach(driver => {
        const option = document.createElement('option');
        option.value = driver.id;
        option.textContent = driver.name;
        ownerSelect.appendChild(option);
    });
}

document.getElementById('vehicle-form').addEventListener('submit', async function (e) {
    e.preventDefault();

    const plate = document.getElementById('plate').value;
    const model = document.getElementById('model').value;
    const color = document.getElementById('color').value;
    const ownerId = document.getElementById('owner').value;

    const vehicleData = {
        owner: { id: ownerId },
        plate: plate,
        model: model,
        color: color
    };

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(vehicleData)
        });

        if (response.status === 201) {
            document.getElementById('success-message').style.display = 'block';
            setTimeout(() => {
                document.getElementById('success-message').style.display = 'none';
            }, 3000);

            loadDrivers();
            closeModal();
        } else {
            throw new Error('Falha ao adicionar veículo.');
        }
    } catch (error) {
        alert('Erro ao adicionar veículo: ' + error.message);
    }
});

document.getElementById('btn-create').addEventListener('click', openModal);
document.getElementById('close-modal').addEventListener('click', closeModal);
