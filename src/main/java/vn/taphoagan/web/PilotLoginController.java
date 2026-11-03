package vn.taphoagan.web;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("!local & !test")
public class PilotLoginController {

    @GetMapping("/login")
    public String login() {
        return "redirect:/oauth2/authorization/auth0";
    }
}
