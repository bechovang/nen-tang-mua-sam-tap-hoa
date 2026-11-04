package vn.taphoagan.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.payos.model.v2.paymentRequests.PaymentLinkStatus;
import vn.payos.model.webhooks.WebhookData;
import vn.taphoagan.domain.Order;
import vn.taphoagan.repository.OrderRepository;
import vn.taphoagan.web.BusinessException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static vn.taphoagan.domain.Enums.PaymentMethod;
import static vn.taphoagan.domain.Enums.PaymentStatus;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private static final long PAYMENT_TTL_MINUTES = 15;

    private final ObjectProvider<PayOS> payOsProvider;
    private final OrderRepository orders;
    private final OrderService orderService;
    private final String baseUrl;

    public PaymentService(ObjectProvider<PayOS> payOsProvider,
                          OrderRepository orders,
                          OrderService orderService,
                          @Value("${app.base-url}") String baseUrl) {
        this.payOsProvider = payOsProvider;
        this.orders = orders;
        this.orderService = orderService;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public boolean isEnabled() {
        return payOsProvider.getIfAvailable() != null;
    }

    public String start(Order order) {
        PayOS payOS = requirePayOS();
        Order managed = orders.findById(order.getId())
                .orElseThrow(() -> new BusinessException("ORDER_NOT_FOUND", "Không tìm thấy đơn hàng."));
        if (managed.getPaymentMethod() != PaymentMethod.PAYOS) {
            throw new BusinessException("PAYMENT_METHOD", "Đơn hàng không dùng thanh toán PayOS.");
        }
        if (managed.isPaid()) {
            throw new BusinessException("PAYMENT_ALREADY_PAID", "Đơn hàng đã được thanh toán.");
        }
        if (managed.getPaymentUrl() != null
                && managed.getPaymentExpiresAt() != null
                && managed.getPaymentExpiresAt().isAfter(Instant.now().plusSeconds(60))) {
            return managed.getPaymentUrl();
        }
        if (managed.getTotal() <= 0) {
            throw new BusinessException("PAYMENT_AMOUNT", "Đơn hàng không có số tiền cần thanh toán.");
        }

        Instant expiresAt = Instant.now().plus(PAYMENT_TTL_MINUTES, ChronoUnit.MINUTES);
        CreatePaymentLinkRequest request = CreatePaymentLinkRequest.builder()
                .orderCode(managed.getId())
                .amount(managed.getTotal())
                .description("TH " + managed.getCode())
                .returnUrl(baseUrl + "/payments/payos/return")
                .cancelUrl(baseUrl + "/payments/payos/cancel")
                .expiredAt(expiresAt.getEpochSecond())
                .build();
        try {
            CreatePaymentLinkResponse response = payOS.paymentRequests().create(request);
            managed.setPaymentStatus(PaymentStatus.PENDING);
            managed.setPaymentLinkId(response.getPaymentLinkId());
            managed.setPaymentUrl(response.getCheckoutUrl());
            managed.setPaymentExpiresAt(response.getExpiredAt() == null
                    ? expiresAt : Instant.ofEpochSecond(response.getExpiredAt()));
            orders.save(managed);
            return response.getCheckoutUrl();
        } catch (Exception ex) {
            log.warn("Cannot create PayOS payment link for order {}", managed.getCode(), ex);
            throw new BusinessException("PAYOS_CREATE_FAILED", "Không tạo được link thanh toán. Vui lòng thử lại.");
        }
    }

    public void syncStatus(Long payOsOrderCode) {
        if (payOsOrderCode == null) {
            return;
        }
        Order order = orders.findById(payOsOrderCode).orElse(null);
        if (order == null || order.getPaymentMethod() != PaymentMethod.PAYOS || order.isPaid()) {
            return;
        }
        try {
            PaymentLink link = requirePayOS().paymentRequests().get(payOsOrderCode);
            PaymentLinkStatus status = link.getStatus();
            if (status == PaymentLinkStatus.PAID) {
                orderService.markPaidOnline(order.getId(), order.getTotal(),
                        link.getTransactions() == null || link.getTransactions().isEmpty()
                                ? null : link.getTransactions().getFirst().getReference(),
                        link.getId());
            } else if (status == PaymentLinkStatus.CANCELLED || status == PaymentLinkStatus.EXPIRED) {
                order.setPaymentStatus(PaymentStatus.CANCELLED);
                orders.save(order);
            } else if (status == PaymentLinkStatus.FAILED) {
                order.setPaymentStatus(PaymentStatus.FAILED);
                orders.save(order);
            }
        } catch (Exception ex) {
            log.warn("Cannot sync PayOS payment status for order {}", order.getCode(), ex);
        }
    }

    public WebhookData verifyWebhook(JsonNode body) {
        return requirePayOS().webhooks().verify(body);
    }

    public void handleWebhook(JsonNode body) {
        WebhookData data = verifyWebhook(body);
        boolean success = body.path("success").asBoolean(false)
                || "00".equals(body.path("code").asText())
                || "00".equals(data.getCode());
        if (!success || data.getOrderCode() == null) {
            log.info("Ignoring non-success PayOS webhook: {}", body.path("desc").asText(""));
            return;
        }
        Order order = orders.findById(data.getOrderCode()).orElse(null);
        if (order == null || order.getPaymentMethod() != PaymentMethod.PAYOS) {
            log.warn("PayOS webhook for unknown/non-PayOS order {}", data.getOrderCode());
            return;
        }
        if (order.isPaid()) {
            return;
        }
        if (data.getAmount() == null || data.getAmount() != order.getTotal()) {
            throw new BusinessException("PAYMENT_AMOUNT_MISMATCH",
                    "Số tiền PayOS không khớp đơn hàng " + order.getCode() + ".");
        }
        orderService.markPaidOnline(order.getId(), data.getAmount(), data.getReference(), data.getPaymentLinkId());
    }

    private PayOS requirePayOS() {
        PayOS payOS = payOsProvider.getIfAvailable();
        if (payOS == null) {
            throw new BusinessException("PAYOS_DISABLED", "Thanh toán online chưa được cấu hình.");
        }
        return payOS;
    }
}
