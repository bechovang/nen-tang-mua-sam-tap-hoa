# Build Report — Tạp Hóa Gần v0.1

## Trạng thái

- Build: **PASS** (`mvn -DskipTests package`)
- Test: **PASS** — 4/4 (2 integration, 2 unit)
- Local runtime: `http://localhost:8080`
- Database: PostgreSQL 18.6 (Docker Compose), Flyway v5
- Auth0: cấu hình profile `pilot`; local dùng form login demo

## Đã triển khai

| Khu vực | Nội dung |
|---|---|
| Auth | OAuth2 Client Auth0, provisioning theo `sub`, role CSDL, local form login, route bảo vệ |
| Cửa hàng | Hồ sơ, bán kính, giờ mở cửa, giao/pickup, ghim Leaflet/OSM, trạng thái mở/tạm đóng |
| Danh mục | Quét Quagga2, tra Open Food Facts + cache, tạo/cập nhật, bật/tắt, xóa mềm, tìm kiếm |
| Khám phá | Vị trí geolocation/ward, Haversine, danh sách tiệm gần, tìm theo tên tiệm/món |
| Đặt hàng | Giỏ session, checkout, giao/pickup, tiền mặt, idempotency, hủy, lịch sử, đặt lại |
| Xử lý đơn | Hàng đợi, xác nhận/từ chối, soạn bằng quét, thiếu hàng, state machine, stock atomic |
| POS | Phiên bán, quét/tìm món, tạo nhanh, hoàn tất, biên nhận, trừ kho |
| Thông báo | In-app list + badge, email after-commit, job pickup quá hạn advisory lock |
| Ảnh | Upload JPEG/PNG/WebP ≤ 2 MB, S3-compatible (S3Mock local), signed URL qua `/images` |
| Hạ tầng | Flyway, profile local/test/pilot, Dockerfile, README, seed demo |

## Smoke test đã chạy

- `GET /` → 200, render layout tiếng Việt.
- `POST /location` → 200, danh sách tiệm gần.
- `GET /stores/tap-hoa-chi-lan-2` → 200, danh mục 8 món.
- `GET /search?q=Chị Lan` → 200, tìm theo tên tiệm.
- Login `store@local` → `/store`, `/store/catalog`, `/store/scan`, `/store/orders`, `/pos` → 200.
- Login `customer@local` → thêm giỏ → checkout → đơn `TH-...` tạo thành công.
- Store xác nhận → soạn → `PREPARING` → `OUT_FOR_DELIVERY` → `COMPLETED`; tồn kho giảm.
- POS mở phiên → thêm món → hoàn tất → biên nhận `POS-...`; tồn kho giảm.
- `/api/barcode/000000000000` → fallback `MANUAL`; `/store/scan` render form nhập tay.
- `/notifications` → 200.
- Upload ảnh PNG qua `/store/scan` → lưu `imageKey`, `/images?key=...` trả 302 tới S3Mock và ảnh 200.
- Screenshot mobile home, store page, store dashboard, POS.

## Sai lệch / giới hạn còn lại

- **Auth0 thật cần tenant + Google connection**; không thể smoke login nếu chưa có credential. Profile `pilot` đã cấu hình qua env.
- Ảnh dùng S3Mock local; pilot cần cấu hình S3-compatible thật.
- Nhắc pickup trước 30 phút và job quá hạn `NO_SHOW` đã chạy (migration V6).
- Không có thanh toán online, courier, admin console, app native/PWA, bản đồ discovery, đánh giá sao.
- Pilot một instance; nhiều instance cần Spring Session JDBC.
- Barcode camera cần HTTPS hoặc localhost; Quagga2 không hỗ trợ rung trên iOS Safari, đã có phản hồi màu/nội dung.

## Lệnh chạy

```bash
docker compose up -d
mvn spring-boot:run
# hoặc
mvn -DskipTests package
java -jar target/taphoagan-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

Tài khoản demo: `customer@local`, `store@local`, `owner2@local`, `owner3@local` / `password`.
