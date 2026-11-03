package vn.taphoagan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.taphoagan.domain.User;
import vn.taphoagan.repository.UserRepository;
import vn.taphoagan.web.NotFoundException;

import static vn.taphoagan.domain.Enums.Role;

@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Transactional
    public User provision(String email, String name, String auth0Sub) {
        User user = (auth0Sub == null ? java.util.Optional.<User>empty() : users.findByAuth0Sub(auth0Sub))
                .or(() -> users.findByEmail(email))
                .orElseGet(() -> {
                    User created = new User();
                    created.setEmail(email);
                    created.setName(name);
                    created.setAuth0Sub(auth0Sub);
                    created.getRoles().add(Role.CUSTOMER);
                    return users.save(created);
                });
        if (user.getAuth0Sub() == null && auth0Sub != null) {
            user.setAuth0Sub(auth0Sub);
        }
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        if (!user.getRoles().contains(Role.CUSTOMER)) {
            user.getRoles().add(Role.CUSTOMER);
        }
        return users.save(user);
    }

    @Transactional
    public void addRole(User user, Role role) {
        if (user.getRoles().contains(role)) {
            return;
        }
        user.getRoles().add(role);
        users.save(user);
    }

    public User requireByEmail(String email) {
        return users.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng."));
    }
}
