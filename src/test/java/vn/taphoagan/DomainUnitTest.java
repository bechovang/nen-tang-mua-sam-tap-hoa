package vn.taphoagan;

import org.junit.jupiter.api.Test;
import vn.taphoagan.domain.Enums.OrderStatus;
import vn.taphoagan.service.GeoService;

import static org.assertj.core.api.Assertions.assertThat;

class DomainUnitTest {

    @Test
    void orderStatusOnlyAllowsLegalTransitions() {
        assertThat(OrderStatus.PENDING.canTransitionTo(OrderStatus.CONFIRMED)).isTrue();
        assertThat(OrderStatus.PENDING.canTransitionTo(OrderStatus.COMPLETED)).isFalse();
        assertThat(OrderStatus.CONFIRMED.canTransitionTo(OrderStatus.PREPARING)).isTrue();
        assertThat(OrderStatus.PREPARING.canTransitionTo(OrderStatus.READY)).isTrue();
        assertThat(OrderStatus.READY.canTransitionTo(OrderStatus.NO_SHOW)).isTrue();
        assertThat(OrderStatus.COMPLETED.canTransitionTo(OrderStatus.CANCELLED)).isFalse();
    }

    @Test
    void distanceIsZeroForSamePointAndAboutOneKmForPointZeroOneDegree() {
        GeoService geo = new GeoService();
        assertThat(geo.distanceKm(10.0, 106.0, 10.0, 106.0)).isZero();
        assertThat(geo.distanceKm(10.0, 106.0, 10.01, 106.0)).isBetween(1.0, 1.2);
    }
}
