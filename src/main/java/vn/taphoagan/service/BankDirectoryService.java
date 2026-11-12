package vn.taphoagan.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import vn.taphoagan.web.BusinessException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class BankDirectoryService {

    public record Bank(String code, String name, String bin) {
    }

    private final List<Bank> banks;

    public BankDirectoryService() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream input = new ClassPathResource("banks/vietqr.json").getInputStream()) {
            this.banks = List.copyOf(objectMapper.readValue(input, new TypeReference<List<Bank>>() {
            }));
        } catch (IOException ex) {
            throw new IllegalStateException("Không đọc được danh sách ngân hàng VietQR.", ex);
        }
    }

    public List<Bank> all() {
        return banks;
    }

    public Optional<Bank> find(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        String normalized = code.trim().toUpperCase(Locale.ROOT);
        return banks.stream().filter(bank -> bank.code().equals(normalized)).findFirst();
    }

    public Bank require(String code) {
        return find(code).orElseThrow(() ->
                new BusinessException("BANK_INVALID", "Ngân hàng không hợp lệ hoặc chưa được hỗ trợ."));
    }
}
