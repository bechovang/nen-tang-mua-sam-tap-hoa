package vn.taphoagan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOsConfig {

    @Bean
    @ConditionalOnProperty(prefix = "payos", name = "enabled", havingValue = "true")
    PayOS payOS(@Value("${payos.client-id}") String clientId,
                @Value("${payos.api-key}") String apiKey,
                @Value("${payos.checksum-key}") String checksumKey) {
        return new PayOS(clientId, apiKey, checksumKey);
    }
}
