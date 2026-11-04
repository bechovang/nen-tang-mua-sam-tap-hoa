package vn.taphoagan.web;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.taphoagan.domain.Order;
import vn.taphoagan.repository.OrderRepository;
import vn.taphoagan.service.PaymentService;

@Controller
public class PayOsController {

    private static final Logger log = LoggerFactory.getLogger(PayOsController.class);

    private final PaymentService payments;
    private final OrderRepository orders;

    public PayOsController(PaymentService payments, OrderRepository orders) {
        this.payments = payments;
        this.orders = orders;
    }

    @GetMapping("/payments/payos/return")
    public String returned(@RequestParam(required = false) Long orderCode,
                           Authentication authentication, RedirectAttributes redirect) {
        payments.syncStatus(orderCode);
        Order order = orderCode == null ? null : orders.findById(orderCode).orElse(null);
        if (order != null && authentication != null && authentication.isAuthenticated()) {
            redirect.addFlashAttribute("success", "Đã quay lại từ PayOS. Hệ thống sẽ cập nhật khi nhận xác nhận thanh toán.");
            return "redirect:/orders/" + order.getCode();
        }
        return "redirect:/orders";
    }

    @GetMapping("/payments/payos/cancel")
    public String cancelled(@RequestParam(required = false) Long orderCode,
                            Authentication authentication, RedirectAttributes redirect) {
        payments.syncStatus(orderCode);
        Order order = orderCode == null ? null : orders.findById(orderCode).orElse(null);
        if (order != null && authentication != null && authentication.isAuthenticated()) {
            redirect.addFlashAttribute("error", "Bạn đã hủy thanh toán PayOS. Có thể thử lại từ chi tiết đơn.");
            return "redirect:/orders/" + order.getCode();
        }
        return "redirect:/orders";
    }
}
