package vn.taphoagan.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@ConditionalOnProperty(name = "app.s3.enabled", havingValue = "false", matchIfMissing = true)
public class DisabledImageStorage implements ImageStoragePort {

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    public String upload(MultipartFile file, Long storeId) {
        return null;
    }

    @Override
    public StoredImage load(String key) {
        return null;
    }
}
