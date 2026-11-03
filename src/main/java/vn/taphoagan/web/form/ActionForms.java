package vn.taphoagan.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public final class ActionForms {

    private ActionForms() {
    }

    @Getter
    @Setter
    public static class RejectForm {
        @NotBlank
        private String reason;
    }

    @Getter
    @Setter
    public static class ShortageForm {
        @NotNull
        private Long itemId;
        @Min(0)
        private int quantity;
    }

    @Getter
    @Setter
    public static class QuickCreateForm {
        @NotBlank
        private String barcode;
        @NotBlank
        private String name;
        @Min(1)
        private long price;
        private String unit = "cái";
        @Min(0)
        private int stock;
    }

    @Getter
    @Setter
    public static class LocationForm {
        private Double lat;
        private Double lng;
        private Long wardId;
    }
}
