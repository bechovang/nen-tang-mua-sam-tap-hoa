package vn.taphoagan.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.taphoagan.service.PaymentService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments/webhooks/payos")
public class PayOsWebhookController {

    private static final Logger log = LoggerFactory.getLogger(PayOsWebhookController.class);

    private final PaymentService payments;

    public PayOsWebhookController(PaymentService payments) {
        this.payments = payments;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> webhook(@RequestBody Map<String, Object> body) {
        try {
            payments.handleWebhook(body);
            return ResponseEntity.ok(Map.of("code", "00", "message", "success"));
        } catch (BusinessException ex) {
            log.warn("PayOS webhook rejected: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("code", "01", "message", ex.getMessage()));
        } catch (Exception ex) {
            log.warn("PayOS webhook failed", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("code", "01", "message", "invalid webhook"));
        }
    }
}
