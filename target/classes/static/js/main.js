/**
 * APP.JS - JavaScript chuẩn thực tế công ty
 * Stack: jQuery + Bootstrap + AJAX calls đơn giản
 * Pattern: Đúng cách mà 90% công ty VN đang dùng
 */

$(document).ready(function () {

    // ===== CHUẨN THỰC TẾ: Sử dụng jQuery =====

    // 1. AJAX Add to Cart (Chuẩn công ty)
    $('.add-to-cart').click(function (e) {
        e.preventDefault();

        const productId = $(this).data('product-id');
        const quantity = $('#quantity-' + productId).val() || 1;

        // Show loading (Bootstrap spinner)
        $(this).html('<span class="spinner-border spinner-border-sm"></span> Đang thêm...');

        $.ajax({
            url: '/api/cart/add',
            method: 'POST',
            data: {
                productId: productId,
                quantity: quantity
            },
            success: function (response) {
                if (response.success) {
                    // Update cart badge
                    $('#cart-count').text(response.cartCount);

                    // Show success toast (Bootstrap)
                    showToast('Đã thêm vào giỏ hàng!', 'success');

                    // Reset button
                    $('.add-to-cart[data-product-id="' + productId + '"]')
                        .html('<i class="fas fa-cart-plus"></i> Thêm vào giỏ');
                } else {
                    showToast(response.message, 'error');
                }
            },
            error: function () {
                showToast('Có lỗi xảy ra, vui lòng thử lại!', 'error');
            }
        });
    });

    // 2. Remove from Cart
    $('.remove-item').click(function (e) {
        e.preventDefault();

        if (!confirm('Bạn có chắc muốn xóa sản phẩm này?')) return;

        const cartItemId = $(this).data('item-id');
        const $row = $(this).closest('tr');

        $.ajax({
            url: '/api/cart/remove/' + cartItemId,
            method: 'DELETE',
            success: function (response) {
                if (response.success) {
                    $row.fadeOut(300, function () {
                        $(this).remove();
                    });
                    $('#cart-total').text(response.newTotal);
                    $('#cart-count').text(response.cartCount);
                    showToast('Đã xóa sản phẩm!', 'success');
                }
            }
        });
    });

    // 3. Update Quantity
    $('.quantity-input').change(function () {
        const cartItemId = $(this).data('item-id');
        const newQuantity = $(this).val();

        $.ajax({
            url: '/api/cart/update',
            method: 'PUT',
            data: {
                cartItemId: cartItemId,
                quantity: newQuantity
            },
            success: function (response) {
                if (response.success) {
                    $('#item-total-' + cartItemId).text(response.itemTotal);
                    $('#cart-total').text(response.cartTotal);
                }
            }
        });
    });

    // 4. Form Validation (Chuẩn Bootstrap)
    $('form.needs-validation').submit(function (e) {
        if (!this.checkValidity()) {
            e.preventDefault();
            e.stopPropagation();
        }
        $(this).addClass('was-validated');
    });

    // 5. Search Products (Debounced)
    let searchTimeout;
    $('#search-input').keyup(function () {
        clearTimeout(searchTimeout);
        const keyword = $(this).val();

        searchTimeout = setTimeout(function () {
            if (keyword.length >= 2) {
                searchProducts(keyword);
            }
        }, 500);
    });

    // 6. Category Filter
    $('.category-filter').click(function (e) {
        e.preventDefault();
        const categoryId = $(this).data('category-id');

        $.ajax({
            url: '/api/products/category/' + categoryId,
            method: 'GET',
            success: function (products) {
                renderProducts(products);
            }
        });
    });
});

// ===== UTILITY FUNCTIONS (Chuẩn thực tế) =====

// Toast notification (Bootstrap Toast)
function showToast(message, type) {
    const toastHtml = `
        <div class="toast align-items-center text-white bg-${type === 'success' ? 'success' : 'danger'}" role="alert">
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;

    $('#toast-container').append(toastHtml);
    $('.toast').last().toast('show');
}

// Search Products
function searchProducts(keyword) {
    $.ajax({
        url: '/api/products/search',
        method: 'GET',
        data: {q: keyword},
        success: function (products) {
            renderProducts(products);
        }
    });
}

// Render Products (Template rendering)
function renderProducts(products) {
    let html = '';
    products.forEach(function (product) {
        html += `
            <div class="col-md-4 mb-3">
                <div class="card">
                    <img src="${product.image}" class="card-img-top" alt="${product.name}">
                    <div class="card-body">
                        <h5 class="card-title">${product.name}</h5>
                        <p class="card-text">${product.description}</p>
                        <p class="text-danger fw-bold">${formatPrice(product.price)}</p>
                        <button class="btn btn-primary add-to-cart" data-product-id="${product.id}">
                            <i class="fas fa-cart-plus"></i> Thêm vào giỏ
                        </button>
                    </div>
                </div>
            </div>
        `;
    });
    $('#products-container').html(html);
}

// Format Price (VND)
function formatPrice(price) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(price);
}

// ===== ADMIN FUNCTIONS =====

// Admin: Delete with confirmation
function deleteItem(url, itemName) {
    if (confirm(`Bạn có chắc muốn xóa "${itemName}"?`)) {
        $.ajax({
            url: url,
            method: 'DELETE',
            success: function (response) {
                if (response.success) {
                    location.reload(); // Reload trang sau khi xóa
                    showToast('Đã xóa thành công!', 'success');
                }
            }
        });
    }
}

// Admin: Toggle status
function toggleStatus(url, currentStatus) {
    $.ajax({
        url: url,
        method: 'PUT',
        data: {status: !currentStatus},
        success: function (response) {
            if (response.success) {
                location.reload();
            }
        }
    });
}
