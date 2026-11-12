package vn.taphoagan.service;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import vn.taphoagan.domain.Order;
import vn.taphoagan.domain.Enums.PaymentMethod;

@Service
public class VietQrService {

    private static final String IMAGE_ENDPOINT = "https://vietqr.app/img";

    public String buildUrl(Order order) {
        if (!isConfigured(order)) {
            return null;
        }
        return UriComponentsBuilder.fromUriString(IMAGE_ENDPOINT)
                .queryParam("acc", order.getPaymentAccountNumber())
                .queryParam("bank", order.getPaymentBankCode())
                .queryParam("amount", order.getTotal())
                .queryParam("des", order.getPaymentContent())
                .queryParam("template", "compact")
                .queryParam("showinfo", "true")
                .queryParam("fullacc", "true")
                .queryParam("holder", order.getPaymentAccountHolder())
                .build()
                .encode()
                .toUriString();
    }

    public boolean isConfigured(Order order) {
        return order != null
                && order.getPaymentMethod() == PaymentMethod.VIETQR
                && order.getTotal() > 0
                && hasText(order.getPaymentAccountNumber())
                && hasText(order.getPaymentBankCode())
                && hasText(order.getPaymentContent())
                && hasText(order.getPaymentAccountHolder());
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
