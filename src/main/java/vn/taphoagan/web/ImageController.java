package vn.taphoagan.web;

import org.springframework.http.CacheControl;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.taphoagan.service.ImageStoragePort;

import java.time.Duration;

@Controller
public class ImageController {

    private final ImageStoragePort storage;

    public ImageController(ImageStoragePort storage) {
        this.storage = storage;
    }

    @GetMapping("/images")
    public ResponseEntity<byte[]> image(@RequestParam String key) {
        ImageStoragePort.StoredImage image = storage.load(key);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        MediaType contentType;
        try {
            contentType = MediaType.parseMediaType(image.contentType());
        } catch (InvalidMediaTypeException ex) {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok()
                .contentType(contentType)
                .cacheControl(CacheControl.maxAge(Duration.ofMinutes(15)).cachePrivate())
                .body(image.bytes());
    }
}
