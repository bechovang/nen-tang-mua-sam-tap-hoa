package vn.taphoagan.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import vn.taphoagan.service.ImageStoragePort;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImageControllerTest {

    private static final byte[] BYTES = {1, 2, 3};

    private final MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new ImageController(new FakeStorage()))
            .build();

    @Test
    void streamsStoredImageWithContentType() throws Exception {
        mvc.perform(get("/images").param("key", "known"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"))
                .andExpect(content().bytes(BYTES))
                .andExpect(header().string("Cache-Control", containsString("private")));
    }

    @Test
    void returnsNotFoundForUnknownKey() throws Exception {
        mvc.perform(get("/images").param("key", "missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void fallsBackToOctetStreamForInvalidContentType() throws Exception {
        mvc.perform(get("/images").param("key", "bad-type"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/octet-stream"));
    }

    private static final class FakeStorage implements ImageStoragePort {

        @Override
        public boolean enabled() {
            return true;
        }

        @Override
        public String upload(MultipartFile file, Long storeId) {
            return null;
        }

        @Override
        public StoredImage load(String key) {
            return switch (key) {
                case "known" -> new StoredImage(BYTES, "image/jpeg");
                case "bad-type" -> new StoredImage(BYTES, "not a media type");
                default -> null;
            };
        }
    }
}