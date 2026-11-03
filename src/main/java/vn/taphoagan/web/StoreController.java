package vn.taphoagan.web;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.taphoagan.domain.*;
import vn.taphoagan.repository.WardRepository;
import vn.taphoagan.service.*;
import vn.taphoagan.web.form.ActionForms;
import vn.taphoagan.web.form.ProductForm;
import vn.taphoagan.web.form.StoreForm;

import java.util.List;

import static vn.taphoagan.domain.Enums.OrderStatus;
import static vn.taphoagan.domain.Enums.StoreStatus;

@Controller
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final CatalogService catalog;
    private final OrderService orders;
    private final CurrentUserService currentUserService;
    private final WardRepository wards;

    public StoreController(StoreService storeService, CatalogService catalog, OrderService orders,
                           CurrentUserService currentUserService, WardRepository wards) {
        this.storeService = storeService;
        this.catalog = catalog;
        this.orders = orders;
        this.currentUserService = currentUserService;
        this.wards = wards;
    }

    @GetMapping
    public String dashboard(Authentication authentication, Model model) {
        Store store = store(authentication);
        model.addAttribute("store", store);
        model.addAttribute("productCount", catalog.count(store.getId()));
        model.addAttribute("pendingCount", orders.countPending(store));
        List<Order> recent = orders.storeOrders(store).stream().limit(8).toList();
        model.addAttribute("recentOrders", recent);
        return "store/dashboard";
    }

    @GetMapping({"/onboarding", "/profile"})
    public String profileForm(Authentication authentication, Model model) {
        User user = currentUserService.require(authentication);
        Store store = storeService.findByOwner(user.getId()).orElse(null);
        StoreForm form = store == null ? new StoreForm() : storeService.toForm(store);
        model.addAttribute("storeForm", form);
        model.addAttribute("store", store);
        model.addAttribute("wards", wards.findAllByOrderByNameAsc());
        return "store/profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@Valid @ModelAttribute("storeForm") StoreForm form, BindingResult binding,
                              Authentication authentication, Model model, RedirectAttributes redirect) {
        User user = currentUserService.require(authentication);
        if (binding.hasErrors()) {
            model.addAttribute("wards", wards.findAllByOrderByNameAsc());
            model.addAttribute("store", storeService.findByOwner(user.getId()).orElse(null));
            return "store/profile";
        }
        try {
            storeService.saveForOwner(user, form);
            redirect.addFlashAttribute("success", "Đã lưu hồ sơ cửa hàng.");
            return "redirect:/store";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("wards", wards.findAllByOrderByNameAsc());
            model.addAttribute("store", storeService.findByOwner(user.getId()).orElse(null));
            return "store/profile";
        }
    }

    @PostMapping("/status")
    public String toggleStatus(@RequestParam StoreStatus status, Authentication authentication,
                               RedirectAttributes redirect) {
        Store store = store(authentication);
        storeService.setStatus(store, status);
        redirect.addFlashAttribute("success", status == StoreStatus.OPEN ? "Tiệm đang mở." : "Tiệm đã tạm đóng.");
        return "redirect:/store";
    }

    @GetMapping("/catalog")
    public String catalog(@RequestParam(defaultValue = "") String q, Authentication authentication, Model model) {
        Store store = store(authentication);
        model.addAttribute("store", store);
        model.addAttribute("products", catalog.search(store.getId(), q));
        model.addAttribute("q", q);
        return "store/catalog";
    }

    @GetMapping("/scan")
    public String scan(@RequestParam(required = false) String barcode, Authentication authentication, Model model) {
        Store store = store(authentication);
        model.addAttribute("store", store);
        ProductForm form = new ProductForm();
        if (barcode != null && !barcode.isBlank()) {
            BarcodeLookupService.BarcodeResult result = catalog.lookup(barcode);
            Product existing = catalog.findByBarcode(store.getId(), result.barcode());
            model.addAttribute("lookup", result);
            model.addAttribute("existing", existing);
            form.setBarcode(result.barcode());
            if (existing != null) {
                form.setName(existing.getName());
                form.setPrice(existing.getPrice());
                form.setUnit(existing.getUnit());
                form.setStock(existing.getStock());
                form.setActive(existing.isActive());
            } else if (result.found()) {
                form.setName(result.name());
            }
        }
        model.addAttribute("productForm", form);
        return "store/scan";
    }

    @PostMapping("/scan")
    public String saveScanned(@Valid @ModelAttribute("productForm") ProductForm form, BindingResult binding,
                              Authentication authentication, Model model, RedirectAttributes redirect) {
        Store store = store(authentication);
        if (binding.hasErrors()) {
            model.addAttribute("store", store);
            return "store/scan";
        }
        try {
            catalog.createOrUpdate(store.getId(), form);
            redirect.addFlashAttribute("success", "Đã lưu mặt hàng.");
            return "redirect:/store/scan";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("store", store);
            return "store/scan";
        }
    }

    @PostMapping("/catalog/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("productForm") ProductForm form,
                                BindingResult binding, Authentication authentication, RedirectAttributes redirect) {
        Store store = store(authentication);
        if (binding.hasErrors()) {
            redirect.addFlashAttribute("error", "Dữ liệu mặt hàng không hợp lệ.");
            return "redirect:/store/catalog";
        }
        catalog.createOrUpdate(store.getId(), form);
        redirect.addFlashAttribute("success", "Đã cập nhật mặt hàng.");
        return "redirect:/store/catalog";
    }

    @PostMapping("/catalog/{id}/toggle")
    public String toggleProduct(@PathVariable Long id, Authentication authentication, RedirectAttributes redirect) {
        catalog.toggle(store(authentication).getId(), id);
        redirect.addFlashAttribute("success", "Đã đổi trạng thái còn hàng.");
        return "redirect:/store/catalog";
    }

    @PostMapping("/catalog/{id}/delete")
    public String deleteProduct(@PathVariable Long id, Authentication authentication, RedirectAttributes redirect) {
        catalog.softDelete(store(authentication).getId(), id);
        redirect.addFlashAttribute("success", "Đã xóa mặt hàng khỏi danh mục bán.");
        return "redirect:/store/catalog";
    }

    @GetMapping("/orders")
    public String orderList(Authentication authentication, Model model) {
        Store store = store(authentication);
        model.addAttribute("store", store);
        model.addAttribute("orders", orders.storeOrders(store));
        return "store/orders";
    }

    @GetMapping("/orders/{code}")
    public String orderDetail(@PathVariable String code, Authentication authentication, Model model) {
        Store store = store(authentication);
        Order order = orders.storeOrder(code, store);
        model.addAttribute("store", store);
        model.addAttribute("order", order);
        model.addAttribute("items", orders.items(order));
        model.addAttribute("events", orders.events(order));
        return "store/order-detail";
    }

    @PostMapping("/orders/{code}/confirm")
    public String confirm(@PathVariable String code, Authentication authentication, RedirectAttributes redirect) {
        Store store = store(authentication);
        Order order = orders.storeOrder(code, store);
        try {
            orders.transition(order, OrderStatus.CONFIRMED, currentUserService.require(authentication), "Tiệm xác nhận");
            redirect.addFlashAttribute("success", "Đã xác nhận đơn.");
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/store/orders/" + code;
    }

    @PostMapping("/orders/{code}/reject")
    public String reject(@PathVariable String code, @ModelAttribute ActionForms.RejectForm form,
                         Authentication authentication, RedirectAttributes redirect) {
        Store store = store(authentication);
        Order order = orders.storeOrder(code, store);
        try {
            orders.cancelByStore(order, currentUserService.require(authentication), form.getReason());
            redirect.addFlashAttribute("success", "Đã từ chối đơn.");
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/store/orders/" + code;
    }

    @PostMapping("/orders/{code}/pick")
    public String pick(@PathVariable String code, @RequestParam String barcode, Authentication authentication,
                       RedirectAttributes redirect) {
        Store store = store(authentication);
        Order order = orders.storeOrder(code, store);
        OrderItem item = orders.itemByBarcode(order, barcode);
        if (item == null) {
            redirect.addFlashAttribute("error", "Mã này không thuộc đơn.");
            return "redirect:/store/orders/" + code;
        }
        orders.markPicked(order, item.getId(), item.getPickedQuantity() + 1, currentUserService.require(authentication));
        redirect.addFlashAttribute("success", "Đã soạn: " + item.getNameSnapshot());
        return "redirect:/store/orders/" + code;
    }

    @PostMapping("/orders/{code}/shortage")
    public String shortage(@PathVariable String code, @ModelAttribute ActionForms.ShortageForm form,
                           Authentication authentication, RedirectAttributes redirect) {
        Store store = store(authentication);
        Order order = orders.storeOrder(code, store);
        try {
            orders.adjustShortage(order, form.getItemId(), form.getQuantity(), currentUserService.require(authentication));
            redirect.addFlashAttribute("success", "Đã điều chỉnh thiếu hàng.");
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/store/orders/" + code;
    }

    @PostMapping("/orders/{code}/status")
    public String status(@PathVariable String code, @RequestParam OrderStatus target,
                         Authentication authentication, RedirectAttributes redirect) {
        Store store = store(authentication);
        Order order = orders.storeOrder(code, store);
        try {
            orders.transition(order, target, currentUserService.require(authentication), null);
            redirect.addFlashAttribute("success", "Đã chuyển trạng thái đơn.");
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/store/orders/" + code;
    }

    private Store store(Authentication authentication) {
        User user = currentUserService.require(authentication);
        return storeService.requireByOwner(user.getId());
    }
}
