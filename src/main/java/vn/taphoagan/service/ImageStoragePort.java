package vn.taphoagan.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoragePort {

    record StoredImage(byte[] bytes, String contentType) {
    }

    boolean enabled();

    String upload(MultipartFile file, Long storeId);

    StoredImage load(String key);
}
