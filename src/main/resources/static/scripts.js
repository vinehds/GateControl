const API_URL = 'http://localhost:8080/records';
const VEHICLES_BY_DRIVER_URL = 'http://localhost:8080/vehicles/findbydriver';

async function loadRecords() {
    try {
        const response = await fetch(`${API_URL}`);
        const records = await response.json();
        const driversResponse = await fetch('/drivers');
        const drivers = await driversResponse.json();

        const list = document.getElementById('records-list');
        list.innerHTML = '';

        if (records.length > 0) {
            records.forEach(record => {
                const driverOptions = drivers
                    .map(driver => `<option value="${driver.id}" ${driver.name === record.driver.name ? 'selected' : ''}>${driver.name}</option>`)
                    .join('');

                list.innerHTML += `
            <tr id="row-${record.id}">
                <td>
                    <select class="editable" id="driver-${record.id}" onchange="updateVehicleOptions(${record.id})" disabled>
                        ${driverOptions}
                    </select>
                </td>
                <td>
                    <select class="editable" id="plate-${record.id}" disabled>
                        <option value="${record.vehicle.plate}">${record.vehicle.plate}</option>
                    </select>
                </td>
                <td>
                    <input type="datetime-local" value="${formatDateForInput(record.date)}" class="editable" id="date-${record.id}" disabled>
                </td>
                <td>
                    <select class="editable" id="recordType-${record.id}" disabled>
                        <option value="IN" ${record.recordType === 'IN' ? 'selected' : ''}>IN</option>
                        <option value="OUT" ${record.recordType === 'OUT' ? 'selected' : ''}>OUT</option>
                    </select>
                </td>
                <td><input type="text" value="${record.remark}" class="editable" id="remark-${record.id}" disabled></td>
                <td>
                    <button class="btn btn-edit" id="edit-${record.id}" onclick="enableEdit(${record.id})">Editar</button>
                    <button class="btn btn-save" id="save-${record.id}" onclick="saveRecord(${record.id})" style="display: none;">Salvar</button>
                    <button class="delete-button" id="delete-${record.id}" onclick="deleteRecord(${record.id})">Excluir</button>
                </td>
            </tr>
            `;
            });
        } else {
            list.innerHTML += `<p>Nenhum registro encontrado no banco de dados!</p>`;
        }
    } catch (error) {
        console.error('Erro ao carregar registros ou motoristas:', error);
    }
}

function formatDateForInput(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

async function updateVehicleOptions(recordId) {
    const driverId = document.getElementById(`driver-${recordId}`).value;
    const plateSelect = document.getElementById(`plate-${recordId}`);
    plateSelect.innerHTML = '<option value="">Selecione a Placa</option>';

    if (driverId) {
        try {
            const response = await fetch(`${VEHICLES_BY_DRIVER_URL}/${driverId}`);
            const vehicles = await response.json();
            plateSelect.innerHTML += vehicles
                .map(vehicle => `<option value="${vehicle.plate}">${vehicle.plate}</option>`)
                .join('');
        } catch (error) {
            console.error('Erro ao carregar veículos:', error);
        }
    }
}

function enableEdit(recordId) {
    document.querySelectorAll(`#row-${recordId} .editable`).forEach(input => {
        input.disabled = false;
    });
    document.getElementById(`edit-${recordId}`).style.display = 'none';
    document.getElementById(`save-${recordId}`).style.display = 'inline-block';
    updateVehicleOptions(recordId);
}

async function saveRecord(recordId) {
    const updatedRecord = {
        recordType: document.getElementById(`recordType-${recordId}`).value,
        vehicle: {plate: document.getElementById(`plate-${recordId}`).value},
        driver: {id: document.getElementById(`driver-${recordId}`).value},
        remark: document.getElementById(`remark-${recordId}`).value
    };

    try {
        const response = await fetch(`${API_URL}/${recordId}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(updatedRecord)
        });

        if (response.ok) {
            alert('Registro atualizado com sucesso!');
            document.querySelectorAll(`#row-${recordId} .editable`).forEach(input => {
                input.disabled = true;
            });
            document.getElementById(`edit-${recordId}`).style.display = 'inline-block';
            document.getElementById(`save-${recordId}`).style.display = 'none';
        } else {
            const error = await response.json();
            alert(`Erro ao atualizar registro: ${error.message}`);
        }
    } catch (error) {
        console.error('Erro ao salvar registro:', error);
        alert('Erro ao salvar registro!');
    }
}

async function deleteRecord(id) {
    const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });

    if (response.ok) {
        loadRecords();
    }
}

function openAddRecordPopup() {
    document.getElementById('popup').style.display = 'flex';
    loadDrivers();
}

function closeAddRecordPopup() {
    document.getElementById('popup').style.display = 'none';
}

async function loadDrivers() {
    const response = await fetch('/drivers');
    const drivers = await response.json();
    const driverSelect = document.getElementById('driverId');
    driverSelect.innerHTML = '<option value="">Selecione o Motorista</option>';

    drivers.forEach(driver => {
        const option = document.createElement('option');
        option.value = driver.id;
        option.textContent = driver.name;
        driverSelect.appendChild(option);
    });
}

async function loadDriverVehicles() {
    const driverId = document.getElementById('driverId').value;
    const vehicleSelect = document.getElementById('vehiclePlate');
    vehicleSelect.innerHTML = '<option value="">Selecione a Placa</option>';

    if (driverId) {
        const response = await fetch(`${VEHICLES_BY_DRIVER_URL}/${driverId}`);
        const vehicles = await response.json();

        vehicles.forEach(vehicle => {
            const option = document.createElement('option');
            option.value = vehicle.plate;
            option.textContent = vehicle.plate;
            vehicleSelect.appendChild(option);
        });
    }
}

async function addRecord() {
    const driverId = document.getElementById('driverId').value;
    const vehiclePlate = document.getElementById('vehiclePlate').value;
    const recordType = document.getElementById('recordType').value;
    const date = document.getElementById('date').value;
    const formattedDate = new Date(date).toISOString();
    const remark = document.getElementById('remark').value;

    try {
        const vehicleResponse = await fetch(`http://localhost:8080/vehicles/plate/${vehiclePlate}`);
        if (!vehicleResponse.ok) {
            alert('Erro ao buscar veículo! Verifique a placa e tente novamente.');
            return;
        }

        const vehicle = await vehicleResponse.json();
        const driverResponse = await fetch(`http://localhost:8080/drivers/${driverId}`);
        if (!driverResponse.ok) {
            alert('Erro ao buscar Motorista! Verifique a placa e tente novamente.');
            return;
        }

        const driver = await driverResponse.json();

        const newRecord = {
            recordType: recordType,
            date: formattedDate,
            vehicle: vehicle,
            driver: driver,
            remark: remark,
        };

        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(newRecord),
        });

        if (response.ok) {
            alert('Novo registro adicionado com sucesso!');
            closeAddRecordPopup();
            loadRecords();
        } else {
            alert('Erro ao adicionar o registro!');
        }
    } catch (error) {
        console.error('Erro na requisição:', error);
        alert('Erro na requisição ao servidor!');
    }
}

window.onload = () => {
    loadRecords();
};
