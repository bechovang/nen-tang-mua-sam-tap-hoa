package vn.taphoagan.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vn.taphoagan.domain.Enums.Role;
import vn.taphoagan.domain.Store;
import vn.taphoagan.domain.User;
import vn.taphoagan.repository.StoreRepository;
import vn.taphoagan.repository.WardRepository;
import vn.taphoagan.service.CatalogService;
import vn.taphoagan.service.ImageStoragePort;
import vn.taphoagan.service.StoreService;
import vn.taphoagan.service.UserService;
import vn.taphoagan.web.form.ProductForm;
import vn.taphoagan.web.form.StoreForm;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;

@Component
@Profile("!local & !test")
@ConditionalOnProperty(name = "app.demo-seed", havingValue = "true")
@Order(20)
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    private final UserService users;
    private final StoreService stores;
    private final StoreRepository storeRepository;
    private final WardRepository wards;
    private final CatalogService catalog;
    private final ImageStoragePort images;

    public DemoDataSeeder(UserService users, StoreService stores, StoreRepository storeRepository,
                          WardRepository wards, CatalogService catalog, ImageStoragePort images) {
        this.users = users;
        this.stores = stores;
        this.storeRepository = storeRepository;
        this.wards = wards;
        this.catalog = catalog;
        this.images = images;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (storeRepository.count() > 0) {
            log.info("Demo seed skipped: stores already exist");
            return;
        }
        try {
            Long wardId = wards.findAllByOrderByNameAsc().stream().findFirst().map(w -> w.getId()).orElse(null);

            User lan = owner("demo.lan@taphoa.local", "Chị Lan");
            User minh = owner("demo.minh@taphoa.local", "Anh Minh");
            User hoa = owner("demo.hoa@taphoa.local", "Cô Hoa");

            Store lanStore = stores.saveForOwner(lan, form("Tạp hóa Chị Lan", "12 Nguyễn Trãi, Phường Bến Thành",
                    10.7725, 106.6980, "0901234567", wardId));
            Store minhStore = stores.saveForOwner(minh, form("Tạp hóa Anh Minh", "45 Lê Lợi, Phường Đa Kao",
                    10.7800, 106.7050, "0902345678", wardId));
            Store hoaStore = stores.saveForOwner(hoa, form("Tạp hóa Cô Hoa", "8 Trần Hưng Đạo, Phường 1",
                    10.7600, 106.7000, "0903456789", wardId));

            seed(lanStore, new Row[]{
                    new Row("893100000001", "Cà chua", 18000, "kg", 18, "organic-home-grown-tomatoes-unripe-to-ripe.jpg"),
                    new Row("893100000002", "Bắp cải", 20000, "cái", 12, "bap-cai.jpg"),
                    new Row("893100000003", "Rau muống", 8000, "bó", 25, "rau-muong.jpg"),
                    new Row("893100000004", "Khoai tây", 25000, "kg", 16, "khoai-tay.jpg")
            });
            seed(minhStore, new Row[]{
                    new Row("893100000011", "Hoa cúc", 35000, "bó", 10, "hoa-cuc.jpg"),
                    new Row("893100000012", "Rau củ hỗn hợp", 30000, "túi", 14, "rau-cu.jpg"),
                    new Row("893100000013", "Trứng gà", 30000, "hộp", 20, null),
                    new Row("893100000014", "Bánh mì", 12000, "cái", 30, null),
                    new Row("893100000015", "Sữa tươi", 32000, "chai", 12, "glass-milk-bottles.jpg"),
                    new Row("893100000016", "Mì gói", 5000, "gói", 40, "instant-noodle-balls.jpg")
            });
            seed(hoaStore, new Row[]{
                    new Row("893100000021", "Cà chua", 20000, "kg", 10, "organic-home-grown-tomatoes-unripe-to-ripe.jpg"),
                    new Row("893100000022", "Rau muống", 9000, "bó", 18, "rau-muong.jpg"),
                    new Row("893100000023", "Bắp cải", 22000, "cái", 8, "bap-cai.jpg")
            });
            log.info("Demo seed ready: 3 stores, 13 products");
        } catch (RuntimeException ex) {
            log.error("Demo seed failed: {}", ex.getMessage(), ex);
        }
    }

    private User owner(String email, String name) {
        User user = users.provision(email, name, null);
        users.addRole(user, Role.STORE_OWNER);
        return user;
    }

    private void seed(Store store, Row[] rows) {
        for (Row row : rows) {
            if (catalog.findByBarcode(store.getId(), row.barcode()) != null) {
                continue;
            }
            ProductForm form = new ProductForm();
            form.setBarcode(row.barcode());
            form.setName(row.name());
            form.setPrice(row.price());
            form.setUnit(row.unit());
            form.setStock(row.stock());
            form.setActive(true);
            if (row.image() != null && images.enabled()) {
                byte[] bytes = resource("/static/img/demo/" + row.image());
                if (bytes != null) {
                    form.setImage(new ClasspathImage(row.image(), bytes));
                }
            }
            catalog.createOrUpdate(store.getId(), form);
        }
    }

    private StoreForm form(String name, String address, double lat, double lng, String phone, Long wardId) {
        StoreForm form = new StoreForm();
        form.setName(name);
        form.setAddress(address);
        form.setLat(lat);
        form.setLng(lng);
        form.setPhone(phone);
        form.setWardId(wardId);
        form.setRadiusKm(BigDecimal.valueOf(1.5));
        form.setDeliveryEnabled(true);
        form.setPickupEnabled(true);
        form.setDeliveryFee(10000);
        form.setMinOrder(0);
        form.setOpenMinute(7 * 60);
        form.setCloseMinute(22 * 60);
        return form;
    }

    private byte[] resource(String path) {
        try (InputStream input = getClass().getResourceAsStream(path)) {
            if (input == null) {
                log.warn("Demo image missing: {}", path);
                return null;
            }
            return input.readAllBytes();
        } catch (IOException ex) {
            log.warn("Cannot read demo image {}: {}", path, ex.getMessage());
            return null;
        }
    }

    private record Row(String barcode, String name, long price, String unit, int stock, String image) {
    }

    private record ClasspathImage(String name, byte[] bytes) implements MultipartFile {

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return "image/jpeg";
        }

        @Override
        public boolean isEmpty() {
            return bytes.length == 0;
        }

        @Override
        public long getSize() {
            return bytes.length;
        }

        @Override
        public byte[] getBytes() {
            return bytes;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            Files.write(dest.toPath(), bytes);
        }
    }
}
