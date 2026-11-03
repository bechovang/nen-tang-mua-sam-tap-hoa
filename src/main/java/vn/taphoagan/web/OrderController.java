package vn.taphoagan.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.taphoagan.domain.Order;
import vn.taphoagan.domain.User;
import vn.taphoagan.service.Cart;
import vn.taphoagan.service.CartService;
import vn.taphoagan.service.CurrentUserService;
import vn.taphoagan.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orders;
    private final CurrentUserService currentUserService;
    private final CartService cartService;

    public OrderController(OrderService orders, CurrentUserService currentUserService, CartService cartService) {
        this.orders = orders;
        this.currentUserService = currentUserService;
        this.cartService = cartService;
    }

    @GetMapping
    public String list(Authentication authentication, Model model) {
        User user = currentUserService.require(authentication);
        model.addAttribute("orders", orders.customerOrders(user));
        return "orders";
    }

    @GetMapping("/{code}")
    public String detail(@PathVariable String code, Authentication authentication, Model model) {
        User user = currentUserService.require(authentication);
        Order order = orders.customerOrder(code, user);
        model.addAttribute("order", order);
        model.addAttribute("items", orders.items(order));
        model.addAttribute("events", orders.events(order));
        return "order-detail";
    }

    @PostMapping("/{code}/cancel")
    public String cancel(@PathVariable String code, Authentication authentication, RedirectAttributes redirect) {
        User user = currentUserService.require(authentication);
        Order order = orders.customerOrder(code, user);
        try {
            orders.cancelByCustomer(order, user);
            redirect.addFlashAttribute("success", "Đã hủy đơn.");
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/orders/" + code;
    }

    @PostMapping("/{code}/reorder")
    public String reorder(@PathVariable String code, Authentication authentication,
                          HttpSession session, RedirectAttributes redirect) {
        User user = currentUserService.require(authentication);
        Order order = orders.customerOrder(code, user);
        try {
            Cart cart = orders.reorder(order);
            session.setAttribute(CartService.SESSION_KEY, cart);
            return "redirect:/cart";
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/orders/" + code;
        }
    }
}
