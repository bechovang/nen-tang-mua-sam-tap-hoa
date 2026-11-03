package vn.taphoagan.web;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.taphoagan.domain.Order;
import vn.taphoagan.domain.PosSession;
import vn.taphoagan.domain.Store;
import vn.taphoagan.domain.User;
import vn.taphoagan.service.CurrentUserService;
import vn.taphoagan.service.OrderService;
import vn.taphoagan.service.PosService;
import vn.taphoagan.service.StoreService;
import vn.taphoagan.web.form.ActionForms;

@Controller
@RequestMapping("/pos")
public class PosController {

    private final PosService pos;
    private final StoreService storeService;
    private final OrderService orders;
    private final CurrentUserService currentUserService;

    public PosController(PosService pos, StoreService storeService, OrderService orders,
                         CurrentUserService currentUserService) {
        this.pos = pos;
        this.storeService = storeService;
        this.orders = orders;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String pos(Authentication authentication, Model model) {
        Store store = store(authentication);
        PosSession session = pos.currentSession(store.getId());
        model.addAttribute("store", store);
        model.addAttribute("posSession", session);
        if (session != null) {
            model.addAttribute("lines", pos.lines(session.getId()));
            model.addAttribute("total", pos.total(session.getId()));
        }
        return "store/pos";
    }

    @PostMapping("/open")
    public String open(Authentication authentication) {
        pos.open(store(authentication).getId());
        return "redirect:/pos";
    }

    @PostMapping("/cancel")
    public String cancel(Authentication authentication, RedirectAttributes redirect) {
        pos.cancel(store(authentication).getId());
        redirect.addFlashAttribute("success", "Đã hủy phiên bán.");
        return "redirect:/pos";
    }

    @PostMapping("/add")
    public String add(@RequestParam String barcode, Authentication authentication, RedirectAttributes redirect) {
        try {
            pos.addByBarcode(store(authentication).getId(), barcode);
        } catch (NotFoundException ex) {
            redirect.addAttribute("barcode", barcode.trim());
            return "redirect:/pos/quick-create";
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/pos";
    }

    @GetMapping("/quick-create")
    public String quickCreateForm(@RequestParam String barcode, Authentication authentication, Model model) {
        ActionForms.QuickCreateForm form = new ActionForms.QuickCreateForm();
        form.setBarcode(barcode);
        model.addAttribute("barcode", barcode);
        model.addAttribute("quickCreateForm", form);
        return "store/pos-quick-create";
    }

    @PostMapping("/quick-create")
    public String quickCreate(@Valid @ModelAttribute("quickCreateForm") ActionForms.QuickCreateForm form,
                              BindingResult binding, Authentication authentication, Model model,
                              RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            model.addAttribute("barcode", form.getBarcode());
            return "store/pos-quick-create";
        }
        pos.quickCreate(store(authentication).getId(), form);
        redirect.addFlashAttribute("success", "Đã tạo nhanh mặt hàng.");
        return "redirect:/pos";
    }

    @PostMapping("/line/{id}")
    public String updateLine(@PathVariable Long id, @RequestParam int quantity, Authentication authentication) {
        pos.updateLine(store(authentication).getId(), id, quantity);
        return "redirect:/pos";
    }

    @PostMapping("/line/{id}/remove")
    public String removeLine(@PathVariable Long id, Authentication authentication) {
        pos.removeLine(store(authentication).getId(), id);
        return "redirect:/pos";
    }

    @PostMapping("/complete")
    public String complete(Authentication authentication, RedirectAttributes redirect) {
        try {
            Order order = pos.complete(store(authentication).getId());
            return "redirect:/pos/receipt/" + order.getCode();
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/pos";
        }
    }

    @GetMapping("/receipt/{code}")
    public String receipt(@PathVariable String code, Authentication authentication, Model model) {
        Store store = store(authentication);
        Order order = pos.receipt(code, store);
        model.addAttribute("order", order);
        model.addAttribute("items", orders.items(order));
        return "store/receipt";
    }

    private Store store(Authentication authentication) {
        User user = currentUserService.require(authentication);
        return storeService.requireByOwner(user.getId());
    }
}
