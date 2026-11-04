package vn.taphoagan.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Cloudflare Tunnel can terminate TLS and forward plain HTTP to the origin.
 * Honour X-Forwarded-Proto/CF-Visitor so redirects (including the login redirect)
 * stay on HTTPS, and explicitly allow geolocation for this origin.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProxyHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        String cloudflareVisitor = request.getHeader("CF-Visitor");
        boolean forwardedHttps = "https".equalsIgnoreCase(forwardedProto)
                || (cloudflareVisitor != null && cloudflareVisitor.contains("\"scheme\":\"https\""));

        HttpServletRequest effective = forwardedHttps && !request.isSecure()
                ? new HttpsRequest(request)
                : request;
        response.setHeader("Permissions-Policy", "geolocation=(self)");
        filterChain.doFilter(effective, response);
    }

    private static final class HttpsRequest extends HttpServletRequestWrapper {

        private HttpsRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getScheme() {
            return "https";
        }

        @Override
        public boolean isSecure() {
            return true;
        }

        @Override
        public int getServerPort() {
            return 443;
        }
    }
}
