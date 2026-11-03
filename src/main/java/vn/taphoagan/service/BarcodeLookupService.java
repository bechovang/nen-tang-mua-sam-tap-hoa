package vn.taphoagan.service;

import tools.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import vn.taphoagan.domain.BarcodeCache;
import vn.taphoagan.repository.BarcodeCacheRepository;

import java.time.Duration;
import java.time.Instant;

@Service
public class BarcodeLookupService {

    private static final Logger log = LoggerFactory.getLogger(BarcodeLookupService.class);
    private static final Duration CACHE_TTL = Duration.ofDays(30);

    private final BarcodeCacheRepository cache;
    private final RestClient client;
    private final String lookupUrl;

    public BarcodeLookupService(BarcodeCacheRepository cache,
                                RestClient barcodeRestClient,
                                @Value("${app.barcode-lookup-url}") String lookupUrl) {
        this.cache = cache;
        this.client = barcodeRestClient;
        this.lookupUrl = lookupUrl;
    }

    public record BarcodeResult(String barcode, String name, String imageUrl, String source) {
        public boolean found() {
            return name != null && !name.isBlank();
        }
    }

    public String normalize(String barcode) {
        return barcode == null ? "" : barcode.trim().replaceAll("\\s+", "").toUpperCase();
    }

    public BarcodeResult lookup(String rawBarcode) {
        String barcode = normalize(rawBarcode);
        if (barcode.isBlank()) {
            return new BarcodeResult(barcode, null, null, "NONE");
        }
        BarcodeCache cached = cache.findById(barcode).orElse(null);
        if (cached != null && cached.getFetchedAt().isAfter(Instant.now().minus(CACHE_TTL))) {
            return new BarcodeResult(barcode, cached.getName(), cached.getImageUrl(), cached.getSource());
        }
        try {
            JsonNode body = client.get()
                    .uri(lookupUrl + "/{barcode}.json", barcode)
                    .retrieve()
                    .body(JsonNode.class);
            if (body != null && body.path("status").asInt(0) == 1) {
                JsonNode product = body.path("product");
                String name = firstNonBlank(product.path("product_name_vi").asText(null),
                        product.path("product_name").asText(null));
                String image = product.path("image_url").asText(null);
                if (name != null && !name.isBlank()) {
                    BarcodeCache entry = new BarcodeCache();
                    entry.setBarcode(barcode);
                    entry.setName(name);
                    entry.setImageUrl(image);
                    entry.setSource("OpenFoodFacts");
                    entry.setFetchedAt(Instant.now());
                    cache.save(entry);
                    return new BarcodeResult(barcode, name, image, "OpenFoodFacts");
                }
            }
        } catch (Exception ex) {
            log.info("Barcode lookup failed for {}: {}", barcode, ex.getMessage());
        }
        return new BarcodeResult(barcode, null, null, "MANUAL");
    }

    private String firstNonBlank(String first, String second) {
        return first != null && !first.isBlank() ? first : second;
    }
}
