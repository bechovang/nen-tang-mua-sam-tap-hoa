package vn.taphoagan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import vn.taphoagan.domain.User;
import vn.taphoagan.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@Profile("!local & !test")
public class PilotSecurityConfig {

    @Bean
    SecurityFilterChain pilotFilterChain(HttpSecurity http,
                                         OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService,
                                         ClientRegistrationRepository registrations,
                                         @Value("${app.base-url}") String appBaseUrl) throws Exception {
        OidcClientInitiatedLogoutSuccessHandler logoutHandler =
                new OidcClientInitiatedLogoutSuccessHandler(registrations);
        logoutHandler.setPostLogoutRedirectUri(appBaseUrl.endsWith("/") ? appBaseUrl : appBaseUrl + "/");
        LoginUrlAuthenticationEntryPoint loginEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");
        if (appBaseUrl.startsWith("https://")) {
            PortMapperImpl portMapper = new PortMapperImpl();
            portMapper.setPortMappings(java.util.Map.of("80", "443", "8080", "443"));
            loginEntryPoint.setPortMapper(portMapper);
            loginEntryPoint.setForceHttps(true);
        }

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/stores/**", "/search", "/api/barcode/**",
                                "/api/v1/payments/webhooks/**", "/payments/payos/**",
                                "/css/**", "/js/**", "/vendor/**", "/img/**",
                                "/login", "/images", "/error", "/actuator/health").permitAll()
                        .requestMatchers("/store/**", "/pos/**").authenticated()
                        .requestMatchers("/checkout/**", "/orders/**", "/notifications").authenticated()
                        .anyRequest().permitAll())
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(info -> info.oidcUserService(oidcUserService))
                        .defaultSuccessUrl("/", false))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(loginEntryPoint))
                .logout(logout -> logout.logoutSuccessHandler(logoutHandler))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1/payments/webhooks/**"));
        return http.build();
    }

    @Bean
    OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(UserService userService) {
        OidcUserService delegate = new OidcUserService();
        return request -> {
            OidcUser user = delegate.loadUser(request);
            Set<GrantedAuthority> authorities = new HashSet<>(user.getAuthorities());
            if (user.getEmail() != null) {
                User appUser = userService.provision(user.getEmail(), user.getFullName(), user.getSubject());
                appUser.getRoles().forEach(role ->
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name())));
            }
            return user.getUserInfo() == null
                    ? new DefaultOidcUser(authorities, user.getIdToken())
                    : new DefaultOidcUser(authorities, user.getIdToken(), user.getUserInfo());
        };
    }
}
