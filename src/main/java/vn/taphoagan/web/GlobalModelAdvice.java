package vn.taphoagan.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.taphoagan.domain.User;
import vn.taphoagan.service.CartService;
import vn.taphoagan.service.CurrentUserService;
import vn.taphoagan.service.NotificationService;

@ControllerAdvice
public class GlobalModelAdvice {

    private final CurrentUserService currentUserService;
    private final CartService cartService;
    private final NotificationService notificationService;
    private final ViewFormat viewFormat;

    public GlobalModelAdvice(CurrentUserService currentUserService, CartService cartService,
                             NotificationService notificationService, ViewFormat viewFormat) {
        this.currentUserService = currentUserService;
        this.cartService = cartService;
        this.notificationService = notificationService;
        this.viewFormat = viewFormat;
    }

    @ModelAttribute("currentUser")
    public User currentUser(Authentication authentication) {
        return currentUserService.find(authentication).orElse(null);
    }

    @ModelAttribute("isStoreOwner")
    public boolean isStoreOwner(Authentication authentication) {
        return currentUserService.isStoreOwner(authentication);
    }

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        return cartService.count(session);
    }

    @ModelAttribute("unreadCount")
    public long unreadCount(Authentication authentication) {
        return currentUserService.find(authentication)
                .map(user -> notificationService.unread(user.getId())).orElse(0L);
    }

    @ModelAttribute("appName")
    public String appName() {
        return "Tạp Hóa Gần";
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("format")
    public ViewFormat format() {
        return viewFormat;
    }
}
