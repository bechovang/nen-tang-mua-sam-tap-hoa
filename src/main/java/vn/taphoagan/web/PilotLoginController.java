package vn.taphoagan.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("!local & !test")
public class PilotLoginController {

    private final String appBaseUrl;

    public PilotLoginController(@Value("${app.base-url}") String appBaseUrl) {
        this.appBaseUrl = appBaseUrl;
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:" + appBaseUrl + "/oauth2/authorization/auth0";
    }
}
