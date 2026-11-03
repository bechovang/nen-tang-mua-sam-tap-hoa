package vn.taphoagan.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import vn.taphoagan.domain.Product;
import vn.taphoagan.repository.StoreRepository;
import vn.taphoagan.web.BusinessException;

@Service
public class CartService {

    public static final String SESSION_KEY = "cart";

    private final StoreRepository stores;

    public CartService(StoreRepository stores) {
        this.stores = stores;
    }

    public Cart get(HttpSession session) {
        Object value = session.getAttribute(SESSION_KEY);
        if (value instanceof Cart cart) {
            return cart;
        }
        Cart cart = new Cart();
        session.setAttribute(SESSION_KEY, cart);
        return cart;
    }

    public Cart add(HttpSession session, Product product, int quantity) {
        if (!product.isActive() || product.getStock() <= 0) {
            throw new BusinessException("PRODUCT_UNAVAILABLE", "Món này đã hết hàng.");
        }
        Cart cart = get(session);
        if (cart.getStoreId() != null && !cart.getStoreId().equals(product.getStoreId())) {
            String current = cart.getStoreName() == null ? "tiệm khác" : cart.getStoreName();
            throw new BusinessException("CART_OTHER_STORE",
                    "Giỏ đang có món từ " + current + ". Xóa giỏ trước khi thêm món từ tiệm khác.");
        }
        if (cart.getStoreId() == null) {
            cart.setStoreId(product.getStoreId());
            cart.setStoreName(stores.findById(product.getStoreId())
                    .map(s -> s.getName()).orElse("Cửa hàng"));
            cart.setStoreSlug(stores.findById(product.getStoreId())
                    .map(s -> s.getSlug()).orElse(null));
        }
        Cart.Item item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(product.getId()))
                .findFirst().orElse(null);
        if (item == null) {
            item = new Cart.Item();
            item.setProductId(product.getId());
            item.setName(product.getName());
            item.setUnit(product.getUnit());
            item.setPrice(product.getPrice());
            item.setQuantity(0);
            cart.getItems().add(item);
        }
        item.setQuantity(Math.max(1, item.getQuantity() + quantity));
        return cart;
    }

    public Cart update(HttpSession session, Long productId, int quantity) {
        Cart cart = get(session);
        cart.getItems().removeIf(item -> {
            if (item.getProductId().equals(productId) && quantity <= 0) {
                return true;
            }
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
            }
            return false;
        });
        if (cart.getItems().isEmpty()) {
            cart.clear();
        }
        return cart;
    }

    public Cart remove(HttpSession session, Long productId) {
        return update(session, productId, 0);
    }

    public void clear(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }

    public int count(HttpSession session) {
        return get(session).count();
    }
}
