// JS for checkout page: address, etc.
// Hiệu ứng toast/thông báo đẹp
function showToast(msg, type = 'success') {
    let toast = document.createElement('div');
    toast.className = 'checkout-toast ' + type;
    toast.innerText = msg;
    document.body.appendChild(toast);
    setTimeout(() => toast.classList.add('show'), 50);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 400);
    }, 2200);
}

function showAddAddressForm() {
    document.getElementById('address-form-modal').style.display = 'flex';
    setTimeout(() => {
        let inp = document.querySelector('#addressForm input[name="name"]');
        if (inp) inp.focus();
    }, 100);
}

function hideAddAddressForm() {
    document.getElementById('address-form-modal').style.display = 'none';
}

function setDefaultAddress(id) {
    showToast('Đặt địa chỉ ' + id + ' làm mặc định!', 'success');
    // Hiệu ứng highlight
    let addr = document.querySelector('.address-item[data-id="' + id + '"]');
    if (addr) {
        addr.classList.add('highlight');
        setTimeout(() => addr.classList.remove('highlight'), 1200);
    }
}

function editAddress(id) {
    // Lấy dữ liệu địa chỉ, show form sửa
    alert('Sửa địa chỉ ' + id);
}

function deleteAddress(id) {
    let modal = document.createElement('div');
    modal.className = 'checkout-modal';
    modal.innerHTML = `<div class='modal-content'>
        <div class='modal-title'>Xác nhận xóa địa chỉ?</div>
        <div class='modal-actions'>
            <button class='btn btn-danger' id='confirmDel'>Xóa</button>
            <button class='btn btn-cancel' id='cancelDel'>Hủy</button>
        </div>
    </div>`;
    document.body.appendChild(modal);
    document.getElementById('confirmDel').onclick = function () {
        showToast('Đã xóa địa chỉ ' + id, 'success');
        modal.remove();
    };
    document.getElementById('cancelDel').onclick = function () {
        modal.remove();
    };
}

// Đóng modal khi lưu địa chỉ thành công
if (document.getElementById('addressForm')) {
    document.getElementById('addressForm').onsubmit = function (e) {
        e.preventDefault();
        showToast('Lưu địa chỉ thành công!', 'success');
        hideAddAddressForm();
    };
}

// Scroll đến phần lỗi nếu có
function scrollToError() {
    let err = document.querySelector('.input-error');
    if (err) err.scrollIntoView({behavior: 'smooth', block: 'center'});
}
