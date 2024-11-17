const API_URL = `http://localhost:8080/drivers`;

async function loadDrivers() {
    const response = await fetch(API_URL);
    const drivers = await response.json();
    const list = document.getElementById('driver-table');
    list.innerHTML = drivers.length > 0 ? drivers.map(driver => `
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
    `).join('') : `<tr><td colspan="4">Nenhum Motorista encontrado no banco de dados!</td></tr>`;
}

window.onload = loadDrivers;

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

    const newDriver = {
        name: document.getElementById('name').value,
        cnh: document.getElementById('cnh').value,
        phone: document.getElementById('phone').value
    };

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newDriver)
        });

        if (response.ok) {
            document.getElementById('success-message').style.display = 'block';
            setTimeout(() => document.getElementById('success-message').style.display = 'none', 3000);
            closeModal();
            loadDrivers();
        } else {
            throw new Error('Falha ao adicionar motorista.');
        }
    } catch (error) {
        alert('Erro ao adicionar motorista: ' + error.message);
    }
});

function editDriver(id) {
    document.getElementById(`name-${id}`).disabled = false;
    document.getElementById(`cnh-${id}`).disabled = false;
    document.getElementById(`phone-${id}`).disabled = false;

    document.querySelector(`#driver-table tr td button[onclick="saveDriver(${id})"]`).style.display = 'inline-block';
    document.querySelector(`#driver-table tr td button[onclick="editDriver(${id})"]`).style.display = 'none';
}

async function saveDriver(id) {
    const driverData = {
        name: document.getElementById(`name-${id}`).value,
        cnh: document.getElementById(`cnh-${id}`).value,
        phone: document.getElementById(`phone-${id}`).value
    };

    await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(driverData)
    });

    loadDrivers();
}

async function deleteDriver(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });

        if (response.ok) {
            alert('Motorista deletado com sucesso!');
            loadDrivers();
        } else {
            alert('Erro ao deletar o motorista!');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao conectar com o servidor!');
    }
}
