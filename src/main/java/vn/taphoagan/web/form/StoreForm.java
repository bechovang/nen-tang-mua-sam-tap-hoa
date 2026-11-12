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
    private Double lat = 10.776;

    @NotNull
    private Double lng = 106.700;

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

    @Size(max = 16)
    @Pattern(regexp = "^$|^[A-Za-z0-9]{2,16}$", message = "Mã ngân hàng không hợp lệ")
    private String bankCode;

    @Size(max = 19)
    @Pattern(regexp = "^$|^[A-Za-z0-9]{4,19}$", message = "Số tài khoản chỉ gồm chữ và số, tối đa 19 ký tự")
    private String bankAccountNumber;

    @Size(max = 80)
    @Pattern(regexp = "^$|^[A-Za-z0-9 .'-]{2,80}$", message = "Tên chủ tài khoản không dấu, tối đa 80 ký tự")
    private String bankAccountHolder;

    private boolean bankTransferEnabled = false;

    @Min(0)
    @Max(1439)
    private int openMinute = 420;

    @Min(0)
    @Max(1439)
    private int closeMinute = 1320;
}
