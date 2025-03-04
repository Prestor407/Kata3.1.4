document.addEventListener('DOMContentLoaded', () => {
    initAdminPage()
})

async function initAdminPage() {
    await loadCurrentUser()
    await loadRoles()
    await loadUsers()
    setupEventListeners()
    setupModals()
}

async function loadCurrentUser() {
    const response = await fetch('/api/admin/current');
    const user = await response.json();
    document.getElementById('current-user').textContent =
        `${user.username} with roles: (${user.roles.map(r => r.name).join(', ')})`
}

async function loadUsers() {
    const response = await fetch('/api/admin/')
    const users = await response.json()
    displayUsers(users)
}

async function loadRoles() {
    const response = await fetch('/api/admin/roles')
    const roles = await response.json()
    populateRoleSelect(roles)
}

function populateRoleSelect(roles) {
    const selects = document.querySelectorAll('.role-select')
    selects.forEach(select => {
        select.innerHTML = roles.map(role =>
            `<option value = "${role.id}">${role.name}</option>`).join('')
    })
}

function displayUsers(users) {
    const tbody = document.getElementById('users-table-body');
    tbody.innerHTML = '';

    users.forEach(user => {
        const row = `
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.roles.map(r => r.name).join(', ')}</td>
                <td>
                    <button class="btn btn-info edit-btn" 
                            data-bs-toggle="modal"
                            data-bs-target="#editModal"
                            data-userid="${user.id}"
                            data-username="${user.username}"
                            data-age="${user.age}"
                            data-email="${user.email}"
                            data-roles="${user.roles.map(r => r.id).join(',')}">
                        Edit
                    </button>
                </td>
                <td>
                    <button class="btn btn-danger delete-btn" 
                            data-bs-toggle="modal"
                            data-bs-target="#deleteModal"
                            data-userid="${user.id}"
                            data-username="${user.username}"
                            data-age="${user.age}"
                            data-email="${user.email}"
                            data-roles="${user.roles.map(r => r.id).join(',')}">
                        Delete
                    </button>
                </td>
            </tr>
        `;
        tbody.insertAdjacentHTML('beforeend', row)
    });

}

// Настройка модальных окон
function setupModals() {
    const editModal = document.getElementById('editModal');
    editModal.addEventListener('show.bs.modal', event => {
        const button = event.relatedTarget;
        const roles = button.dataset.roles.split(',').map(id => parseInt(id))

        document.getElementById('editId').value = button.dataset.userid;
        document.getElementById('editUsername').value = button.dataset.username;
        document.getElementById('editAge').value = button.dataset.age;
        document.getElementById('editEmail').value = button.dataset.email;

        // Выбор ролей
        Array.from(document.getElementById('editRole').options).forEach(option => {
            option.selected = roles.includes(parseInt(option.value));
        })
    })

    const deleteModal = document.getElementById('deleteModal');
    deleteModal.addEventListener('show.bs.modal', event => {
        const button = event.relatedTarget;
        const roleIds = button.dataset.roles.split(',').map(id => parseInt(id))

        document.getElementById('deleteId').value = button.dataset.userid;
        document.getElementById('deleteUsername').value = button.dataset.username;
        document.getElementById('deleteAge').value = button.dataset.age;
        document.getElementById('deleteEmail').value = button.dataset.email;
        fetch('api/admin/roles')
            .then(response => response.json())
            .then(roles => {
                const roleNames = roles.filter(role => roleIds.includes(role.id))
                    .map(role => role.name)
                    .join(', ')
                document.getElementById('deleteRole').value = roleNames
            })
        })
}

// Обработчики событий
function setupEventListeners() {
    // Создание пользователя
    document.getElementById('create-user-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = getFormData('create-user-form');
        await addUser(formData);
    })

    // Редактирование пользователя
    document.getElementById('edit-user-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = getFormData('edit-user-form');
        await updateUser(formData);
    })

    // Удаление пользователя
    document.getElementById('delete-user-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const userId = document.getElementById('deleteId').value
        await deleteUser(userId);
    })
    document.getElementById('logout-form').addEventListener('submit', async (e) => {
        e.preventDefault()
        await fetch('/logout', {
            method: 'post'
        })
        window.location.href = '/login'
    })
}

async function addUser(userData) {
    await fetch('api/admin/save', {
        method: 'post', body: JSON.stringify(userData), headers: { 'Content-Type': 'application/json' }
    })
    await loadUsers()
}

async function updateUser(userData) {
    console.log('Отправляемая Data:', userData)
    await fetch(`api/admin/update`, {
        method: 'put', body: JSON.stringify(userData), headers: { 'Content-Type': 'application/json' }
    })
    await loadUsers()
    const modal = bootstrap.Modal.getInstance(document.getElementById('editModal'))
    if (modal) modal.hide()
}

async function deleteUser(userId) {
    await fetch(`/api/admin/${userId}`, {
        method: 'delete', headers: { 'Content-Type': 'application/json' }})
    await loadUsers();
    const modal = bootstrap.Modal.getInstance(document.getElementById('deleteModal'))
    if (modal) modal.hide();
}

function getFormData(formId) {
    const form = document.getElementById(formId);
    return {
        id: form.elements.id?.value,
        username: form.elements.username.value,
        age: form.elements.age.value,
        email: form.elements.email.value,
        password: form.elements.password.value,
        roles: Array.from(form.elements.roleIds.selectedOptions).map(option => ({
            id: parseInt(option.value),
            name: option.text,
        }))

    };
}

