package vn.taphoagan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.taphoagan.domain.Store;
import vn.taphoagan.domain.StoreHour;
import vn.taphoagan.domain.User;
import vn.taphoagan.repository.StoreHourRepository;
import vn.taphoagan.repository.StoreRepository;
import vn.taphoagan.web.BusinessException;
import vn.taphoagan.web.NotFoundException;
import vn.taphoagan.web.form.StoreForm;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static vn.taphoagan.domain.Enums.Role;
import static vn.taphoagan.domain.Enums.StoreStatus;

@Service
public class StoreService {

    public static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final StoreRepository stores;
    private final StoreHourRepository hours;
    private final UserService userService;

    public StoreService(StoreRepository stores, StoreHourRepository hours, UserService userService) {
        this.stores = stores;
        this.hours = hours;
        this.userService = userService;
    }

    public Optional<Store> findByOwner(Long userId) {
        return stores.findByOwnerId(userId);
    }

    public Store requireByOwner(Long userId) {
        return stores.findByOwnerId(userId)
                .orElseThrow(() -> new NotFoundException("Bạn chưa có cửa hàng."));
    }

    public Store requireBySlug(String slug) {
        return stores.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng."));
    }

    public Store requireById(Long id) {
        return stores.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng."));
    }

    @Transactional
    public Store saveForOwner(User owner, StoreForm form) {
        if (!form.isDeliveryEnabled() && !form.isPickupEnabled()) {
            throw new BusinessException("STORE_FULFILLMENT_REQUIRED", "Phải bật ít nhất một hình thức nhận hàng.");
        }
        if (form.getCloseMinute() <= form.getOpenMinute()) {
            throw new BusinessException("STORE_HOURS_INVALID", "Giờ đóng cửa phải sau giờ mở cửa.");
        }
        Store store = stores.findByOwnerId(owner.getId()).orElseGet(Store::new);
        boolean created = store.getId() == null;
        if (created) {
            store.setOwnerId(owner.getId());
            store.setSlug(uniqueSlug(slugify(form.getName()) + "-" + owner.getId()));
        }
        store.setName(form.getName().trim());
        store.setAddress(form.getAddress().trim());
        store.setWardId(form.getWardId());
        store.setLat(form.getLat());
        store.setLng(form.getLng());
        store.setPhone(form.getPhone());
        store.setRadiusKm(form.getRadiusKm());
        store.setDeliveryEnabled(form.isDeliveryEnabled());
        store.setPickupEnabled(form.isPickupEnabled());
        store.setDeliveryFee(form.getDeliveryFee());
        store.setMinOrder(form.getMinOrder());
        store.setStatus(StoreStatus.OPEN);
        store = stores.save(store);

        hours.deleteAll(hours.findByStoreId(store.getId()));
        for (int day = 1; day <= 7; day++) {
            StoreHour hour = new StoreHour();
            hour.setStoreId(store.getId());
            hour.setDayOfWeek(day);
            hour.setOpenMinute(form.getOpenMinute());
            hour.setCloseMinute(form.getCloseMinute());
            hours.save(hour);
        }
        userService.addRole(owner, Role.STORE_OWNER);
        return store;
    }

    @Transactional
    public void setStatus(Store store, StoreStatus status) {
        store.setStatus(status);
        stores.save(store);
    }

    public StoreForm toForm(Store store) {
        StoreForm form = new StoreForm();
        form.setName(store.getName());
        form.setAddress(store.getAddress());
        form.setWardId(store.getWardId());
        form.setLat(store.getLat());
        form.setLng(store.getLng());
        form.setPhone(store.getPhone());
        form.setRadiusKm(store.getRadiusKm());
        form.setDeliveryEnabled(store.isDeliveryEnabled());
        form.setPickupEnabled(store.isPickupEnabled());
        form.setDeliveryFee(store.getDeliveryFee());
        form.setMinOrder(store.getMinOrder());
        List<StoreHour> list = hours.findByStoreId(store.getId());
        if (!list.isEmpty()) {
            form.setOpenMinute(list.getFirst().getOpenMinute());
            form.setCloseMinute(list.getFirst().getCloseMinute());
        }
        return form;
    }

    public boolean isOpenNow(Store store) {
        if (store.getStatus() != StoreStatus.OPEN) {
            return false;
        }
        List<StoreHour> list = hours.findByStoreId(store.getId());
        if (list.isEmpty()) {
            return true;
        }
        java.time.LocalDateTime now = java.time.LocalDateTime.now(ZONE);
        int minute = now.getHour() * 60 + now.getMinute();
        int day = now.getDayOfWeek().getValue();
        for (StoreHour hour : list) {
            if (hour.getDayOfWeek() != day) {
                continue;
            }
            if (hour.getOpenMinute() <= hour.getCloseMinute()) {
                if (minute >= hour.getOpenMinute() && minute < hour.getCloseMinute()) {
                    return true;
                }
            } else if (minute >= hour.getOpenMinute() || minute < hour.getCloseMinute()) {
                return true;
            }
        }
        return false;
    }

    private String uniqueSlug(String base) {
        String candidate = base;
        int i = 2;
        while (stores.findBySlug(candidate).isPresent()) {
            candidate = base + "-" + i++;
        }
        return candidate;
    }

    private String slugify(String value) {
        String normalized = java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return normalized.isBlank() ? "tiem" : normalized;
    }
}
