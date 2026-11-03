package vn.taphoagan.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.taphoagan.domain.Notification;
import vn.taphoagan.domain.Order;
import vn.taphoagan.domain.Store;
import vn.taphoagan.domain.User;
import vn.taphoagan.repository.NotificationRepository;
import vn.taphoagan.repository.UserRepository;

import java.util.List;

import static vn.taphoagan.domain.Enums.NotificationType;

@Service
public class NotificationService {

    private final NotificationRepository notifications;
    private final UserRepository users;
    private final ApplicationEventPublisher publisher;

    public NotificationService(NotificationRepository notifications, UserRepository users,
                               ApplicationEventPublisher publisher) {
        this.notifications = notifications;
        this.users = users;
        this.publisher = publisher;
    }

    public record EmailEvent(String to, String subject, String body) {
    }

    @Transactional
    public void notifyUser(User user, NotificationType type, String title, String body) {
        if (user == null) {
            return;
        }
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setType(type);
        notification.setTitle(title);
        notification.setBody(body);
        notifications.save(notification);
        publisher.publishEvent(new EmailEvent(user.getEmail(), title, body));
    }

    @Transactional
    public void notifyStore(Store store, NotificationType type, String title, String body) {
        users.findById(store.getOwnerId()).ifPresent(owner -> notifyUser(owner, type, title, body));
    }

    @Transactional
    public void notifyCustomer(Order order, NotificationType type, String title, String body) {
        if (order.getCustomerId() == null) {
            return;
        }
        users.findById(order.getCustomerId()).ifPresent(customer -> notifyUser(customer, type, title, body));
    }

    public List<Notification> list(Long userId) {
        return notifications.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long unread(Long userId) {
        return notifications.countByUserIdAndReadFalse(userId);
    }
}
