package vn.taphoagan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.taphoagan.domain.Product;
import vn.taphoagan.repository.ProductRepository;
import vn.taphoagan.web.BusinessException;
import vn.taphoagan.web.NotFoundException;
import vn.taphoagan.web.form.ProductForm;

import java.time.Instant;
import java.util.List;

@Service
public class CatalogService {

    private final ProductRepository products;
    private final BarcodeLookupService barcodeLookup;
    private final ImageStoragePort imageStorage;

    public CatalogService(ProductRepository products, BarcodeLookupService barcodeLookup,
                          ImageStoragePort imageStorage) {
        this.products = products;
        this.barcodeLookup = barcodeLookup;
        this.imageStorage = imageStorage;
    }

    public List<Product> list(Long storeId) {
        return products.findByStoreIdAndDeletedAtIsNullOrderByUpdatedAtDesc(storeId);
    }

    public List<Product> search(Long storeId, String q) {
        if (q == null || q.isBlank()) {
            return list(storeId);
        }
        return products.search(storeId, q.trim());
    }

    public Product require(Long storeId, Long productId) {
        return products.findByIdAndStoreId(productId, storeId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy mặt hàng."));
    }

    public Product findByBarcode(Long storeId, String barcode) {
        return products.findByStoreIdAndBarcode(storeId, barcodeLookup.normalize(barcode)).orElse(null);
    }

    public long count(Long storeId) {
        return products.countByStoreIdAndActiveTrueAndDeletedAtIsNull(storeId);
    }

    @Transactional
    public Product createOrUpdate(Long storeId, ProductForm form) {
        String barcode = barcodeLookup.normalize(form.getBarcode());
        if (barcode.length() < 6 || barcode.length() > 32) {
            throw new BusinessException("BARCODE_INVALID", "Mã vạch phải dài 6–32 ký tự.");
        }
        Product product = products.findByStoreIdAndBarcode(storeId, barcode).orElseGet(Product::new);
        boolean created = product.getId() == null;
        if (created) {
            product.setStoreId(storeId);
            product.setBarcode(barcode);
            product.setCreatedAt(Instant.now());
        }
        product.setName(form.getName().trim());
        product.setPrice(form.getPrice());
        product.setUnit(form.getUnit().trim());
        product.setStock(form.getStock());
        if (form.getImage() != null && !form.getImage().isEmpty()) {
            if (!imageStorage.enabled()) {
                throw new BusinessException("IMAGE_DISABLED", "Chưa cấu hình lưu ảnh.");
            }
            product.setImageKey(imageStorage.upload(form.getImage(), storeId));
        } else if (form.getImageKey() != null && !form.getImageKey().isBlank()) {
            product.setImageKey(form.getImageKey());
        }
        product.setActive(form.isActive());
        product.setUpdatedAt(Instant.now());
        product.setDeletedAt(null);
        return products.save(product);
    }

    @Transactional
    public Product quickCreate(Long storeId, String barcode, String name, long price, String unit, int stock) {
        ProductForm form = new ProductForm();
        form.setBarcode(barcode);
        form.setName(name);
        form.setPrice(price);
        form.setUnit(unit);
        form.setStock(stock);
        return createOrUpdate(storeId, form);
    }

    @Transactional
    public void toggle(Long storeId, Long productId) {
        Product product = require(storeId, productId);
        product.setActive(!product.isActive());
        product.setUpdatedAt(Instant.now());
        products.save(product);
    }

    @Transactional
    public void softDelete(Long storeId, Long productId) {
        Product product = require(storeId, productId);
        product.setDeletedAt(Instant.now());
        product.setActive(false);
        products.save(product);
    }

    public BarcodeLookupService.BarcodeResult lookup(String barcode) {
        return barcodeLookup.lookup(barcode);
    }
}
