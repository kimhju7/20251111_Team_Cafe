let menuModal;

document.addEventListener('DOMContentLoaded', function() {
    menuModal = new bootstrap.Modal(document.getElementById('menuModal'));
    loadMenus();
});

async function loadMenus() {
    try {
        const response = await fetch('/api/menus');
        const menus = await response.json();

        const tbody = document.getElementById('menuTableBody');
        tbody.innerHTML = '';

        menus.forEach(menu => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${menu.id}</td>
                <td>${menu.name}</td>
                <td>${menu.description || ''}</td>
                <td>${menu.price != null ? menu.price.toFixed(2) : ''}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editMenu(${menu.id})">수정</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteMenu(${menu.id})">삭제</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('메뉴 목록을 불러오는데 실패했습니다:', error);
        alert('메뉴 목록을 불러오는데 실패했습니다.');
    }
}

function showAddMenuModal() {
    document.getElementById('modalTitle').textContent = '메뉴 추가';
    document.getElementById('menuForm').reset();
    document.getElementById('menuId').value = '';
    menuModal.show();
}

async function editMenu(id) {
    try {
        const response = await fetch(`/api/menus/${id}`);
        const menu = await response.json();

        document.getElementById('modalTitle').textContent = '메뉴 수정';
        document.getElementById('menuId').value = menu.id;
        document.getElementById('menuName').value = menu.name;
        document.getElementById('menuDescription').value = menu.description || '';
        document.getElementById('menuPrice').value = menu.price != null ? menu.price : '';

        menuModal.show();
    } catch (error) {
        console.error('메뉴 정보를 불러오는데 실패했습니다:', error);
        alert('메뉴 정보를 불러오는데 실패했습니다.');
    }
}

async function saveMenu() {
    const id = document.getElementById('menuId').value;
    const menu = {
        name: document.getElementById('menuName').value,
        description: document.getElementById('menuDescription').value,
        price: parseFloat(document.getElementById('menuPrice').value)
    };

    try {
        const url = id ? `/api/menus/${id}` : '/api/menus';
        const method = id ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(menu)
        });

        if (!response.ok) {
            throw new Error('저장에 실패했습니다.');
        }

        menuModal.hide();
        loadMenus();
        alert('저장되었습니다.');
    } catch (error) {
        console.error('저장에 실패했습니다:', error);
        alert('저장에 실패했습니다.');
    }
}

async function deleteMenu(id) {
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch(`/api/menus/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('삭제에 실패했습니다.');
        }

        loadMenus();
        alert('삭제되었습니다.');
    } catch (error) {
        console.error('삭제에 실패했습니다:', error);
        alert('삭제에 실패했습니다.');
    }
}
