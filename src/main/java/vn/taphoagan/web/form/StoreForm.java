package vn.taphoagan.web.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StoreForm {

    @NotBlank
    @Size(min = 2, max = 80)
    private String name;

    @NotBlank
    private String address;

    private Long wardId;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

    @NotBlank
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotNull
    @DecimalMin("0.5")
    @DecimalMax("5.0")
    private BigDecimal radiusKm = BigDecimal.ONE;

    private boolean deliveryEnabled = true;

    private boolean pickupEnabled = true;

    @Min(0)
    private long deliveryFee;

    @Min(0)
    @Max(5_000_000)
    private long minOrder;

    @Min(0)
    @Max(1439)
    private int openMinute = 420;

    @Min(0)
    @Max(1439)
    private int closeMinute = 1320;
}
