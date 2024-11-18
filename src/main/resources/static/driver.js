const API_URL = `http://localhost:8080/drivers`;

async function loadDrivers() {
    const response = await fetch(API_URL);
    const drivers = await response.json();
    const list = document.getElementById('driver-table');
    list.innerHTML = drivers.length > 0 ? drivers.map(driver => `
        <tr>
            <td><input oninput="this.value = this.value.replace(/[^a-zA-ZáÁàÀâÂãÃéÉêÊíÍóÓôÔõÕúÚçÇ\\s]/g, '')" 
                class="editable" 
                type="text" 
                id="name-${driver.id}" 
                value="${driver.name}" 
                disabled /></td>
            <td><input oninput="this.value = this.value.replace(/[^0-9]/g, '')" 
                minlength="11"
                maxlength="11" 
                class="editable" 
                type="text" 
                id="cnh-${driver.id}" 
                value="${driver.cnh}" 
                disabled /></td>
            <td><input placeholder="(XX) XXXXX-XXXX"
                maxlength="15"
                minlength="15"
                oninput="formatarTelefone(this)"
                class="editable" type="text" id="phone-${driver.id}" value="${driver.phone}" disabled /></td>
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

        if (response.ok || response.status === 201) {
            document.getElementById('success-message').style.display = 'block';
            setTimeout(() => document.getElementById('success-message').style.display = 'none', 3000);
            closeModal();
            await loadDrivers();
        }
        else {
            alert('Erro: Motorista com a CNH inserida já cadastrado!')
        }
    } catch (error) {
        alert('Erro ' + error);
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

    const  resp = fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(driverData)
    });

    if((await resp).ok){
        alert('Motorista atualizado!')
    }
    else{
        alert('Motorista com a CNH inserida já registrada no sistema!')
    }

    await loadDrivers();
}

async function deleteDriver(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });

        if (response.ok) {
            alert('Motorista deletado com sucesso!');
            await loadDrivers();
        } else {
            alert(`Este Motorista não pode ser excluído porque está associado a outros registros no sistema.`);
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao conectar com o servidor!');
    }
}
function formatarTelefone(input) {
    let valor = input.value.replace(/\D/g, ''); // Remove tudo que não for dígito
    valor = valor.replace(/^(\d{2})(\d)/, '($1) $2'); // Adiciona o parênteses no DDD
    valor = valor.replace(/(\d{5})(\d)/, '$1-$2'); // Adiciona o hífen no número
    input.value = valor.substring(0, 15); // Limita a 15 caracteres
}
