package vn.taphoagan.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import vn.taphoagan.domain.User;
import vn.taphoagan.repository.UserRepository;

import java.util.Optional;

@Service
public class CurrentUserService {

    private final UserRepository users;
    private final UserService userService;

    public CurrentUserService(UserRepository users, UserService userService) {
        this.users = users;
        this.userService = userService;
    }

    public Optional<User> find(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof OAuth2User oauth) {
            String email = oauth.getAttribute("email");
            String sub = oauth.getAttribute("sub");
            String name = oauth.getAttribute("name");
            if (email == null) {
                return Optional.empty();
            }
            return Optional.of(userService.provision(email, name, sub));
        }
        return users.findByEmail(authentication.getName());
    }

    public User require(Authentication authentication) {
        return find(authentication).orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Cần đăng nhập."));
    }
}
