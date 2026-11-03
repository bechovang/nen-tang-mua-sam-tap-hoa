package vn.taphoagan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vn.taphoagan.repository.UserRepository;

@Configuration
@EnableWebSecurity
@Profile({"local", "test"})
public class LocalSecurityConfig {

    @Bean
    SecurityFilterChain localFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/stores/**", "/search", "/api/barcode/**",
                                "/css/**", "/js/**", "/vendor/**", "/img/**",
                                "/login", "/images", "/error", "/actuator/health").permitAll()
                        .requestMatchers("/store/**", "/pos/**").hasRole("STORE_OWNER")
                        .requestMatchers("/checkout/**", "/orders/**", "/notifications").authenticated()
                        .anyRequest().permitAll())
                .formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .csrf(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    UserDetailsService localUserDetailsService(UserRepository users, PasswordEncoder encoder) {
        return username -> users.findByEmail(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(encoder.encode("password"))
                        .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
