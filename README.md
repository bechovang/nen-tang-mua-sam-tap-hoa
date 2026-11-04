# Tạp Hóa Gần

Nền tảng mua sắm tạp hóa khu phố: chủ tiệm quét mã vạch để đưa danh mục lên, khách gần đó xem hàng và đặt giao/pickup, chủ tiệm quản lý đơn và bán tại quầy.

## Stack

- Java 25 LTS, Spring Boot 4.1.2, Spring Security 7.1
- Thymeleaf + Bootstrap 5.3.8, Quagga2 1.13.0, Leaflet 1.9.4
- PostgreSQL 18.6, Flyway, Spring Data JPA
- Auth0 OIDC (Google) ở profile `pilot`; form login ở local
- JUnit 5 + Testcontainers PostgreSQL

## Chạy local

```bash
# 1. PostgreSQL + Mailpit + S3Mock
docker compose up -d

# 2. Chạy ứng dụng
mvn spring-boot:run
# hoặc: mvn -q -DskipTests package && java -jar target/taphoagan-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

Mở `http://localhost:8080`.

Tài khoản demo (mật khẩu `password`):

| Vai trò | Email |
|---|---|
| Khách | `customer@local` |
| Chủ tiệm | `store@local` |
| Chủ tiệm 2 | `owner2@local` |
| Chủ tiệm 3 | `owner3@local` |

Mailpit: `http://localhost:8025`. S3Mock: `http://localhost:9090`.

## Tính năng đã có

- Đăng nhập Google qua Auth0 ở pilot; form login demo ở local.
- Khách đã đăng nhập có thể tự mở cửa hàng: điền hồ sơ tiệm, ghim vị trí và bắt đầu đăng bán ngay.
- Hồ sơ cửa hàng, bán kính, giờ mở cửa, ghim vị trí bằng Leaflet/OSM.
- Quét mã EAN/UPC/Code-128 bằng Quagga2; tra cứu Open Food Facts, cache 30 ngày, nhập tay fallback.
- Danh mục: tạo/cập nhật, bật/tắt còn hàng, xóa mềm, tìm kiếm.
- Khám phá tiệm gần bằng Haversine; tìm theo tên tiệm hoặc món.
- Giỏ hàng session, checkout, giao hàng hoặc nhận tại tiệm, tiền mặt, idempotency.
- Máy trạng thái đơn, xác nhận/từ chối, soạn bằng quét, xử lý thiếu hàng, tồn kho atomic.
- POS tại quầy, tạo nhanh món, biên nhận, trừ tồn kho.
- Thông báo in-app + email after-commit; job pickup quá hạn dùng advisory lock.
- Lịch sử đơn, hủy, đặt lại; quyền sở hữu theo vai trò.

## Kiểm thử

```bash
mvn verify
```

Test dùng Testcontainers PostgreSQL 18, không dùng H2. Yêu cầu Docker đang chạy.

## Chạy pilot với Auth0

Tạo **Regular Web Application** trong Auth0, bật Google connection:

- Allowed Callback URL: `https://<host>/login/oauth2/code/auth0`
- Allowed Logout URL: `https://<host>/`

Tạo file `.env.pilot` từ template:

```bash
cp .env.pilot.example .env.pilot
# điền AUTH0_DOMAIN / AUTH0_CLIENT_ID / AUTH0_CLIENT_SECRET và DB/SMTP/S3 thật
```

Chạy:

```powershell
.\run-pilot.ps1
```

Hoặc chạy trực tiếp sau khi export đủ biến:

```bash
SPRING_PROFILES_ACTIVE=pilot \
DB_URL=jdbc:postgresql://... \
DB_USER=... DB_PASSWORD=... \
APP_BASE_URL=https://... \
AUTH0_DOMAIN=... AUTH0_CLIENT_ID=... AUTH0_CLIENT_SECRET=... \
MAIL_HOST=... MAIL_PORT=587 MAIL_USERNAME=... MAIL_PASSWORD=... \
S3_ENABLED=true S3_ENDPOINT=... S3_ACCESS_KEY=... S3_SECRET_KEY=... S3_BUCKET=... \
java -jar target/taphoagan-0.0.1-SNAPSHOT.jar
```

Pilot local test có thể dùng PostgreSQL/S3Mock/Mailpit trong `compose.yaml`:
`APP_BASE_URL=http://localhost:8081`, callback Auth0 `http://localhost:8081/login/oauth2/code/auth0`,
`MAIL_SMTP_AUTH=false`, `MAIL_STARTTLS=false`, `MAIL_PORT=1025`, `S3_ENDPOINT=http://localhost:9090`.

Ảnh mặt hàng dùng `ImageStoragePort` + S3-compatible. Local dùng S3Mock ở `http://localhost:9090`; pilot cấu hình `S3_ENDPOINT`, `S3_ACCESS_KEY`, `S3_SECRET_KEY`, `S3_BUCKET`. Ảnh private, trình duyệt tải qua `/images?key=...`; app đọc object từ S3 nội bộ và stream bytes, không redirect ra endpoint nội bộ và không ghi ra đĩa cục bộ.

## Triển khai production (Dokploy homeserver)

URL: **https://taphoa.phucnn.io.vn**

- `docker-compose.prod.yml`: app + PostgreSQL 18 + Garage S3 (self-hosted, thay MinIO vì image MinIO không còn publish).
- `Dockerfile`: runtime Alpine non-root, healthcheck `/actuator/health`.
- CI/CD: `.github/workflows/deploy.yml` — PR chạy `mvn verify`; push `main` build GHCR `:latest` + `:sha-<sha>`, pin compose sang sha rồi gọi Dokploy `compose.deploy`.
- Email production tắt (`MAIL_ENABLED=false`); không cần Mailpit.
- Ảnh stream qua app nên S3 endpoint (`http://garage:3900`) chỉ cần nội bộ.

Secrets GitHub repo:

| Name | Value |
|---|---|
| `DOKPLOY_URL` | `http://100.83.245.0:3000` |
| `DOKPLOY_API_KEY` | API key Dokploy |
| `DOKPLOY_COMPOSE_ID` | ID compose `taphoa` sau khi tạo |
| `TS_OAUTH_CLIENT_ID` / `TS_OAUTH_SECRET` | Tailscale OAuth tag `tag:ci` |

Variable: `DEPLOY_ENABLED=true`.

Auth0 Regular Web Application cần thêm:

- Allowed Callback URL: `https://taphoa.phucnn.io.vn/login/oauth2/code/auth0`
- Allowed Logout URL: `https://taphoa.phucnn.io.vn/`
- Allowed Web Origin: `https://taphoa.phucnn.io.vn`

Biến môi trường compose đặt trong Dokploy (không commit): xem `.env.prod.example`.

Mở cửa hàng: đăng nhập Google, bấm **Mở cửa hàng** ở header hoặc cuối trang chủ,
điền hồ sơ tiệm và lưu. Hệ thống tự thêm role `STORE_OWNER` cho tài khoản đó.

## Giới hạn v1

- Chỉ thanh toán tiền mặt; không có cổng thanh toán.
- Chưa có app native/PWA, bản đồ discovery, đánh giá sao, tài khoản nhân viên, admin console.
- Pilot chạy một instance; khi cần nhiều instance, chuyển session sang Spring Session JDBC.
- Camera cần HTTPS (localhost được phép).

## Tài liệu

- Brief: `_bmad-output/planning-artifacts/briefs/brief-nen-tang-mua-sam-tap-hoa-2026-11-03/brief.md`
- PRD: `_bmad-output/planning-artifacts/prds/prd-nen-tang-mua-sam-tap-hoa-2026-11-03/prd.md`
- UX: `_bmad-output/planning-artifacts/ux-designs/ux-nen-tang-mua-sam-tap-hoa-2026-11-03/`
- Architecture: `_bmad-output/planning-artifacts/architecture/architecture-nen-tang-mua-sam-tap-hoa-2026-11-03/ARCHITECTURE-SPINE.md`
- Epics: `_bmad-output/planning-artifacts/epics.md`
