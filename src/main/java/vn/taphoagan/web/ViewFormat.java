package vn.taphoagan.web;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class ViewFormat {

    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("HH:mm dd/MM").withZone(ZONE);
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZONE);

    public String money(long value) {
        return NumberFormat.getNumberInstance(Locale.of("vi", "VN")).format(value) + " ₫";
    }

    public String dateTime(Instant instant) {
        return instant == null ? "" : DATE_TIME.format(instant);
    }

    public String date(Instant instant) {
        return instant == null ? "" : DATE.format(instant);
    }

    public String time(Instant instant) {
        return instant == null ? "" : DateTimeFormatter.ofPattern("HH:mm").withZone(ZONE).format(instant);
    }
}
