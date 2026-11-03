package vn.taphoagan.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import vn.taphoagan.domain.Store;
import vn.taphoagan.domain.User;
import vn.taphoagan.repository.StoreRepository;
import vn.taphoagan.repository.WardRepository;
import vn.taphoagan.service.CatalogService;
import vn.taphoagan.service.StoreService;
import vn.taphoagan.service.UserService;
import vn.taphoagan.web.form.StoreForm;

import java.math.BigDecimal;

@Component
@Profile({"local", "test"})
@ConditionalOnProperty(name = "app.seed", havingValue = "true")
public class LocalDataSeeder implements ApplicationRunner {

    private final UserService users;
    private final StoreService stores;
    private final StoreRepository storeRepository;
    private final WardRepository wards;
    private final CatalogService catalog;

    public LocalDataSeeder(UserService users, StoreService stores, StoreRepository storeRepository,
                           WardRepository wards, CatalogService catalog) {
        this.users = users;
        this.stores = stores;
        this.storeRepository = storeRepository;
        this.wards = wards;
        this.catalog = catalog;
    }

    @Override
    public void run(ApplicationArguments args) {
        User customer = users.provision("customer@local", "Khách Minh", null);
        User lan = users.provision("store@local", "Chị Lan", null);
        User minh = users.provision("owner2@local", "Anh Minh", null);
        User hoa = users.provision("owner3@local", "Cô Hoa", null);

        if (storeRepository.count() > 0) {
            return;
        }
        Long ward1 = wards.findAllByOrderByNameAsc().stream().findFirst().map(w -> w.getId()).orElse(null);

        Store lanStore = stores.saveForOwner(lan, form("Tạp hóa Chị Lan",
                "12 Nguyễn Trãi, Phường Bến Thành", 10.7725, 106.6980, "0901234567", ward1));
        Store minhStore = stores.saveForOwner(minh, form("Tạp hóa Anh Minh",
                "45 Lê Lợi, Phường Đa Kao", 10.7800, 106.7050, "0902345678", ward1));
        Store hoaStore = stores.saveForOwner(hoa, form("Tạp hóa Cô Hoa",
                "8 Trần Hưng Đạo, Phường 1", 10.7600, 106.7000, "0903456789", ward1));

        seed(lanStore, new String[][]{
                {"893000000001", "Nước mắm Nam Ngư 500ml", "35000", "chai", "20"},
                {"893000000002", "Trứng gà 10 quả", "30000", "hộp", "15"},
                {"893000000003", "Mì Hảo Hảo", "5000", "gói", "50"},
                {"893000000004", "Đường trắng 1kg", "24000", "kg", "10"},
                {"893000000005", "Dầu ăn Tường An 1L", "52000", "chai", "8"},
                {"893000000006", "Gạo ST25 5kg", "180000", "bao", "5"},
                {"893000000007", "Sữa Vinamilk 1L", "32000", "hộp", "12"},
                {"893000000008", "Bánh mì", "12000", "cái", "20"}
        });
        seed(minhStore, new String[][]{
                {"893000000011", "Nước suối Lavie 1.5L", "12000", "chai", "24"},
                {"893000000012", "Coca Cola 330ml", "10000", "lon", "30"},
                {"893000000013", "Kẹo bạc hà", "15000", "hộp", "10"},
                {"893000000014", "Giấy vệ sinh", "28000", "lốc", "6"}
        });
        seed(hoaStore, new String[][]{
                {"893000000021", "Rau muống", "8000", "bó", "20"},
                {"893000000022", "Cà chua", "18000", "kg", "12"},
                {"893000000023", "Trứng vịt", "32000", "hộp", "10"}
        });
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

    private void seed(Store store, String[][] rows) {
        for (String[] row : rows) {
            catalog.quickCreate(store.getId(), row[0], row[1], Long.parseLong(row[2]), row[3], Integer.parseInt(row[4]));
        }
    }
}
