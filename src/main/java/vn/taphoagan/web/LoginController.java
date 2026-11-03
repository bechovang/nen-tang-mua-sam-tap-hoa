package vn.taphoagan.web;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile({"local", "test"})
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
