package vn.taphoagan.service;

import org.junit.jupiter.api.Test;
import vn.taphoagan.domain.Enums.PaymentMethod;
import vn.taphoagan.domain.Order;

import static org.assertj.core.api.Assertions.assertThat;

class VietQrServiceTest {

    private final VietQrService service = new VietQrService();

    @Test
    void buildsEncodedImageUrlFromOrderSnapshot() {
        Order order = new Order();
        order.setPaymentMethod(PaymentMethod.VIETQR);
        order.setTotal(65000);
        order.setPaymentContent("TH2611061234");
        order.setPaymentBankCode("VCB");
        order.setPaymentAccountNumber("0123456789");
        order.setPaymentAccountHolder("NGUYEN VAN A");

        String url = service.buildUrl(order);

        assertThat(url).startsWith("https://vietqr.app/img?");
        assertThat(url).contains("acc=0123456789");
        assertThat(url).contains("bank=VCB");
        assertThat(url).contains("amount=65000");
        assertThat(url).contains("des=TH2611061234");
        assertThat(url).contains("template=compact");
        assertThat(url).contains("showinfo=true");
        assertThat(url).contains("fullacc=true");
        assertThat(url).contains("holder=NGUYEN%20VAN%20A");
    }

    @Test
    void doesNotBuildUrlWithoutCompleteBankSnapshot() {
        Order order = new Order();
        order.setPaymentMethod(PaymentMethod.VIETQR);
        order.setTotal(65000);
        order.setPaymentContent("TH2611061234");
        order.setPaymentBankCode("VCB");
        order.setPaymentAccountNumber("0123456789");

        assertThat(service.buildUrl(order)).isNull();
    }

    @Test
    void loadsSupportedBankDirectory() {
        BankDirectoryService directory = new BankDirectoryService();

        assertThat(directory.all()).hasSizeGreaterThan(50);
        assertThat(directory.require("VCB").name()).isEqualTo("Vietcombank");
        assertThat(directory.require("vcb").code()).isEqualTo("VCB");
    }

    @Test
    void doesNotBuildUrlForCashOrder() {
        Order order = new Order();
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setTotal(65000);

        assertThat(service.buildUrl(order)).isNull();
    }
}
