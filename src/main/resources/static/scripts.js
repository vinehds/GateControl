const API_URL = 'http://localhost:8080/records'; // URL base para registros
const VEHICLES_BY_DRIVER_URL = 'http://localhost:8080/vehicles/findbydriver'; // URL para buscar veículos por motorista

// Função para exibir todos os registros de veículos e motoristas
async function loadRecords() {
    try {
        const response = await fetch(`${API_URL}`);
        const records = await response.json();
        const driversResponse = await fetch('/drivers');
        const drivers = await driversResponse.json();

        const list = document.getElementById('records-list');
        list.innerHTML = ''; // Limpar a lista

        if (records.length > 0) {
            records.forEach(record => {
                // Criar opções do select de motoristas
                const driverOptions = drivers
                    .map(driver => `<option value="${driver.id}" ${driver.name === record.driver.name ? 'selected' : ''}>${driver.name}</option>`)
                    .join('');

                // Criar a linha da tabela
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

// Função para formatar a data no formato do input datetime-local
function formatDateForInput(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Mês começa do 0
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

// Função para atualizar as opções de placas de veículos com base no motorista selecionado
async function updateVehicleOptions(recordId) {
    const driverId = document.getElementById(`driver-${recordId}`).value;
    const plateSelect = document.getElementById(`plate-${recordId}`);

    // Limpar placas anteriores
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

// Função para habilitar a edição dos campos
function enableEdit(recordId) {
    document.querySelectorAll(`#row-${recordId} .editable`).forEach(input => {
        input.disabled = false;
    });
    document.getElementById(`edit-${recordId}`).style.display = 'none';
    document.getElementById(`save-${recordId}`).style.display = 'inline-block';

    // Atualizar veículos disponíveis com base no motorista selecionado
    updateVehicleOptions(recordId);
}

// Função para salvar o registro editado
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

// Função para excluir um registro
async function deleteRecord(id) {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
    });

    if (response.ok) {
        loadRecords(); // Recarregar a lista de registros
    }
}

function openAddRecordPopup() {
    document.getElementById('popup').style.display = 'flex';
    loadDrivers();  // Carregar motoristas ao abrir o popup
}

// Função para fechar o popup de adicionar novo registro
function closeAddRecordPopup() {
    document.getElementById('popup').style.display = 'none';
}

async function loadDrivers() {
    const response = await fetch('/drivers'); // URL para carregar os motoristas
    const drivers = await response.json();
    const driverSelect = document.getElementById('driverId');

    driverSelect.innerHTML = '<option value="">Selecione o Motorista</option>'; // Limpar as opções existentes

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

    // Limpar placas anteriores
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

//add
async function addRecord() {
    const driverId = document.getElementById('driverId').value;
    const vehiclePlate = document.getElementById('vehiclePlate').value;
    const recordType = document.getElementById('recordType').value;
    const date = document.getElementById('date').value;
    const formattedDate = new Date(date).toISOString();
    const remark = document.getElementById('remark').value;

    try {
        // Busca o veículo pelo número da placa
        const vehicleResponse = await fetch(`http://localhost:8080/vehicles/plate/${vehiclePlate}`);
        if (!vehicleResponse.ok) {
            alert('Erro ao buscar veículo! Verifique a placa e tente novamente.');
            return;
        }

        const vehicle = await vehicleResponse.json(); // Obtem o veículo

        const driverResponse = await fetch(`http://localhost:8080/drivers/${driverId}`);
        if (!driverResponse.ok) {
            alert('Erro ao buscar Motorista! Verifique a placa e tente novamente.');
            return;
        }

        const driver = await driverResponse.json(); // Obtem o driver

        // Monta o objeto conforme o formato esperado
        const newRecord = {
            recordType: recordType,
            date: formattedDate,
            vehicle: vehicle,
            driver: driver,
            remark: remark,
        };

        // Envia o registro final ao servidor
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newRecord),
        });

        if (response.ok) {
            alert('Novo registro adicionado com sucesso!');
            closeAddRecordPopup(); // Fechar o popup após sucesso
            loadRecords(); // Recarregar a lista de registros
        } else {
            alert('Erro ao adicionar o registro!');
        }
    } catch (error) {
        console.error('Erro na requisição:', error);
        alert('Erro na requisição ao servidor!');
    }
}


// Carregar os registros quando a página carregar
window.onload = () => {
    loadRecords();
};
