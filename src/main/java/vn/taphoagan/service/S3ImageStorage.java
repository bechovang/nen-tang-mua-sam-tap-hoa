package vn.taphoagan.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vn.taphoagan.web.BusinessException;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.s3.enabled", havingValue = "true")
public class S3ImageStorage implements ImageStoragePort {

    private static final Logger log = LoggerFactory.getLogger(S3ImageStorage.class);
    private static final Set<String> ALLOWED = Set.of("image/jpeg", "image/png", "image/webp");

    private final MinioClient client;
    private final String bucket;

    public S3ImageStorage(@Value("${app.s3.endpoint}") String endpoint,
                          @Value("${app.s3.access-key}") String accessKey,
                          @Value("${app.s3.secret-key}") String secretKey,
                          @Value("${app.s3.bucket}") String bucket,
                          @Value("${app.s3.region:us-east-1}") String region) {
        this.bucket = bucket;
        this.client = MinioClient.builder()
                .endpoint(endpoint)
                .region(region)
                .credentials(accessKey, secretKey)
                .build();
    }

    @PostConstruct
    void init() {
        try {
            boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
            log.info("Image storage ready: bucket={}", bucket);
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot initialise object storage: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public String upload(MultipartFile file, Long storeId) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new BusinessException("IMAGE_TOO_LARGE", "Ảnh tối đa 2 MB sau nén.");
        }
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!ALLOWED.contains(contentType)) {
            throw new BusinessException("IMAGE_TYPE", "Chỉ nhận JPEG, PNG hoặc WebP.");
        }
        String extension = switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
        String key = "stores/" + storeId + "/" + UUID.randomUUID() + extension;
        try (var input = file.getInputStream()) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .stream(input, file.getSize(), -1)
                    .contentType(contentType)
                    .build());
            return key;
        } catch (Exception ex) {
            throw new BusinessException("IMAGE_UPLOAD", "Không lưu được ảnh. Vui lòng thử lại.");
        }
    }

    @Override
    public StoredImage load(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        try {
            StatObjectResponse stat = client.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .build());
            try (GetObjectResponse object = client.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .build())) {
                String contentType = stat.contentType() == null
                        ? "application/octet-stream"
                        : stat.contentType();
                return new StoredImage(object.readAllBytes(), contentType);
            }
        } catch (ErrorResponseException ex) {
            if ("NoSuchKey".equals(ex.errorResponse().code())) {
                return null;
            }
            log.warn("Cannot load image {}: {}", key, ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.warn("Cannot load image {}: {}", key, ex.getMessage());
            return null;
        }
    }
}
