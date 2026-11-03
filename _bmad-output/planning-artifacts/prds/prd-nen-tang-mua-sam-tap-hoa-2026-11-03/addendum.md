# Phụ lục PRD — Ràng buộc kỹ thuật và quyết định triển khai

Phụ lục này chứa phần "làm thế nào" không thuộc PRD. PRD là nguồn sự thật về hành vi; tài liệu này ghi lại lựa chọn kỹ thuật, mô hình dữ liệu và các spike cần làm trước khi kiến trúc chính thức.

> **Đã chốt:** phiên bản stack và quyết định kỹ thuật cuối cùng nằm trong `../architecture/architecture-nen-tang-mua-sam-tap-hoa-2026-11-03/ARCHITECTURE-SPINE.md`. Phụ lục này giữ bối cảnh; khi lệch, architecture spine thắng.

## 1. Stack đề xuất

| Lớp | Lựa chọn | Lý do |
|---|---|---|
| Ngôn ngữ | Java 25 (LTS) | Ổn định, hỗ trợ record/pattern matching, phù hợp Spring Boot 4.x |
| Framework | Spring Boot 4.1.2 | Hệ sinh thái JPA, Validation, Mail có sẵn; Spring Security 7.1 |
| Build | Maven 3.9.x | Đơn giản, phổ biến, không cần cấu hình đa module ở v1 |
| View | Thymeleaf 3.1.x + Bootstrap 5.3.8 | Server-rendered, SEO tốt, không cần SPA; Bootstrap đủ component cho form/bảng/modal |
| JS | Vanilla JS + Quagga2 1.13.0 | 1D EAN/UPC/Code-128; đang active; không cần build pipeline phức tạp |
| Bảo mật | Spring Security 7.1 + OAuth2 Client (Auth0) | Luồng chuẩn OIDC, không dùng SDK Auth0/Okta |
| ORM | Spring Data JPA (Hibernate) | CRUD nhanh; câu truy vấn địa lý viết native query |
| CSDL | PostgreSQL 18.6 | Ổn định, hỗ trợ truy vấn hình học đơn giản; không cần PostGIS ở v1 |
| Migration | Flyway | Version hóa schema, chạy tự động khi khởi động |
| Email | Spring Mail (SMTP) | Thông báo giao dịch; có thể dùng nhà cung cấp SMTP bất kỳ |
| Test | JUnit 5, MockMvc, Testcontainers | Kiểm thử tích hợp với PostgreSQL thật |
| Deploy | Một container ứng dụng + PostgreSQL managed | Monolith đủ cho pilot; HTTPS bắt buộc |

## 2. Tích hợp Auth0

- Tạo **Regular Web Application** trong Auth0, bật connection Google.
- Callback URL: `https://{host}/login/oauth2/code/auth0`; logout URL: `https://{host}/`.
- Scope: `openid profile email`.
- Cấu hình qua biến môi trường: `AUTH0_DOMAIN`, `AUTH0_CLIENT_ID`, `AUTH0_CLIENT_SECRET`, `APP_BASE_URL`.
- Ánh xạ người dùng: `nguoi_dung.auth0_sub` unique; lần đăng nhập đầu tạo bản ghi với vai trò `KHACH_HANG`.
- Vai trò nghiệp vụ lưu trong CSDL, không phụ thuộc claim RBAC của Auth0. Lý do: cần cấp `CHU_TIEM` sau khi tạo cửa hàng và tránh phụ thuộc cấu hình tenant.
- Logout dùng RP-initiated logout của Auth0 để kết thúc cả phiên Google.
- Môi trường local: Auth0 development tenant với callback `http://localhost:8080/login/oauth2/code/auth0`; test tự động dùng security test support, không gọi Auth0 thật.

## 3. Quét mã vạch

- Thư viện: `Quagga2` 1.13.0 (1D: EAN/UPC/Code-128), self-host trong `static/js`; không dùng `html5-qrcode` vì maintainer đang tìm người nhận.
- Định dạng: EAN-13, EAN-8, UPC-A, Code-128.
- Ràng buộc camera: `facingMode: environment`, độ phân giải mục tiêu 1280×720; bật torch nếu thiết bị hỗ trợ.
- Chỉ chạy trên HTTPS hoặc localhost; nếu `navigator.mediaDevices` không có, hiển thị nhập tay.
- Chống quét trùng: bỏ qua cùng mã trong 1,5 giây; hiển thị phản hồi rung/nháy.
- Tra cứu: Open Food Facts API v2 `https://world.openfoodfacts.org/api/v2/product/{barcode}.json`, timeout 3 giây, gửi User-Agent định danh ứng dụng.
- Cache: bảng `ma_vach_cache` lưu `ma_vach`, `ten`, `anh_url`, `nguon`, `fetched_at`; TTL 30 ngày. Không gọi lại nguồn ngoài nếu cache còn hạn.
- Giới hạn: tối đa 60 lượt tra cứu/phút toàn hệ thống; một mã chỉ tra một lần/30 ngày.
- Rủi ro: độ phủ hàng Việt thấp. Spike bắt buộc: lấy mẫu 500 mã từ 3–5 tiệm, đo tỷ lệ trúng trước khi chốt SM-2.

## 4. Vị trí và tìm cửa hàng gần

- `cua_hang.vi_do` và `cua_hang.kinh_do` kiểu `numeric(9,6)`; đánh index tổ hợp.
- Truy vấn hai bước: lọc bounding box theo vĩ độ/kinh độ, sau đó tính khoảng cách Haversine trong SQL.
- Không dùng PostGIS ở v1; dữ liệu pilot nhỏ, Haversine đủ chính xác.
- Khoảng cách hiển thị làm tròn 100 m; khoảng cách dùng để kiểm tra bán kính làm tròn 10 m.
- Khách hàng: ưu tiên Geolocation API; fallback chọn phường từ bảng `phuong_pilot` (seed sẵn toạ độ trung tâm).
- Không geocode địa chỉ tự do trong v1 để tránh phụ thuộc nhà cung cấp và chi phí.
- Toạ độ khách chỉ lưu trong session; đơn hàng lưu địa chỉ giao và toạ độ đã chọn để phục vụ hỗ trợ.

## 5. Mô hình dữ liệu sơ bộ

> Tên bảng dưới đây là sketch ban đầu. Schema chính thức dùng tên tiếng Anh (`stores`, `products`, `orders`, `order_items`, …) theo `ARCHITECTURE-SPINE.md`.

| Bảng | Cột chính | Ghi chú |
|---|---|---|
| `nguoi_dung` | `id`, `auth0_sub` unique, `email`, `ten`, `vai_tro`, `created_at` | `vai_tro`: `KHACH_HANG`, `CHU_TIEM` |
| `cua_hang` | `id`, `chu_tiem_id`, `ten`, `dia_chi`, `vi_do`, `kinh_do`, `sdt`, `ban_kinh_km`, `giao_hang`, `nhan_tai_tiem`, `phi_giao_hang`, `don_toi_thieu`, `trang_thai` | Một `chu_tiem_id` chỉ có một cửa hàng ở v1 |
| `gio_mo_cua` | `id`, `cua_hang_id`, `thu`, `gio_mo`, `gio_dong` | 0 = Chủ nhật; hỗ trợ nhiều khung/ngày nếu cần |
| `mat_hang` | `id`, `cua_hang_id`, `ma_vach`, `ten`, `gia`, `don_vi`, `ton_kho`, `anh_url`, `con_hang`, `deleted_at` | Unique (`cua_hang_id`, `ma_vach`) |
| `ma_vach_cache` | `ma_vach` PK, `ten`, `anh_url`, `nguon`, `fetched_at` | Cache tra cứu ngoài |
| `don_hang` | `id`, `ma_don` unique, `cua_hang_id`, `khach_hang_id` nullable, `hinh_thuc`, `trang_thai`, `da_thu_tien`, `dia_chi_giao`, `sdt`, `ghi_chu`, `tam_tinh`, `phi_giao`, `tong_tien`, `gio_hen`, `created_at` | `hinh_thuc`: `GIAO_HANG`, `NHAN_TAI_TIEM`, `TAI_QUAY`; đơn tại quầy không cần `khach_hang_id` |
| `dong_don_hang` | `id`, `don_hang_id`, `mat_hang_id`, `ten_snapshot`, `don_gia_snapshot`, `don_vi_snapshot`, `so_luong`, `so_luong_da_soan`, `trang_thai_dong` | Snapshot giá/tên tại thời điểm đặt |
| `su_kien_don_hang` | `id`, `don_hang_id`, `tu_trang_thai`, `den_trang_thai`, `nguoi_thuc_hien`, `ghi_chu`, `created_at` | Audit trail |
| `phien_ban` | `id`, `cua_hang_id`, `trang_thai`, `created_at`, `updated_at` | Chỉ một phiên `DANG_MO`/cửa hàng |
| `dong_phien_ban` | `id`, `phien_ban_id`, `mat_hang_id` nullable, `ten`, `don_gia`, `so_luong` | Cho phép mặt hàng tạo nhanh chưa có ID trước khi lưu |
| `su_kien_ton_kho` | `id`, `mat_hang_id`, `thay_doi`, `ly_do`, `don_hang_id`, `created_at` | Lý do: `XAC_NHAN_DON`, `HUY_DON`, `BAN_TAI_QUAY`, `DIEU_CHINH` |
| `thong_bao` | `id`, `nguoi_dung_id`, `loai`, `tieu_de`, `noi_dung`, `da_doc`, `created_at` | Thông báo trong ứng dụng |
| `phuong_pilot` | `id`, `ten`, `vi_do`, `kinh_do` | Fallback vị trí |

Quy ước tiền: lưu `numeric(12,0)` theo VND, không dùng số thực. Thời gian lưu UTC, hiển thị `Asia/Ho_Chi_Minh`.

## 6. Máy trạng thái đơn hàng

| Từ | Đến | Ai được chuyển | Ghi chú |
|---|---|---|---|
| `CHO_XAC_NHAN` | `DA_XAC_NHAN` | Chủ tiệm | Gửi thông báo khách; giữ tồn kho |
| `CHO_XAC_NHAN` | `DA_HUY` | Chủ tiệm, khách hàng | Bắt buộc lý do nếu chủ tiệm hủy |
| `DA_XAC_NHAN` | `DANG_SOAN` | Chủ tiệm | Bắt đầu soạn hàng |
| `DA_XAC_NHAN` | `DA_HUY` | Chủ tiệm, khách hàng | Khách chỉ hủy trước khi soạn |
| `DANG_SOAN` | `SAN_SANG` | Chủ tiệm | Chỉ đơn nhận tại tiệm |
| `DANG_SOAN` | `DANG_GIAO` | Chủ tiệm | Chỉ đơn giao hàng |
| `DANG_SOAN` | `DA_HUY` | Chủ tiệm | Có lý do; hoàn tồn kho |
| `SAN_SANG` | `HOAN_TAT` | Chủ tiệm | Khách đã nhận và trả tiền |
| `SAN_SANG` | `KHONG_NHAN` | Chủ tiệm | Sau thời gian giữ; hoàn tồn kho |
| `DANG_GIAO` | `HOAN_TAT` | Chủ tiệm | Đã giao và thu tiền |
| `DANG_GIAO` | `DA_HUY` | Chủ tiệm | Giao thất bại; hoàn tồn kho |

Không có chuyển ngược từ `HOAN_TAT`, `DA_HUY`, `KHONG_NHAN`. Mọi chuyển ghi `su_kien_don_hang` trong cùng transaction.

## 7. Bảo mật

- Mọi route `/store/**`, `/pos/**`, `/orders/**` kiểm tra quyền sở hữu, không chỉ vai trò.
- Chống IDOR: truy vấn luôn kèm `chu_tiem_id`/`khach_hang_id` của người dùng hiện tại.
- CSRF bật cho form Thymeleaf; endpoint JSON dùng cookie phiên và header CSRF.
- Cookie phiên `HttpOnly`, `Secure`, `SameSite=Lax`; session fixation protection của Spring Security.
- Giới hạn tần suất: đăng nhập 10 lần/phút/IP; tra cứu mã vạch 60 lần/phút/hệ thống; tạo đơn 20 lần/phút/người dùng.
- Upload ảnh: chỉ JPEG/PNG/WebP, tối đa 2 MB, kiểm tra magic bytes, đổi tên tệp, không phục vụ từ thư mục thực thi.
- Không log token, email đầy đủ, số điện thoại đầy đủ hoặc toạ độ chính xác.
- Bí mật cấu hình qua biến môi trường; không commit `application-prod.yml` chứa secret.

## 8. Hiệu năng và vận hành

- Index: `cua_hang(vi_do, kinh_do)`, `mat_hang(cua_hang_id, deleted_at)`, `mat_hang(cua_hang_id, ma_vach)`, `don_hang(cua_hang_id, trang_thai, created_at)`, `don_hang(khach_hang_id, created_at)`.
- Phân trang mọi danh sách; tránh N+1 bằng `join fetch` hoặc projection.
- Cache cấu hình cửa hàng trong bộ nhớ 60 giây; cache tra cứu mã vạch trong CSDL.
- Health check `/actuator/health`; metrics qua Micrometer; log JSON có `requestId`.
- Backup PostgreSQL hằng ngày, giữ 14 ngày; migration chạy trước khi ứng dụng nhận traffic.
- Không có hàng đợi tin nhắn ở v1; email gửi đồng bộ với timeout 5 giây và ghi log lỗi.

## 9. Phương án đã loại

| Phương án | Vì sao loại |
|---|---|
| SPA React/Vue | Tăng độ phức tạp build và deploy; Thymeleaf đủ cho form/bảng, SEO tốt hơn |
| App native | Không cần cho pilot; camera trình duyệt đã đủ |
| PostGIS | Dữ liệu pilot nhỏ; Haversine + index đủ, giảm phụ thuộc vận hành |
| Microservices | Một đội nhỏ, một miền nghiệp vụ; monolith dễ triển khai và debug |
| Auth0 RBAC cho vai trò nghiệp vụ | Cấp quyền chủ tiệm sau khi tạo cửa hàng; lưu trong CSDL đơn giản hơn |
| CSDL sản phẩm nội bộ tự xây | Cần nhập liệu ban đầu; tra cứu mở + cache + nhập tay nhanh hơn cho pilot |
| Thanh toán online | Tăng phạm vi tuân thủ và đối soát; chưa có bằng chứng nhu cầu |
| PWA/offline | Không cần cho luồng quét có mạng; giảm phạm vi |

## 10. Spike trước khi chốt kiến trúc

1. **Auth0:** dựng tenant dev, đăng nhập Google, logout, ánh xạ người dùng; đo thời gian cấu hình.
2. **Độ phủ mã vạch:** lấy 500 mã thực tế từ 3–5 tiệm, đo tỷ lệ trả về tên/ảnh; quyết định có cần nhập thêm nguồn dữ liệu.
3. **Camera trên thiết bị thật:** iPhone Safari và Android Chrome tầm trung; đo thời gian nhận mã và tỷ lệ đọc thành công với bao bì nhăn/mờ.
4. **Vị trí:** đo sai số Geolocation trong khu phố; kiểm tra fallback chọn phường.
5. **Email:** kiểm tra khả năng gửi tới Gmail/Outlook, tránh spam; đo thời gian gửi.
6. **Hiệu năng tìm gần:** seed 500 cửa hàng giả lập, đo p95 của truy vấn Haversine.

## 11. Cấu hình và triển khai

- Biến môi trường tối thiểu: `APP_BASE_URL`, `AUTH0_DOMAIN`, `AUTH0_CLIENT_ID`, `AUTH0_CLIENT_SECRET`, `DB_URL`, `DB_USER`, `DB_PASSWORD`, `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`.
- Dockerfile đa tầng; chạy với user không phải root; health check.
- Flyway chạy khi khởi động; rollback bằng migration bù, không sửa migration đã phát hành.
- Môi trường: `local`, `test`, `pilot`. Không có môi trường production công khai trong giai đoạn pilot.
