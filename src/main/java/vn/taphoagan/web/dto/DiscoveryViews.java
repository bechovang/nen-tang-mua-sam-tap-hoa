package vn.taphoagan.web.dto;

import vn.taphoagan.domain.Product;
import vn.taphoagan.domain.Store;

import java.util.List;

public final class DiscoveryViews {

    private DiscoveryViews() {
    }

    public record NearbyStore(Store store, double distanceKm, long itemCount) {
    }

    public record StoreSearch(Store store, double distanceKm, List<Product> products) {
    }

    public record StoreCard(Store store, Double distanceKm, long itemCount) {
    }

    public record FeaturedProduct(Product product, Store store) {
    }

    public record StorePage(Store store, double distanceKm, List<Product> products) {
    }
}
