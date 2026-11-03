package vn.taphoagan.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import vn.taphoagan.domain.Enums.Fulfillment;

@Getter
@Setter
public class CheckoutForm {

    @NotNull
    private Fulfillment fulfillment = Fulfillment.DELIVERY;

    private String address;

    @NotBlank
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private String note;

    private String pickupSlot;

    @NotBlank
    private String idempotencyKey;

    private Double lat;

    private Double lng;
}
