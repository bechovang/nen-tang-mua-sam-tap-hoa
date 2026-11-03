package vn.taphoagan.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.taphoagan.domain.Order;
import vn.taphoagan.domain.Product;
import vn.taphoagan.domain.Store;
import vn.taphoagan.domain.User;
import vn.taphoagan.repository.ProductRepository;
import vn.taphoagan.repository.StoreRepository;
import vn.taphoagan.service.*;
import vn.taphoagan.web.form.CheckoutForm;

import java.util.UUID;

@Controller
public class CartController {

    private final CartService cartService;
    private final ProductRepository products;
    private final StoreRepository stores;
    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public CartController(CartService cartService, ProductRepository products, StoreRepository stores,
                          OrderService orderService, CurrentUserService currentUserService) {
        this.cartService = cartService;
        this.products = products;
        this.stores = stores;
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        Cart cart = cartService.get(session);
        model.addAttribute("cart", cart);
        if (cart.getStoreId() != null) {
            stores.findById(cart.getStoreId()).ifPresent(store -> model.addAttribute("store", store));
        }
        return "cart";
    }

    @PostMapping("/cart/add")
    public String add(@RequestParam Long productId,
                      @RequestParam(defaultValue = "1") int quantity,
                      HttpSession session, HttpServletRequest request,
                      RedirectAttributes redirect) {
        Product product = products.findById(productId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy mặt hàng."));
        try {
            cartService.add(session, product, Math.max(1, quantity));
            redirect.addFlashAttribute("success", "Đã thêm vào giỏ.");
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer == null ? "/cart" : referer);
    }

    @PostMapping("/cart/update")
    public String update(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        cartService.update(session, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam Long productId, HttpSession session) {
        cartService.remove(session, productId);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Authentication authentication, HttpSession session, Model model) {
        User user = currentUserService.require(authentication);
        Cart cart = cartService.get(session);
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        Store store = stores.findById(cart.getStoreId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng."));
        CheckoutForm form = new CheckoutForm();
        form.setIdempotencyKey(UUID.randomUUID().toString());
        form.setLat((Double) session.getAttribute("lat"));
        form.setLng((Double) session.getAttribute("lng"));
        form.setAddress((String) session.getAttribute("wardName"));
        model.addAttribute("cart", cart);
        model.addAttribute("store", store);
        model.addAttribute("checkoutForm", form);
        model.addAttribute("user", user);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@Valid @ModelAttribute("checkoutForm") CheckoutForm form,
                             BindingResult binding, Authentication authentication,
                             HttpSession session, Model model, RedirectAttributes redirect) {
        User user = currentUserService.require(authentication);
        Cart cart = cartService.get(session);
        Store store = stores.findById(cart.getStoreId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng."));
        if (form.getLat() == null) {
            form.setLat((Double) session.getAttribute("lat"));
        }
        if (form.getLng() == null) {
            form.setLng((Double) session.getAttribute("lng"));
        }
        if (binding.hasErrors()) {
            model.addAttribute("cart", cart);
            model.addAttribute("store", store);
            return "checkout";
        }
        try {
            Order order = orderService.placeOrder(user, cart, form);
            cartService.clear(session);
            return "redirect:/orders/" + order.getCode();
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("cart", cart);
            model.addAttribute("store", store);
            return "checkout";
        }
    }
}
