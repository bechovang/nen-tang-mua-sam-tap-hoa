package vn.taphoagan.web.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductForm {

    @NotBlank
    @Size(min = 6, max = 32)
    private String barcode;

    @NotBlank
    @Size(min = 1, max = 120)
    private String name;

    @Min(1)
    private long price;

    @NotBlank
    @Size(min = 1, max = 20)
    private String unit = "cái";

    @Min(0)
    private int stock;

    private String imageKey;

    private MultipartFile image;

    private boolean active = true;
}
