package vn.taphoagan.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.taphoagan.domain.User;
import vn.taphoagan.repository.NotificationRepository;
import vn.taphoagan.service.CurrentUserService;
import vn.taphoagan.service.NotificationService;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notifications;
    private final NotificationRepository repository;
    private final CurrentUserService currentUserService;

    public NotificationController(NotificationService notifications, NotificationRepository repository,
                                  CurrentUserService currentUserService) {
        this.notifications = notifications;
        this.repository = repository;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String list(Authentication authentication, Model model) {
        User user = currentUserService.require(authentication);
        model.addAttribute("notifications", notifications.list(user.getId()));
        return "notifications";
    }

    @PostMapping("/read")
    public String markRead(Authentication authentication) {
        User user = currentUserService.require(authentication);
        repository.markAllRead(user.getId());
        return "redirect:/notifications";
    }
}
