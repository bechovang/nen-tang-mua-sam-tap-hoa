package vn.taphoagan.service;

import org.springframework.stereotype.Service;
import vn.taphoagan.domain.Product;
import vn.taphoagan.domain.Store;
import vn.taphoagan.repository.ProductRepository;
import vn.taphoagan.repository.StoreRepository;
import vn.taphoagan.web.dto.DiscoveryViews;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscoveryService {

    private final StoreRepository stores;
    private final ProductRepository products;
    private final StoreService storeService;
    private final GeoService geo;

    public DiscoveryService(StoreRepository stores, ProductRepository products,
                            StoreService storeService, GeoService geo) {
        this.stores = stores;
        this.products = products;
        this.storeService = storeService;
        this.geo = geo;
    }

    public List<DiscoveryViews.NearbyStore> nearby(double lat, double lng) {
        double radius = 5.0;
        double latDelta = radius / 111.0;
        double lngDelta = radius / (111.0 * Math.max(0.2, Math.cos(Math.toRadians(lat))));
        List<DiscoveryViews.NearbyStore> result = new ArrayList<>();
        for (Store store : stores.findNearby(lat, lng, latDelta, lngDelta, radius)) {
            if (!storeService.isOpenNow(store)) {
                continue;
            }
            long count = products.countByStoreIdAndActiveTrueAndDeletedAtIsNull(store.getId());
            if (count == 0) {
                continue;
            }
            result.add(new DiscoveryViews.NearbyStore(store, geo.distanceKm(lat, lng, store.getLat(), store.getLng()), count));
        }
        result.sort(Comparator.comparingDouble(DiscoveryViews.NearbyStore::distanceKm));
        return result;
    }

    public List<DiscoveryViews.StoreSearch> search(String q, double lat, double lng) {
        if (q == null || q.isBlank()) {
            return List.of();
        }
        Map<Long, List<Product>> byStore = new LinkedHashMap<>();
        for (Product product : products.searchAll(q.trim())) {
            byStore.computeIfAbsent(product.getStoreId(), ignored -> new ArrayList<>()).add(product);
        }
        List<DiscoveryViews.StoreSearch> result = new ArrayList<>();
        for (Map.Entry<Long, List<Product>> entry : byStore.entrySet()) {
            Store store = stores.findById(entry.getKey()).orElse(null);
            if (store == null || !storeService.isOpenNow(store)) {
                continue;
            }
            double distance = geo.distanceKm(lat, lng, store.getLat(), store.getLng());
            if (distance <= 5.0) {
                result.add(new DiscoveryViews.StoreSearch(store, distance, entry.getValue()));
            }
        }
        for (Store store : stores.findByNameContainingIgnoreCase(q.trim())) {
            if (!storeService.isOpenNow(store)) {
                continue;
            }
            boolean already = result.stream().anyMatch(r -> r.store().getId().equals(store.getId()));
            if (already) {
                continue;
            }
            double distance = geo.distanceKm(lat, lng, store.getLat(), store.getLng());
            if (distance <= 5.0) {
                result.add(new DiscoveryViews.StoreSearch(store, distance, List.of()));
            }
        }
        result.sort(Comparator.comparingDouble(DiscoveryViews.StoreSearch::distanceKm));
        return result;
    }

    public DiscoveryViews.StorePage storePage(String slug, Double lat, Double lng) {
        Store store = storeService.requireBySlug(slug);
        double distance = lat != null && lng != null
                ? geo.distanceKm(lat, lng, store.getLat(), store.getLng()) : 0;
        List<Product> list = products.findByStoreIdAndDeletedAtIsNullOrderByUpdatedAtDesc(store.getId());
        return new DiscoveryViews.StorePage(store, distance, list);
    }
}
