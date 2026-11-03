package vn.taphoagan.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoragePort {

    boolean enabled();

    String upload(MultipartFile file, Long storeId);

    String url(String key);
}
