package vn.taphoagan.service;

import org.springframework.stereotype.Service;
import vn.taphoagan.domain.Product;
import vn.taphoagan.domain.Store;
import vn.taphoagan.repository.ProductRepository;
import vn.taphoagan.repository.StoreRepository;
import vn.taphoagan.web.dto.DiscoveryViews;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<DiscoveryViews.StoreCard> featuredStores(Double lat, Double lng, int limit) {
        Map<Long, Long> counts = sellableCounts();
        return stores.findAll().stream()
                .map(store -> card(store, lat, lng, counts))
                .filter(card -> card.itemCount() > 0)
                .sorted(Comparator
                        .comparing((DiscoveryViews.StoreCard card) -> !storeService.isOpenNow(card.store()))
                        .thenComparing(DiscoveryViews.StoreCard::itemCount, Comparator.reverseOrder())
                        .thenComparing(card -> distance(card)))
                .limit(limit)
                .toList();
    }

    public List<DiscoveryViews.FeaturedProduct> featuredProducts(int limit) {
        List<Product> sellable = products.findSellable().stream()
                .sorted(Comparator
                        .comparing((Product product) -> product.getImageKey() == null)
                        .thenComparing(Product::getUpdatedAt, Comparator.reverseOrder()))
                .toList();
        Map<Long, Store> byId = storeMap(sellable);
        return sellable.stream()
                .filter(product -> byId.containsKey(product.getStoreId()))
                .limit(limit)
                .map(product -> new DiscoveryViews.FeaturedProduct(product, byId.get(product.getStoreId())))
                .toList();
    }

    public List<DiscoveryViews.StoreCard> browse(String q, Long wardId, Double lat, Double lng,
                                                 double radiusKm, boolean openNow,
                                                 boolean delivery, boolean pickup, String sort) {
        Map<Long, Long> counts = sellableCounts();
        Set<Long> matchingStores = new HashSet<>();
        String query = q == null ? "" : q.trim();
        if (!query.isBlank()) {
            products.searchAll(query).forEach(product -> matchingStores.add(product.getStoreId()));
        }

        List<DiscoveryViews.StoreCard> cards = stores.findAll().stream()
                .filter(store -> wardId == null || wardId.equals(store.getWardId()))
                .filter(store -> !openNow || storeService.isOpenNow(store))
                .filter(store -> !delivery || store.isDeliveryEnabled())
                .filter(store -> !pickup || store.isPickupEnabled())
                .filter(store -> query.isBlank()
                        || store.getName().toLowerCase().contains(query.toLowerCase())
                        || matchingStores.contains(store.getId()))
                .map(store -> card(store, lat, lng, counts))
                .filter(card -> card.itemCount() > 0)
                .filter(card -> lat == null || lng == null || card.distanceKm() <= radiusKm)
                .collect(Collectors.toCollection(ArrayList::new));

        Comparator<DiscoveryViews.StoreCard> comparator = switch (sort == null ? "" : sort) {
            case "items" -> Comparator.comparing(DiscoveryViews.StoreCard::itemCount).reversed()
                    .thenComparing(card -> distance(card));
            case "name" -> Comparator.comparing(card -> card.store().getName(), String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparing(this::distance);
        };
        return cards.stream().sorted(comparator).limit(100).toList();
    }

    private DiscoveryViews.StoreCard card(Store store, Double lat, Double lng, Map<Long, Long> counts) {
        Double distance = lat == null || lng == null
                ? null : geo.distanceKm(lat, lng, store.getLat(), store.getLng());
        return new DiscoveryViews.StoreCard(store, distance, counts.getOrDefault(store.getId(), 0L));
    }

    private Map<Long, Long> sellableCounts() {
        Map<Long, Long> counts = new HashMap<>();
        for (Product product : products.findSellable()) {
            counts.merge(product.getStoreId(), 1L, Long::sum);
        }
        return counts;
    }

    private Map<Long, Store> storeMap(List<Product> list) {
        Set<Long> ids = list.stream().map(Product::getStoreId).collect(Collectors.toSet());
        return stores.findAllById(ids).stream().collect(Collectors.toMap(Store::getId, store -> store));
    }

    private double distance(DiscoveryViews.StoreCard card) {
        return card.distanceKm() == null ? Double.MAX_VALUE : card.distanceKm();
    }

    public DiscoveryViews.StorePage storePage(String slug, Double lat, Double lng) {
        Store store = storeService.requireBySlug(slug);
        double distance = lat != null && lng != null
                ? geo.distanceKm(lat, lng, store.getLat(), store.getLng()) : 0;
        List<Product> list = products.findByStoreIdAndDeletedAtIsNullOrderByUpdatedAtDesc(store.getId());
        return new DiscoveryViews.StorePage(store, distance, list);
    }
}
