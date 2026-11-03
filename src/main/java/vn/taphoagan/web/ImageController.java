package vn.taphoagan.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.taphoagan.service.ImageStoragePort;

import java.net.URI;

@Controller
public class ImageController {

    private final ImageStoragePort storage;

    public ImageController(ImageStoragePort storage) {
        this.storage = storage;
    }

    @GetMapping("/images")
    public ResponseEntity<Void> image(@RequestParam String key) {
        if (!storage.enabled()) {
            return ResponseEntity.notFound().build();
        }
        String url = storage.url(key);
        if (url == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
    }
}
