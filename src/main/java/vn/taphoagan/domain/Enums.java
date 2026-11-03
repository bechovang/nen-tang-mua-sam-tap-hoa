package vn.taphoagan.domain;

import java.util.EnumSet;
import java.util.Set;

public final class Enums {

    private Enums() {
    }

    public enum Role {
        CUSTOMER, STORE_OWNER
    }

    public enum StoreStatus {
        OPEN, CLOSED
    }

    public enum OrderType {
        ONLINE, POS
    }

    public enum Fulfillment {
        DELIVERY, PICKUP
    }

    public enum PosSessionStatus {
        OPEN, CLOSED
    }

    public enum StockReason {
        RESERVE, RELEASE, POS_SALE, SHORTAGE
    }

    public enum NotificationType {
        ORDER_NEW, ORDER_CONFIRMED, ORDER_CANCELLED, ORDER_COMPLETED,
        ORDER_ADJUSTED, PICKUP_REMINDER
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, PREPARING, READY, OUT_FOR_DELIVERY, COMPLETED, CANCELLED, NO_SHOW;

        public boolean canTransitionTo(OrderStatus target) {
            return switch (this) {
                case PENDING -> EnumSet.of(CONFIRMED, CANCELLED).contains(target);
                case CONFIRMED -> EnumSet.of(PREPARING, CANCELLED).contains(target);
                case PREPARING -> EnumSet.of(READY, OUT_FOR_DELIVERY, CANCELLED).contains(target);
                case READY -> EnumSet.of(COMPLETED, NO_SHOW).contains(target);
                case OUT_FOR_DELIVERY -> EnumSet.of(COMPLETED, CANCELLED).contains(target);
                case COMPLETED, CANCELLED, NO_SHOW -> false;
            };
        }

        public Set<OrderStatus> allowedTargets() {
            return switch (this) {
                case PENDING -> EnumSet.of(CONFIRMED, CANCELLED);
                case CONFIRMED -> EnumSet.of(PREPARING, CANCELLED);
                case PREPARING -> EnumSet.of(READY, OUT_FOR_DELIVERY, CANCELLED);
                case READY -> EnumSet.of(COMPLETED, NO_SHOW);
                case OUT_FOR_DELIVERY -> EnumSet.of(COMPLETED, CANCELLED);
                case COMPLETED, CANCELLED, NO_SHOW -> EnumSet.noneOf(OrderStatus.class);
            };
        }

        public String label() {
            return switch (this) {
                case PENDING -> "Chờ tiệm xác nhận";
                case CONFIRMED -> "Tiệm đã xác nhận";
                case PREPARING -> "Đang soạn";
                case READY -> "Sẵn sàng nhận";
                case OUT_FOR_DELIVERY -> "Đang giao";
                case COMPLETED -> "Hoàn tất";
                case CANCELLED -> "Đã hủy";
                case NO_SHOW -> "Không nhận";
            };
        }
    }
}
