document.addEventListener('DOMContentLoaded', () => {
    loadUserData()
    setupEventListeners()
})

async function loadUserData() {
    const response = await  fetch('/api/user')
    let user = await response.json()
    displayUserData(user)
    checkAdminRole(user)
}

function displayUserData(user) {
    const userDataHTML = `
                    <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(r => r.name).join(', ')}</td>
                </tr>
            `
    document.getElementById('user-data').innerHTML = userDataHTML
    document.getElementById('current-user').textContent =
        `${user.username} with roles: (${user.roles.map(r => r.name).join(', ')})`
}

function setupEventListeners() {
    document.getElementById('logout-form').addEventListener('submit', async (e) => {
        e.preventDefault()
        await fetch('/logout', {
            method: 'post'
        })
        window.location.href = '/login'
    })
}

function getCsrfToken() {
    return document.cookie
        .split('; ')
        .find(row => row.startsWith('XSRF-TOKEN='))
        ?.split('=')[1];
}

function checkAdminRole(user) {
    const isAdmin = user.roles.some(r => r.name === 'ROLE_ADMIN');
    document.getElementById('admin-link').style.display = isAdmin ? 'block' : 'none';
}
