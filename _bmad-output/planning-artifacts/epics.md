---
status: final
stepsCompleted: [1, 2, 3, 4]
inputDocuments:
  - prds/prd-nen-tang-mua-sam-tap-hoa-2026-11-03/prd.md
  - architecture/architecture-nen-tang-mua-sam-tap-hoa-2026-11-03/ARCHITECTURE-SPINE.md
  - ux-designs/ux-nen-tang-mua-sam-tap-hoa-2026-11-03/DESIGN.md
  - ux-designs/ux-nen-tang-mua-sam-tap-hoa-2026-11-03/EXPERIENCE.md
---

# Tạp Hóa Gần - Epic Breakdown

## Overview

Tài liệu này phân rã PRD, UX contract và Architecture spine thành epic và story triển khai được cho pilot Tạp Hóa Gần — nền tảng mua sắm tạp hóa khu phố, phone-first, tiếng Việt.

## Requirements Inventory

### Functional Requirements

- **FR-1:** Khách hàng và chủ tiệm đăng nhập bằng Google qua Auth0; callback tạo/cập nhật người dùng theo `sub`, không tạo trùng.
- **FR-2:** Hệ thống phân quyền `KHACH_HANG` và `CHU_TIEM`; một người có thể có cả hai; route bảo vệ theo vai trò và quyền sở hữu.
- **FR-3:** Người dùng đăng xuất, phiên idle hết hạn sau 8 giờ, cookie an toàn, quay lại URL ban đầu sau đăng nhập.
- **FR-4:** Khách ẩn danh xem trang chủ, cửa hàng gần, trang cửa hàng và danh mục; thêm giỏ ẩn danh; đăng nhập ở checkout.
- **FR-5:** Chủ tiệm tạo hồ sơ cửa hàng: tên, địa chỉ, toạ độ, SĐT, bán kính, giao/nhận, giờ mở cửa, phí giao, đơn tối thiểu.
- **FR-6:** Chủ tiệm sửa hồ sơ; thay đổi bán kính/hình thức nhận có hiệu lực trong 60 giây; không tắt cả giao và nhận.
- **FR-7:** Cửa hàng có trạng thái mở/tạm đóng; ngoài giờ coi là đóng; đơn cũ vẫn xử lý.
- **FR-8:** Chủ tiệm quét EAN-13/EAN-8/UPC-A/Code-128 bằng camera; kết quả ≤ 1 giây; phản hồi rung/nháy; nhập tay khi camera bị từ chối.
- **FR-9:** Hệ thống tra cứu mã vạch từ nguồn mở, timeout 3 giây, cache 30 ngày, không chặn tạo mặt hàng khi lỗi.
- **FR-10:** Chủ tiệm tạo mặt hàng với mã, tên, giá, đơn vị, tồn kho, ảnh tùy chọn; validate dữ liệu; quay lại phiên quét.
- **FR-11:** Quét lại mã đã có mở form cập nhật; không tạo trùng; giá cũ trong đơn không đổi.
- **FR-12:** Chủ tiệm xem, tìm, lọc, sửa, xóa mềm mặt hàng; phân trang; hiển thị số mặt hàng đang bán.
- **FR-13:** Hệ thống kiểm tra mã vạch hợp lệ/duy nhất; cho nhập tay; mã vạch bắt buộc cho mọi mặt hàng v1.
- **FR-14:** Khách chia sẻ vị trí hoặc chọn phường pilot; vị trí chỉ lưu session; khoảng cách làm tròn 100 m.
- **FR-15:** Hệ thống trả cửa hàng trong 5 km hoặc trong bán kính phục vụ, đang mở, có hàng; sắp theo khoảng cách; p95 ≤ 800 ms.
- **FR-16:** Khách mở trang cửa hàng, xem danh mục, tìm theo tên cửa hàng/món; mặt hàng hết hàng có nhãn và không thêm được.
- **FR-17:** Khách thêm/sửa/xóa món trong giỏ một cửa hàng; giỏ ẩn danh lưu session; gộp sau đăng nhập; cảnh báo giá đổi.
- **FR-18:** Khách chọn giao hàng hoặc nhận tại tiệm; kiểm tra bán kính/giờ/đơn tối thiểu; nhập địa chỉ giao và khung giờ nhận.
- **FR-19:** Checkout yêu cầu đăng nhập; hiển thị tóm tắt, tổng tiền, tiền mặt; nút đặt hàng idempotent; lưu snapshot.
- **FR-20:** Hệ thống tạo đơn `CHO_XAC_NHAN`, ghi order + items + event trong một transaction; thông báo cửa hàng; trả mã đơn.
- **FR-21:** Thanh toán tiền mặt khi giao hoặc tại tiệm; không thu thập dữ liệu thẻ; lưu cờ đã thu tiền.
- **FR-22:** Khách hủy trước `DANG_SOAN`; chủ tiệm hủy trước `HOAN_TAT` kèm lý do; hoàn tồn kho; thông báo hai bên.
- **FR-23:** Khách xem lịch sử đơn và đặt lại; bỏ món ngừng bán/hết hàng; hiển thị giá mới; không tự tạo đơn.
- **FR-24:** Chủ tiệm xem đơn mới/đang xử lý/hoàn tất; badge; tự làm mới 30 giây; cảnh báo đơn chờ quá 15 phút.
- **FR-25:** Chủ tiệm xác nhận hoặc từ chối đơn; từ chối bắt buộc lý do; cảnh báo thiếu tồn nhưng vẫn cho xác nhận.
- **FR-26:** Chủ tiệm soạn đơn bằng quét mã; tăng số lượng đã soạn; cảnh báo quét sai/vượt; hoàn tất khi đủ.
- **FR-27:** Chủ tiệm đánh dấu thiếu hàng, nhập số lượng thực tế; tính lại tổng; khách chấp nhận hoặc hủy; hoàn tồn phần giảm.
- **FR-28:** Máy trạng thái đơn hợp lệ; mỗi lần chuyển ghi event; đơn tại quầy tạo trực tiếp `HOAN_TAT`.
- **FR-29:** Hệ thống giữ/hoàn/trừ tồn kho theo đơn; không âm; ghi stock event; không có kiểm kê/nhập kho.
- **FR-30:** Chủ tiệm mở phiên bán tại quầy; tối đa một phiên mở; lưu server; hủy phiên.
- **FR-31:** Chủ tiệm quét/tìm món thêm vào hóa đơn; sửa số lượng/xóa; tổng tiền cập nhật ngay.
- **FR-32:** Mã chưa có trong danh mục mở modal tạo nhanh; lưu vào danh mục và hóa đơn; có thể bỏ qua.
- **FR-33:** Chủ tiệm hoàn tất bán, trừ tồn kho, hiển thị biên nhận; không in máy in; hóa đơn trống không hoàn tất.
- **FR-34:** Khách xem trạng thái và dòng thời gian đơn; tự làm mới 30 giây; đặt lại sau hoàn tất.
- **FR-35:** Hệ thống thông báo trong ứng dụng và email khi đơn đổi trạng thái; lỗi email không chặn giao dịch.
- **FR-36:** Đơn nhận tại tiệm giữ 2 giờ sau giờ hẹn; quá hạn chuyển `KHONG_NHAN`, hoàn tồn kho, nhắc trước 30 phút.

### NonFunctional Requirements

- **NFR-1 Hiệu năng:** tìm gần p95 ≤ 800 ms/500 cửa hàng; trang cửa hàng p95 ≤ 1,5 s; lưu mặt hàng ≤ 1 s; checkout p95 ≤ 2 s.
- **NFR-2 Sẵn sàng:** 99,5% trong 6:00–22:00; bảo trì ngoài khung.
- **NFR-3 Bảo mật:** kiểm soát truy cập mọi route/API; CSRF; cookie `HttpOnly`/`Secure`; rate limit login/lookup/order.
- **NFR-4 Quyền riêng tư:** vị trí sau đồng ý; không lộ toạ độ khách; log vị trí ≤ 30 ngày; đơn giữ 24 tháng.
- **NFR-5 Tương thích:** iOS Safari 16+, Chrome Android 110+; 360 px; camera HTTPS.
- **NFR-6 Tiếp cận:** WCAG 2.1 AA; touch 44×44 px; tương phản; form có nhãn/lỗi rõ.
- **NFR-7 Quan sát:** log JSON có request ID; metric quét/tra cứu/trạng thái/lỗi email.
- **NFR-8 Toàn vẹn:** snapshot giá; tồn kho không âm; tạo đơn idempotent; audit trạng thái.
- **NFR-9 Ngôn ngữ:** tiếng Việt, UTF-8, VND, `Asia/Ho_Chi_Minh`.
- **NFR-10 Chịu lỗi:** lookup lỗi > 3 s chuyển nhập tay; lỗi email không chặn; mất mạng không mất dữ liệu.

### Additional Requirements

- **AR-1:** Modular monolith, package-by-feature, bốn lớp `web → application → domain` + `infrastructure`; không microservices.
- **AR-2:** Mỗi bảng một module sở hữu; cross-module chỉ qua application service/query port; không JPA association chéo module.
- **AR-3:** Auth0 OIDC là danh tính; `user_roles` + `stores.owner_id` là phân quyền; route `/store/**`, `/pos/**`, `/orders/**` kiểm tra cả hai.
- **AR-4:** Tiền `long` VND / DB `bigint`; thời gian `Instant` UTC; snapshot tên/giá/đơn vị/phí giao/địa chỉ trên đơn.
- **AR-5:** Một enum `OrderStatus` + bảng chuyển hợp lệ; chỉ `OrderApplicationService` đổi trạng thái; `@Version`; ghi `order_events`.
- **AR-6:** `StockService` là nơi duy nhất ghi tồn kho; `UPDATE ... WHERE stock >= q`; ghi `stock_events`.
- **AR-7:** `BarcodeLookupPort` timeout 3 s, adapter Open Food Facts, cache `barcode_cache` 30 ngày; lỗi trả rỗng.
- **AR-8:** Một SQL function `distance_km`; `DiscoveryQuery` và `StoreQueryPort` đều dùng; không Haversine thứ hai; không PostGIS.
- **AR-9:** Spring Security OAuth2 Client; CSRF; cookie an toàn; upload ảnh ≤ 2 MB kiểm tra magic bytes; rate limit ở edge.
- **AR-10:** Domain event + `@TransactionalEventListener(AFTER_COMMIT)` cho email/thông báo; không SMTP trong transaction.
- **AR-11:** Flyway only; `ddl-auto=validate`; migration bất biến; PostgreSQL 18.6.
- **AR-12:** Profile `local`/`test`/`pilot`; secret qua env; backup PostgreSQL hằng ngày 14 ngày.
- **AR-13:** `ProblemDetail` + `code`; template lỗi HTML; log JSON `requestId`/`userId`/`orderCode`; không PII.
- **AR-14:** JUnit 5 + MockMvc + Testcontainers PostgreSQL 18; không H2; `mvn verify` là cổng.
- **AR-15:** Thymeleaf server-rendered; một layout + fragment; form POST + CSRF; self-host Bootstrap 5.3.8 và Quagga2 1.13.0; không CDN.
- **AR-16:** `ImageStoragePort` S3-compatible; bucket private; lưu object key; signed URL; ảnh tắt nếu chưa cấu hình.
- **AR-17:** Tạo đơn một transaction: reload store/product, kiểm tra mở/bán kính/đơn tối thiểu/tồn, snapshot, idempotency key.
- **AR-18:** Một phiên POS `OPEN`/store; `completeSale` idempotent; đơn POS tạo `COMPLETED` + event; dòng POS snapshot.
- **AR-19:** `order_events`, `stock_events` append-only; DB user chỉ INSERT/SELECT.
- **AR-20:** Giỏ trong HTTP session; không bảng `carts`; login giữ session; session idle 8 giờ; một cửa hàng/giỏ.
- **AR-21:** Job `@Scheduled` 5 phút + `pg_try_advisory_lock` xử lý pickup quá hạn; idempotent.
- **AR-22:** Stack: Java 25 LTS, Spring Boot 4.1.2, Spring Security 7.1, Thymeleaf 3.1, Bootstrap 5.3.8, Quagga2 1.13.0, PostgreSQL 18.6, Flyway, Auth0 OIDC.
- **AR-23:** Một container Spring Boot + managed PostgreSQL + HTTPS edge; source tree module `auth/store/catalog/discovery/ordering/pos/notification/shared`.

### UX Design Requirements

- **UX-DR1:** Implement design tokens từ `DESIGN.md` thành CSS variables + Bootstrap overrides: surface, ink, primary, accent, success/warning/danger, dark variants.
- **UX-DR2:** Self-host Lora + Be Vietnam Pro; áp dụng đúng ramp display/headline/title/body/label/caption/price; giá dùng tabular-nums.
- **UX-DR3:** Xây component brand: button primary/secondary/ghost, input, store card, product card, status chip, bottom nav, scan frame, toast, FAB quét, quantity stepper.
- **UX-DR4:** Phone-first responsive: <480 một cột; 480–767 hai cột hàng; 768–1023 tablet; ≥1024 customer 720px, store sidebar 240px/1200px, POS hai cột.
- **UX-DR5:** IA khách bottom nav 4 (Nhà/Tìm/Giỏ/Đơn); chủ tiệm bottom nav 5 (Đơn/Danh mục/Quét/Bán/Tiệm); chuyển chế độ ở header.
- **UX-DR6:** Luồng quét: mở camera do người dùng chạm; khung 1:1; đèn pin/nhập mã; debounce 1,5 s; phản hồi rung nếu hỗ trợ + nháy xanh; iOS không rung.
- **UX-DR7:** State patterns: chưa có vị trí, từ chối vị trí, không có tiệm, tiệm đóng, hết hàng, giá đổi, thêm món tiệm khác, dưới đơn tối thiểu, camera denied, mã không tìm thấy, quét trùng, mất mạng, đơn chờ, từ chối, thiếu hàng, quá hạn nhận, POS trống, POS phiên khác, session hết hạn.
- **UX-DR8:** Accessibility WCAG 2.1 AA: 44px target, focus visible, aria-live cho quét/lỗi, nhãn form, 200% zoom, reduced motion, không chỉ dùng màu.
- **UX-DR9:** Microcopy tiếng Việt theo voice table: "Tiệm đang mở", "Còn 12 món", "Đã gửi tới tiệm", "Không tìm thấy mã này…"; VND/time format; không emoji/exclamation.
- **UX-DR10:** 6 key flows (mở tiệm, đặt giao, nhận tại tiệm, POS, soạn đơn, đặt lại) là acceptance scenarios xuyên epic.
- **UX-DR11:** Không trang chi tiết món, không bản đồ, không đánh giá sao, không carousel/infinite scroll/hover-only.
- **UX-DR12:** Dark mode tùy chọn với token `*-dark`; light mặc định; không auto theo hệ thống.
- **UX-DR13:** Order status chip + timeline dùng đúng nhãn: Chờ tiệm xác nhận, Tiệm đã xác nhận, Đang soạn, Sẵn sàng nhận, Đang giao, Hoàn tất, Đã hủy, Không nhận.
- **UX-DR14:** POS receipt hiển thị mã, thời gian, món, tổng, tiền khách đưa/thừa; không in máy in.
- **UX-DR15:** Bottom nav badge chỉ cho Giỏ và Đơn; active state dùng primary; nhãn không bị ẩn khi zoom.

### FR Coverage Map

- **FR-1:** Epic 1 — Đăng nhập Google qua Auth0.
- **FR-2:** Epic 1 — Vai trò và phân quyền.
- **FR-3:** Epic 1 — Phiên và đăng xuất.
- **FR-4:** Epic 2 — Xem ẩn danh.
- **FR-5:** Epic 1 — Tạo hồ sơ cửa hàng.
- **FR-6:** Epic 1 — Cập nhật hồ sơ và bán kính.
- **FR-7:** Epic 1 — Trạng thái mở/tạm đóng/giờ mở cửa.
- **FR-8:** Epic 1 — Quét mã vạch bằng camera.
- **FR-9:** Epic 1 — Tra cứu mã vạch và điền sẵn.
- **FR-10:** Epic 1 — Tạo mặt hàng.
- **FR-11:** Epic 1 — Cập nhật khi quét lại.
- **FR-12:** Epic 1 — Quản lý danh mục hàng.
- **FR-13:** Epic 1 — Kiểm tra mã vạch và nhập tay.
- **FR-14:** Epic 2 — Xác định vị trí khách hàng.
- **FR-15:** Epic 2 — Danh sách cửa hàng gần.
- **FR-16:** Epic 2 — Trang cửa hàng và tìm kiếm.
- **FR-17:** Epic 2 (thêm món từ trang tiệm) và Epic 3 (giỏ hàng đầy đủ, stepper, cross-store).
- **FR-18:** Epic 3 — Chọn giao hàng hoặc nhận tại tiệm.
- **FR-19:** Epic 3 — Checkout và đăng nhập.
- **FR-20:** Epic 3 — Tạo đơn hàng.
- **FR-21:** Epic 3 — Thanh toán tiền mặt.
- **FR-22:** Epic 3 — Hủy đơn hàng.
- **FR-23:** Epic 3 — Lịch sử và đặt lại đơn.
- **FR-24:** Epic 4 — Danh sách đơn hàng của tiệm.
- **FR-25:** Epic 4 — Xác nhận hoặc từ chối đơn.
- **FR-26:** Epic 4 — Soạn hàng bằng quét mã.
- **FR-27:** Epic 4 — Xử lý thiếu hàng.
- **FR-28:** Epic 4 — Máy trạng thái đơn hàng.
- **FR-29:** Epic 4 — Trừ tồn kho và nhật ký kho.
- **FR-30:** Epic 5 — Mở phiên bán tại quầy.
- **FR-31:** Epic 5 — Quét và thêm mặt hàng vào hóa đơn.
- **FR-32:** Epic 5 — Tạo nhanh mặt hàng khi mã chưa có.
- **FR-33:** Epic 5 — Hoàn tất bán và biên nhận.
- **FR-34:** Epic 6 — Trạng thái đơn cho khách hàng.
- **FR-35:** Epic 6 — Thông báo đơn hàng.
- **FR-36:** Epic 6 — Chính sách giữ đơn nhận tại tiệm.

**NFR coverage map**

- **NFR-1:** Story 7.2.
- **NFR-2:** Story 7.6.
- **NFR-3:** Story 7.3.
- **NFR-4:** Story 7.3.
- **NFR-5:** Story 7.4.
- **NFR-6:** Story 7.4.
- **NFR-7:** Story 7.5.
- **NFR-8:** Story 7.1.
- **NFR-9:** Story 1.1, 7.4.
- **NFR-10:** Story 1.5, 3.3, 5.2.

**AR coverage map**

- **AR-1, AR-2, AR-11, AR-14, AR-15, AR-22, AR-23:** Story 1.1, 7.1, 7.6.
- **AR-3, AR-9:** Story 1.2, 1.3, 7.3.
- **AR-4, AR-5, AR-17, AR-19, AR-20:** Story 3.4, 3.5, 4.5, 4.6.
- **AR-6:** Story 1.6, 4.6, 5.4.
- **AR-7:** Story 1.5, 4.3, 5.2.
- **AR-8:** Story 1.4, 2.1, 2.2, 3.2.
- **AR-10:** Story 6.2.
- **AR-12:** Story 7.6.
- **AR-13:** Story 7.5.
- **AR-16:** Story 1.6.
- **AR-18:** Story 5.1, 5.4.
- **AR-21:** Story 6.3.

**UX-DR coverage map**

- **UX-DR1:** Story 1.1, 7.4.
- **UX-DR2:** Story 1.1, 7.4.
- **UX-DR3:** Story 1.1, 2.2, 2.3, 3.2.
- **UX-DR4:** Story 1.1, 7.4.
- **UX-DR5:** Story 2.5, 4.1.
- **UX-DR6:** Story 1.5, 5.2.
- **UX-DR7:** Story 2.1, 2.2, 3.2, 3.3, 4.4, 5.1, 6.1.
- **UX-DR8:** Story 7.4.
- **UX-DR9:** Story 1.1, 3.3, 3.4.
- **UX-DR10:** Story 1.4, 3.4, 4.3, 5.4, 6.1.
- **UX-DR11:** Story 2.5, 3.1.
- **UX-DR12:** Story 7.4.
- **UX-DR13:** Story 6.1.
- **UX-DR14:** Story 5.4.
- **UX-DR15:** Story 2.5, 4.1, 7.4.

### Story → FR Coverage

| Story | FRs |
|---|---|
| 1.1 | Nền tảng (AR) |
| 1.2 | FR-1, FR-3 |
| 1.3 | FR-2 |
| 1.4 | FR-5 |
| 1.5 | FR-8, FR-9 |
| 1.6 | FR-10, FR-11, FR-13 |
| 1.7 | FR-12 |
| 1.8 | FR-7 |
| 1.9 | FR-6 |
| 2.1 | FR-14 |
| 2.2 | FR-15 |
| 2.3 | FR-16, FR-17 |
| 2.4 | FR-16 |
| 2.5 | FR-4 |
| 3.1 | FR-17 |
| 3.2 | FR-17, FR-18 |
| 3.3 | FR-19 |
| 3.4 | FR-20, FR-21 |
| 3.5 | FR-22 |
| 3.6 | FR-23 |
| 4.1 | FR-24 |
| 4.2 | FR-25 |
| 4.3 | FR-28 |
| 4.4 | FR-26 |
| 4.5 | FR-27 |
| 4.6 | FR-29 |
| 5.1 | FR-30 |
| 5.2 | FR-31 |
| 5.3 | FR-32 |
| 5.4 | FR-33 |
| 6.1 | FR-34 |
| 6.2 | FR-35 |
| 6.3 | FR-36 |
| 7.1 | NFR-8, AR-14 |
| 7.2 | NFR-1 |
| 7.3 | NFR-3, NFR-4 |
| 7.4 | NFR-5, NFR-6 |
| 7.5 | NFR-7 |
| 7.6 | NFR-2 |

## Epic List

### Epic 1: Mở tiệm trong 15 phút

Chủ tiệm đăng nhập Google qua Auth0, tạo hồ sơ cửa hàng, quét mã vạch để đưa danh mục lên và quản lý danh mục. Sau epic này, một tiệm có thể sống trên nền tảng mà không cần thao tác thủ công của đội vận hành.

**FRs covered:** FR-1, FR-2, FR-3, FR-5, FR-6, FR-7, FR-8, FR-9, FR-10, FR-11, FR-12, FR-13.

### Epic 2: Tìm tiệm gần và xem hàng

Khách hàng ẩn danh chọn vị trí, thấy các tiệm gần đang mở, mở trang tiệm và xem giá, tồn kho, hình thức nhận hàng; tìm được tiệm hoặc món trong khu vực. Epic này biến danh mục của Epic 1 thành thứ khách nhìn thấy.

**FRs covered:** FR-4, FR-14, FR-15, FR-16, FR-17 (điểm thêm vào giỏ từ trang tiệm).

### Epic 3: Đặt hàng giao hoặc nhận tại tiệm

Khách hàng thêm món vào giỏ, chọn giao hàng hoặc nhận tại tiệm, đăng nhập ở checkout, đặt đơn thanh toán tiền mặt, hủy đơn trong giới hạn và đặt lại đơn cũ. Sau epic này, đơn hàng thật sự đi vào hệ thống.

**FRs covered:** FR-17, FR-18, FR-19, FR-20, FR-21, FR-22, FR-23.

### Epic 4: Xử lý đơn tại tiệm

Chủ tiệm thấy hàng đợi đơn, xác nhận hoặc từ chối, soạn hàng bằng quét mã, xử lý thiếu hàng, chuyển trạng thái và cập nhật tồn kho đúng. Epic này khép vòng đơn online từ phía tiệm.

**FRs covered:** FR-24, FR-25, FR-26, FR-27, FR-28, FR-29.

### Epic 5: Bán tại quầy bằng quét mã

Chủ tiệm bán trực tiếp cho khách vãng lai: mở phiên, quét món, tạo nhanh món chưa có, tính tiền mặt và hiển thị biên nhận; tồn kho được trừ. Epic này dùng chung danh mục của Epic 1 và không cần đơn online.

**FRs covered:** FR-30, FR-31, FR-32, FR-33.

### Epic 6: Theo dõi đơn và thông báo

Khách hàng và chủ tiệm luôn biết đơn đang ở đâu qua trạng thái, dòng thời gian, thông báo trong ứng dụng và email; đơn nhận tại tiệm quá hạn được xử lý tự động. Epic này hoàn thiện niềm tin vận hành.

**FRs covered:** FR-34, FR-35, FR-36.

### Epic 7: Pilot an toàn, nhanh và đáng tin

Đưa các NFR và ràng buộc kiến trúc vào sản phẩm: hiệu năng, bảo mật, quyền riêng tư, accessibility, quan sát, migration, backup, test harness và triển khai pilot. Người dùng pilot nhận được trải nghiệm nhanh, an toàn, không mất đơn.

**FRs covered:** không có FR trực tiếp; bao phủ NFR-1..NFR-10 và AR-1..AR-23.

**Dependencies:** Epic 2 dùng dữ liệu Epic 1; Epic 3 dùng Epic 2; Epic 4 dùng Epic 3; Epic 5 dùng Epic 1; Epic 6 dùng Epic 3/4; Epic 7 xuyên suốt và chốt trước khi mở pilot.

## Epic 1: Mở tiệm trong 15 phút

Chủ tiệm đăng nhập Google qua Auth0, tạo hồ sơ cửa hàng, quét mã vạch để đưa danh mục lên và quản lý danh mục. Sau epic này, một tiệm có thể sống trên nền tảng mà không cần thao tác thủ công của đội vận hành.

### Story 1.1: Khung ứng dụng, layout và test harness

As a thành viên đội phát triển,
I want một ứng dụng Spring Boot chạy được với layout Thymeleaf, migration và test harness,
So that mọi story sau có nền tảng nhất quán để build và kiểm thử.

**Acceptance Criteria:**

- **Given** repo trống, **When** chạy `mvn spring-boot:run` với profile `local`, **Then** ứng dụng khởi động trên cổng cấu hình và `GET /actuator/health` trả `200`.
- **Given** ứng dụng đã chạy, **When** mở `/`, **Then** trang dùng layout Thymeleaf chung, có `lang="vi"`, viewport mobile và các design token CSS từ `DESIGN.md`.
- **Given** database PostgreSQL 18.6 rỗng, **When** khởi động, **Then** Flyway chạy migration baseline và Hibernate `ddl-auto=validate` không báo lỗi; baseline chỉ tạo metadata/schema_version, không tạo bảng nghiệp vụ.
- **Given** một test tích hợp bất kỳ, **When** chạy `mvn verify`, **Then** test dùng Testcontainers PostgreSQL 18, không dùng H2, và build fail nếu test fail.
- **And** source tree có các module `auth/store/catalog/discovery/ordering/pos/notification/shared` theo `ARCHITECTURE-SPINE.md`.

### Story 1.2: Đăng nhập Google qua Auth0

As a chủ tiệm,
I want đăng nhập bằng tài khoản Google qua Auth0,
So that tôi vào được khu vực quản lý tiệm mà không phải tạo mật khẩu mới.

**Acceptance Criteria:**

- **Given** khách chưa đăng nhập, **When** bấm "Đăng nhập bằng Google", **Then** trình duyệt chuyển tới Auth0 với connection Google và quay lại `/login/oauth2/code/auth0`.
- **Given** callback thành công, **When** hệ thống xử lý `sub`, **Then** tạo người dùng mới hoặc cập nhật người dùng cũ, không tạo bản ghi trùng; email đã xác thực được lưu.
- **Given** người dùng đã đăng nhập, **When** bấm đăng xuất, **Then** phiên local bị xóa, chuyển qua RP-initiated logout của Auth0 và quay lại trang chủ.
- **Given** URL yêu cầu đăng nhập trước đó, **When** đăng nhập xong, **Then** người dùng quay lại đúng URL đó.
- **Given** đăng nhập thất bại, **When** Auth0 trả lỗi, **Then** hiển thị thông báo tiếng Việt và không tạo phiên.

### Story 1.3: Vai trò và bảo vệ route

As a chủ tiệm,
I want hệ thống chỉ cho tôi vào khu vực quản lý tiệm của mình,
So that dữ liệu cửa hàng và đơn hàng không bị người khác xem hoặc sửa.

**Acceptance Criteria:**

- **Given** người dùng đăng nhập lần đầu, **When** tài khoản được tạo, **Then** `user_roles` có `KHACH_HANG`.
- **Given** người dùng tạo cửa hàng thành công, **When** hồ sơ được lưu, **Then** `user_roles` có thêm `CHU_TIEM`.
- **Given** người dùng không có vai trò `CHU_TIEM`, **When** truy cập `/store/**` hoặc `/pos/**`, **Then** nhận `403`.
- **Given** chủ tiệm A đã đăng nhập, **When** truy cập đơn của chủ tiệm B, **Then** nhận `404` và không lộ dữ liệu.
- **Given** khách ẩn danh, **When** truy cập `/`, `/stores/**`, `/search`, **Then** được phép; khi truy cập `/checkout` hoặc `/orders`, **Then** chuyển tới Auth0.

### Story 1.4: Tạo hồ sơ cửa hàng

As a chủ tiệm,
I want tạo hồ sơ cửa hàng với địa chỉ, bán kính và hình thức nhận hàng,
So that khách gần đó có thể tìm thấy tiệm và biết tiệm giao hàng hay cho nhận tại tiệm.

**Acceptance Criteria:**

- **Given** chủ tiệm đã đăng nhập và chưa có cửa hàng, **When** mở `/store/onboarding`, **Then** thấy form tên tiệm, địa chỉ, số điện thoại, bán kính 0,5–5 km, giao hàng, nhận tại tiệm, giờ mở cửa, phí giao và đơn tối thiểu.
- **Given** form đang mở, **When** chủ tiệm chọn "Dùng vị trí tiệm" hoặc ghim trên bản đồ Leaflet/OSM, **Then** vĩ độ/kinh độ được điền và có thể sửa tay.
- **Given** chủ tiệm bỏ trống tên, số điện thoại sai hoặc tắt cả giao hàng và nhận tại tiệm, **When** bấm lưu, **Then** form hiện lỗi cạnh trường và không lưu.
- **Given** dữ liệu hợp lệ, **When** bấm lưu, **Then** cửa hàng được tạo, chủ tiệm được cấp `CHU_TIEM`, và chuyển tới phiên quét đầu tiên.
- **Given** chủ tiệm đã có cửa hàng, **When** mở onboarding lần nữa, **Then** hệ thống trả lỗi nghiệp vụ "Mỗi chủ tiệm chỉ có một cửa hàng trong v1".

### Story 1.5: Quét mã vạch và tra cứu sản phẩm

As a chủ tiệm,
I want quét mã vạch trên kệ để hệ thống điền sẵn tên và ảnh,
So that tôi đưa được nhiều món lên danh mục mà không phải gõ từng tên.

**Acceptance Criteria:**

- **Given** chủ tiệm ở màn hình quét, **When** bấm "Bật camera" và cấp quyền, **Then** camera sau mở với khung quét 1:1, có nút đèn pin và "Nhập mã".
- **Given** mã EAN-13, EAN-8, UPC-A hoặc Code-128 nằm trong khung, **When** Quagga2 giải mã, **Then** hệ thống trả kết quả trong ≤ 1 giây, bỏ qua cùng mã trong 1,5 giây và hiện phản hồi nháy xanh.
- **Given** có mã vạch, **When** tra cứu Open Food Facts, **Then** request có timeout 3 giây; kết quả được cache 30 ngày; lỗi/timeout trả form nhập tay với mã đã điền.
- **Given** quyền camera bị từ chối hoặc thiết bị không hỗ trợ, **When** mở màn hình quét, **Then** hiện hướng dẫn cấp quyền và ô nhập mã tay; không chặn toàn bộ màn hình.
- **Given** mất mạng giữa phiên quét, **When** chủ tiệm đã nhập dữ liệu, **Then** dữ liệu không bị xóa và có nút thử lại.

### Story 1.6: Tạo hoặc cập nhật mặt hàng từ mã vạch

As a chủ tiệm,
I want lưu mặt hàng với giá, đơn vị và tồn kho sau khi quét,
So that món đó xuất hiện trong danh mục bán của tiệm.

**Acceptance Criteria:**

- **Given** mã vạch mới, **When** form tạo mở, **Then** chủ tiệm nhập tên, giá > 0, đơn vị bán, tồn kho ≥ 0 và ảnh tùy chọn; lưu thành công quay lại phiên quét với thông báo "Đã thêm: {tên}".
- **Given** mã đã có trong cửa hàng, **When** quét lại, **Then** mở form cập nhật giá/tồn hiện tại, không tạo bản ghi trùng.
- **Given** mã đã thuộc cửa hàng khác, **When** quét trong cửa hàng hiện tại, **Then** vẫn cho tạo mặt hàng mới.
- **Given** mã nhập tay, **When** lưu, **Then** mã được trim, chuyển chữ hoa, dài 6–32 ký tự; mã trùng trong cùng cửa hàng trả lỗi và mở mặt hàng hiện có.
- **Given** ảnh được tải lên, **When** lưu, **Then** ảnh ≤ 2 MB, chỉ JPEG/PNG/WebP, kiểm tra magic bytes; nếu chưa cấu hình S3 thì trường ảnh bị tắt.

### Story 1.7: Quản lý danh mục hàng

As a chủ tiệm,
I want xem, tìm, lọc, sửa và xóa mềm mặt hàng,
So that danh mục luôn khớp với kệ hàng thực tế.

**Acceptance Criteria:**

- **Given** cửa hàng có nhiều mặt hàng, **When** mở `/store/catalog`, **Then** danh sách phân trang 50 dòng, sắp xếp mới cập nhật trước, hiển thị tổng số mặt hàng đang bán.
- **Given** chủ tiệm nhập từ khóa, **When** tìm theo tên hoặc mã vạch, **Then** kết quả lọc đúng; có bộ lọc còn hàng/hết hàng.
- **Given** chủ tiệm sửa giá hoặc tồn kho, **When** lưu, **Then** danh sách cập nhật và giá trong đơn cũ không đổi.
- **Given** chủ tiệm xóa mềm mặt hàng, **When** xác nhận, **Then** mặt hàng biến khỏi danh sách bán nhưng vẫn hiển thị trong đơn cũ.
- **Given** chủ tiệm bật/tắt "còn hàng", **When** lưu, **Then** trạng thái hiển thị ngay trong danh mục và trang cửa hàng.

### Story 1.8: Trạng thái mở, tạm đóng và giờ mở cửa

As a chủ tiệm,
I want bật/tắt nhận đơn và đặt giờ mở cửa,
So that khách chỉ đặt khi tiệm thực sự có thể phục vụ.

**Acceptance Criteria:**

- **Given** chủ tiệm ở `/store`, **When** bật/tắt "Đang mở", **Then** trạng thái lưu ngay và hiển thị cho khách.
- **Given** chủ tiệm đặt giờ mở cửa theo ngày, **When** ngoài khung giờ, **Then** cửa hàng được coi là đóng và không nhận đơn mới.
- **Given** cửa hàng đang đóng, **When** khách mở danh sách gần, **Then** cửa hàng không xuất hiện trong kết quả.
- **Given** đơn đã tạo trước khi tiệm đóng, **When** tiệm tạm đóng, **Then** đơn vẫn tiếp tục xử lý bình thường.
- **Given** chủ tiệm cập nhật giờ mở cửa, **When** lưu, **Then** thay đổi có hiệu lực trong vòng 60 giây.

### Story 1.9: Cập nhật hồ sơ và bán kính

As a chủ tiệm,
I want sửa hồ sơ cửa hàng, bán kính và phí giao,
So that khách luôn thấy thông tin đúng sau khi tiệm thay đổi cách phục vụ.

**Acceptance Criteria:**

- **Given** cửa hàng đã tồn tại, **When** chủ tiệm mở `/store`, **Then** sửa được tên, địa chỉ, toạ độ, số điện thoại, bán kính, hình thức nhận hàng, phí giao và đơn tối thiểu.
- **Given** chủ tiệm đổi bán kính hoặc hình thức nhận hàng, **When** lưu, **Then** kết quả tìm kiếm và checkout phản ánh thay đổi trong vòng 60 giây.
- **Given** chủ tiệm đổi toạ độ, **When** lưu, **Then** khoảng cách hiển thị cho khách được cập nhật.
- **Given** chủ tiệm tắt cả giao hàng và nhận tại tiệm, **When** lưu, **Then** form trả lỗi và không lưu.
- **Given** chủ tiệm nhập đơn tối thiểu > 5.000.000 ₫ hoặc bán kính ngoài 0,5–5 km, **When** lưu, **Then** form trả lỗi cạnh trường.

## Epic 2: Tìm tiệm gần và xem hàng

Khách hàng ẩn danh chọn vị trí, thấy các tiệm gần đang mở, mở trang tiệm và xem giá, tồn kho, hình thức nhận hàng; tìm được tiệm hoặc món trong khu vực.

### Story 2.1: Chọn vị trí khách hàng

As a khách hàng,
I want chia sẻ vị trí hoặc chọn phường của mình,
So that tôi thấy những tiệm thực sự gần và có thể giao tới.

**Acceptance Criteria:**

- **Given** khách mở Nhà lần đầu, **When** chưa có vị trí, **Then** thấy lời mời "Dùng vị trí của tôi" và "Chọn phường"; không tự xin quyền khi tải trang.
- **Given** khách chọn "Dùng vị trí của tôi" và cấp quyền, **When** trình duyệt trả toạ độ, **Then** danh sách tiệm tải lại theo vị trí đó.
- **Given** khách từ chối quyền vị trí, **When** chọn phường trong khu vực pilot, **Then** hệ thống dùng toạ độ trung tâm phường và ghi nhớ trong session.
- **Given** khách đã chọn vị trí, **When** xem khoảng cách, **Then** hiển thị làm tròn 100 m và không lộ toạ độ chính xác cho cửa hàng.
- **Given** khách đóng trình duyệt, **When** quay lại, **Then** vị trí không được lưu vĩnh viễn nếu khách chưa đặt hàng.

### Story 2.2: Danh sách cửa hàng gần

As a khách hàng,
I want thấy các tiệm đang mở quanh mình kèm khoảng cách và hình thức nhận hàng,
So that tôi chọn được tiệm phù hợp mà không phải đi bộ dò từng nơi.

**Acceptance Criteria:**

- **Given** khách đã có vị trí, **When** mở Nhà, **Then** thấy cửa hàng trong 5 km hoặc trong bán kính phục vụ, đang mở và có ít nhất một mặt hàng còn hàng.
- **Given** danh sách có nhiều tiệm, **When** sắp xếp, **Then** khoảng cách tăng dần; cùng khoảng cách thì tiệm nhiều hàng còn hơn đứng trước.
- **Given** một thẻ tiệm, **When** hiển thị, **Then** có tên, khoảng cách, mở/đóng, giao/nhận và số món còn hàng.
- **Given** không có tiệm nào, **When** danh sách trống, **Then** hiện "Chưa có tiệm nào trong bán kính này. Thử chọn phường khác." kèm nút đổi phường.
- **Given** 500 cửa hàng trong dữ liệu, **When** đo p95, **Then** thời gian phản hồi ≤ 800 ms.

### Story 2.3: Trang cửa hàng và danh mục

As a khách hàng,
I want mở trang tiệm và xem danh mục, giá, tình trạng còn hàng,
So that tôi biết tiệm có món mình cần trước khi đặt hoặc đi tới.

**Acceptance Criteria:**

- **Given** khách chọn một thẻ tiệm, **When** mở `/stores/{id}`, **Then** thấy tên, địa chỉ, khoảng cách, giờ mở cửa, hình thức nhận hàng và danh mục.
- **Given** mặt hàng còn hàng, **When** hiển thị, **Then** có tên, đơn vị, giá và nút "+" thêm vào giỏ ngay tại chỗ.
- **Given** mặt hàng hết hàng, **When** hiển thị, **Then** có nhãn "Hết hàng", nút "+" bị vô hiệu và không đưa lên đầu danh sách.
- **Given** tiệm đang đóng, **When** khách mở trang, **Then** thấy nhãn "Đang đóng", vẫn xem danh mục nhưng không thêm được vào giỏ.
- **Given** trang được tải, **When** xem source, **Then** nội dung được render server-side và có title/meta mô tả cho cửa hàng.

### Story 2.4: Tìm kiếm tiệm và món

As a khách hàng,
I want tìm theo tên tiệm hoặc tên món trong khu vực của mình,
So that tôi tìm nhanh thứ mình cần mà không phải mở từng tiệm.

**Acceptance Criteria:**

- **Given** khách ở `/search`, **When** gõ tên tiệm, **Then** kết quả nhóm "Tiệm" hiển thị tiệm gần đang mở khớp tên.
- **Given** khách gõ tên món, **When** kết quả trả về, **Then** nhóm "Món" hiển thị tiệm gần đang bán món đó.
- **Given** không có kết quả, **When** tìm, **Then** hiện "Không tìm thấy tiệm hoặc món này." và gợi ý đổi phường/xóa từ khóa.
- **Given** khách gõ, **When** chờ 300 ms, **Then** kết quả cập nhật mà không cần nhấn Enter.
- **Given** khách ở ngoài khu vực pilot, **When** tìm, **Then** chỉ trả kết quả trong khu vực pilot và giải thích giới hạn.

### Story 2.5: Điều hướng khách và xem ẩn danh

As a khách hàng,
I want xem và thêm giỏ mà không cần tạo tài khoản,
So that tôi không bị chặn trước khi biết tiệm có thứ mình cần.

**Acceptance Criteria:**

- **Given** khách ẩn danh, **When** mở site, **Then** xem được Nhà, Tìm, Cửa hàng, Giỏ; chỉ checkout/đơn hàng yêu cầu đăng nhập.
- **Given** khách ở chế độ mua, **When** nhìn bottom nav, **Then** thấy 4 mục Nhà · Tìm · Giỏ · Đơn, mục đang chọn dùng màu primary, badge chỉ ở Giỏ và Đơn.
- **Given** khách thêm món ẩn danh, **When** đăng nhập ở checkout, **Then** giỏ vẫn còn nguyên.
- **Given** người dùng có cửa hàng, **When** mở header, **Then** thấy nút "Sang chế độ bán" và chuyển chế độ không đăng xuất, không mất giỏ.
- **Given** màn hình 360 px hoặc zoom 200%, **When** xem bottom nav, **Then** nhãn không bị ẩn và không tràn.

## Epic 3: Đặt hàng giao hoặc nhận tại tiệm

Khách hàng thêm món vào giỏ, chọn giao hàng hoặc nhận tại tiệm, đăng nhập ở checkout, đặt đơn thanh toán tiền mặt, hủy đơn trong giới hạn và đặt lại đơn cũ.

### Story 3.1: Thêm món vào giỏ

As a khách hàng,
I want thêm món và điều chỉnh số lượng ngay trên trang tiệm,
So that tôi gom đủ giỏ nhỏ mà không phải mở nhiều màn hình.

**Acceptance Criteria:**

- **Given** khách ở trang tiệm, **When** bấm "+" trên mặt hàng còn hàng, **Then** món được thêm vào giỏ, nút đổi thành stepper và badge Giỏ tăng.
- **Given** khách bấm "+" lần nữa, **When** số lượng tăng, **Then** tổng giỏ cập nhật ngay và không tải lại trang.
- **Given** giỏ đang có món từ tiệm A, **When** khách thêm món từ tiệm B, **Then** hiện sheet "Giỏ đang có món từ {tiệm}. Xóa giỏ và thêm món mới?"; chỉ xóa sau khi xác nhận.
- **Given** mặt hàng hết hàng, **When** khách bấm "+", **Then** nút bị vô hiệu và không có món nào được thêm.
- **Given** khách ẩn danh, **When** thêm món, **Then** giỏ lưu trong session và tồn tại khi chuyển trang.

### Story 3.2: Giỏ hàng và chọn giao hàng hoặc nhận tại tiệm

As a khách hàng,
I want kiểm tra giỏ và chọn cách nhận hàng,
So that tôi biết tổng tiền và có nhận được hàng hay không trước khi đặt.

**Acceptance Criteria:**

- **Given** giỏ có món, **When** mở `/cart`, **Then** thấy danh sách, đơn giá, stepper, nút xóa, tạm tính, phí giao và tổng.
- **Given** khách chọn "Giao hàng", **When** khoảng cách vượt bán kính, **Then** lựa chọn bị vô hiệu kèm lý do và gợi ý nhận tại tiệm/đổi tiệm.
- **Given** khách chọn "Nhận tại tiệm", **When** tiệm bật hình thức này, **Then** hiện các khung 30 phút trong giờ mở cửa; khung gần nhất còn nhận được chọn sẵn.
- **Given** tổng giỏ dưới giá trị đơn tối thiểu, **When** khách xem CTA, **Then** CTA bị vô hiệu và hiện "Tiệm nhận đơn từ {số tiền}".
- **Given** giá món đổi từ lúc thêm, **When** khách vào giỏ, **Then** hiện banner giá đã cập nhật và tổng mới; không tự đặt.

### Story 3.3: Checkout và đăng nhập

As a khách hàng,
I want xác nhận đơn và đăng nhập ở bước cuối,
So that tôi không phải tạo tài khoản trước khi biết mình muốn mua gì.

**Acceptance Criteria:**

- **Given** khách ẩn danh bấm "Đặt hàng", **When** bị chuyển tới Auth0, **Then** sau đăng nhập quay lại `/checkout` với giỏ còn nguyên.
- **Given** khách đã đăng nhập, **When** mở checkout, **Then** thấy danh sách món, số lượng, đơn giá, phí giao, tổng, hình thức nhận và "Thanh toán tiền mặt".
- **Given** khách chọn giao hàng, **When** nhập thông tin, **Then** chọn phường, nhập số nhà/đường và số điện thoại; địa chỉ mặc định theo vị trí nhưng sửa được.
- **Given** khách bấm "Đặt hàng" hai lần liên tiếp, **When** request thứ hai tới, **Then** chỉ một đơn được tạo và cả hai trả cùng mã đơn.
- **Given** form thiếu số điện thoại hoặc địa chỉ, **When** bấm đặt, **Then** lỗi hiện cạnh trường, dữ liệu giữ nguyên và không tạo đơn.

### Story 3.4: Tạo đơn và thanh toán tiền mặt

As a khách hàng,
I want đặt đơn và nhận xác nhận ngay,
So that tôi biết tiệm đã nhận đơn và sẽ giao hoặc chuẩn bị hàng.

**Acceptance Criteria:**

- **Given** checkout hợp lệ, **When** bấm "Đặt hàng", **Then** hệ thống tạo order, order_items và order_event trong một transaction, trạng thái `CHO_XAC_NHAN`.
- **Given** đơn được tạo, **When** xem xác nhận, **Then** thấy mã đơn, tổng tiền, hình thức nhận, "Thanh toán tiền mặt khi nhận/giao" và dòng thời gian bắt đầu.
- **Given** giá hoặc phí giao đổi giữa lúc mở checkout và bấm đặt, **When** tạo đơn, **Then** hệ thống dùng giá hiện tại, lưu snapshot và hiển thị tổng đã chốt.
- **Given** tiệm tạm đóng giữa lúc checkout, **When** bấm đặt, **Then** trả lỗi nghiệp vụ, giữ giỏ và không tạo đơn.
- **Given** đơn đã tạo, **When** cửa hàng mở tab đơn, **Then** đơn mới xuất hiện trong vòng 60 giây.

### Story 3.5: Hủy đơn hàng

As a khách hàng hoặc chủ tiệm,
I want hủy đơn trong giới hạn trạng thái,
So that đơn sai hoặc hết hàng không bị treo.

**Acceptance Criteria:**

- **Given** đơn ở `CHO_XAC_NHAN` hoặc `DA_XAC_NHAN` và chưa `DANG_SOAN`, **When** khách bấm hủy, **Then** đơn chuyển `DA_HUY`, tồn kho được hoàn và hai bên nhận thông báo.
- **Given** đơn đã `DANG_SOAN`, **When** khách bấm hủy, **Then** nút hủy không khả dụng và API trả lỗi.
- **Given** chủ tiệm hủy đơn trước `HOAN_TAT`, **When** xác nhận, **Then** bắt buộc chọn lý do, đơn chuyển `DA_HUY`, tồn kho hoàn và khách nhận lý do.
- **Given** đơn đã `HOAN_TAT`, **When** bất kỳ bên hủy, **Then** hệ thống từ chối.
- **Given** hai bên hủy cùng lúc, **When** request thứ hai tới, **Then** chỉ một chuyển trạng thái thành công và không hoàn tồn kho hai lần.

### Story 3.6: Lịch sử và đặt lại đơn

As a khách hàng,
I want xem đơn cũ và đặt lại,
So that tôi mua lại giỏ quen mà không phải chọn từng món.

**Acceptance Criteria:**

- **Given** khách đã có đơn, **When** mở `/orders`, **Then** thấy danh sách mới nhất trước, phân trang 20 đơn, mỗi dòng có mã, trạng thái, thời gian và tổng tiền.
- **Given** khách mở đơn cũ, **When** bấm "Đặt lại", **Then** hệ thống dựng giỏ từ đơn, bỏ món ngừng bán/hết hàng và hiển thị món bị bỏ.
- **Given** giá hiện tại khác giá cũ, **When** dựng giỏ, **Then** hiển thị giá mới và cảnh báo.
- **Given** giỏ đặt lại, **When** khách chưa xác nhận, **Then** chưa có đơn mới nào được tạo.
- **Given** đơn cũ đã hoàn tất, **When** xem chi tiết, **Then** nút "Đặt lại" hiển thị; đơn đã hủy cũng có thể đặt lại.

## Epic 4: Xử lý đơn tại tiệm

Chủ tiệm thấy hàng đợi đơn, xác nhận hoặc từ chối, soạn hàng bằng quét mã, xử lý thiếu hàng, chuyển trạng thái và cập nhật tồn kho đúng.

### Story 4.1: Hàng đợi đơn của tiệm

As a chủ tiệm,
I want thấy đơn mới và đơn đang xử lý trong một màn hình,
So that tôi không bỏ sót đơn trong lúc đang bán hàng.

**Acceptance Criteria:**

- **Given** chủ tiệm đăng nhập, **When** mở tab Đơn, **Then** thấy số đếm đơn `CHO_XAC_NHAN`, danh sách đơn mới/đang xử lý/hoàn tất.
- **Given** có đơn mới, **When** dashboard làm mới, **Then** đơn xuất hiện trong vòng 30 giây hoặc khi bấm làm mới.
- **Given** chủ tiệm lọc theo trạng thái hoặc ngày, **When** áp dụng, **Then** danh sách chỉ hiện đơn khớp.
- **Given** đơn chờ quá 15 phút, **When** hiển thị, **Then** có cảnh báo và được đưa lên đầu danh sách.
- **Given** chủ tiệm A, **When** mở tab Đơn, **Then** chỉ thấy đơn của cửa hàng mình.

### Story 4.2: Xác nhận hoặc từ chối đơn

As a chủ tiệm,
I want xác nhận hoặc từ chối đơn nhanh,
So that khách biết đơn có được chuẩn bị hay không.

**Acceptance Criteria:**

- **Given** đơn `CHO_XAC_NHAN`, **When** chủ tiệm bấm "Xác nhận", **Then** đơn chuyển `DA_XAC_NHAN`, tồn kho được giữ và khách nhận thông báo.
- **Given** đơn `CHO_XAC_NHAN`, **When** chủ tiệm bấm "Từ chối", **Then** bắt buộc chọn lý do; đơn chuyển `DA_HUY` và khách nhận lý do.
- **Given** tồn kho không đủ, **When** chủ tiệm xác nhận, **Then** hệ thống hiện cảnh báo nhưng vẫn cho xác nhận.
- **Given** đơn không ở `CHO_XAC_NHAN`, **When** gọi xác nhận/từ chối, **Then** API trả lỗi và không đổi trạng thái.
- **Given** hai thiết bị cùng xác nhận, **When** request thứ hai tới, **Then** optimistic lock chặn và trả trạng thái hiện tại.

### Story 4.3: Máy trạng thái và nhật ký đơn

As a chủ tiệm,
I want hệ thống chỉ cho chuyển đơn theo đúng thứ tự và ghi lại lịch sử,
So that trạng thái đơn luôn đáng tin khi có sự cố.

**Acceptance Criteria:**

- **Given** đơn `CHO_XAC_NHAN`, **When** chuyển thẳng sang `HOAN_TAT`, **Then** API trả lỗi và trạng thái không đổi.
- **Given** một chuyển trạng thái hợp lệ, **When** lưu, **Then** `order_events` có bản ghi người thực hiện, thời điểm, trạng thái cũ/mới.
- **Given** hai request chuyển trạng thái đồng thời, **When** một request thắng, **Then** request còn lại nhận lỗi optimistic lock và không ghi event sai.
- **Given** đơn tại quầy, **When** hoàn tất, **Then** đơn tạo trực tiếp `HOAN_TAT` và có event tương ứng.
- **Given** khách mở đơn, **When** xem dòng thời gian, **Then** các event hiển thị đúng thứ tự và nhãn tiếng Việt.

### Story 4.4: Soạn hàng bằng quét mã

As a chủ tiệm,
I want quét từng món để đối chiếu đơn,
So that tôi soạn đúng hàng mà không phải đọc lại tin nhắn.

**Acceptance Criteria:**

- **Given** đơn đã xác nhận, **When** mở "Soạn hàng", **Then** thấy từng dòng với cần soạn/đã soạn và nút quét.
- **Given** chủ tiệm quét mã thuộc đơn, **When** mã đúng, **Then** số lượng đã soạn tăng 1 và dòng đủ chuyển sang trạng thái thành công.
- **Given** quét mã không thuộc đơn, **When** nhận mã, **Then** hiện cảnh báo và không thay đổi số lượng.
- **Given** quét vượt số lượng cần, **When** nhận mã, **Then** hiện cảnh báo nhưng không tự tăng.
- **Given** tất cả dòng đủ, **When** chủ tiệm hoàn tất soạn, **Then** đơn có thể chuyển sang `SAN_SANG` hoặc `DANG_GIAO`.

### Story 4.5: Xử lý thiếu hàng

As a chủ tiệm,
I want đánh dấu món thiếu và nhập số lượng thực tế,
So that khách biết đơn được điều chỉnh trước khi nhận.

**Acceptance Criteria:**

- **Given** một dòng đơn thiếu hàng, **When** chủ tiệm bấm "Thiếu hàng" và nhập số lượng thực tế, **Then** hệ thống tính lại tổng và hiện chênh lệch tiền mặt.
- **Given** điều chỉnh đã lưu, **When** khách mở đơn, **Then** thấy số lượng mới và có thể chấp nhận hoặc hủy.
- **Given** khách chấp nhận, **When** đơn tiếp tục, **Then** trạng thái chuyển sang sẵn sàng nhận/đang giao với tổng mới.
- **Given** khách hủy sau điều chỉnh, **When** hủy, **Then** tồn kho được hoàn và đơn `DA_HUY`.
- **Given** số lượng giảm, **When** lưu, **Then** phần chênh lệch được hoàn vào tồn kho và ghi stock event.

### Story 4.6: Giữ, hoàn và trừ tồn kho

As a chủ tiệm,
I want tồn kho thay đổi đúng theo đơn,
So that tôi không bán món đã hết hoặc bán trùng.

**Acceptance Criteria:**

- **Given** đơn được xác nhận, **When** lưu, **Then** `StockService` giữ tồn bằng một `UPDATE` có điều kiện và ghi stock event.
- **Given** đơn bị hủy, **When** hủy, **Then** tồn kho được hoàn đúng số lượng đã giữ và ghi event.
- **Given** POS hoàn tất, **When** bán, **Then** tồn kho trừ đúng số lượng từng dòng và ghi event.
- **Given** tồn kho không đủ, **When** trừ, **Then** không cho tồn âm; cập nhật 0 dòng và trả cảnh báo.
- **Given** hai đơn cùng xác nhận một món, **When** chạy đồng thời, **Then** không có tồn âm và một đơn nhận cảnh báo thiếu.

## Epic 5: Bán tại quầy bằng quét mã

Chủ tiệm bán trực tiếp cho khách vãng lai: mở phiên, quét món, tạo nhanh món chưa có, tính tiền mặt và hiển thị biên nhận; tồn kho được trừ.

### Story 5.1: Mở phiên bán tại quầy

As a chủ tiệm,
I want mở một phiên bán tại quầy,
So that tôi bắt đầu tính tiền cho khách vãng lai mà không cần đơn online.

**Acceptance Criteria:**

- **Given** chủ tiệm ở tab Bán, **When** chưa có phiên, **Then** thấy màn hình trống với nút "Bắt đầu bán".
- **Given** chủ tiệm bấm bắt đầu, **When** phiên được tạo, **Then** phiên lưu server và không mất khi tải lại trang.
- **Given** đã có phiên `OPEN` trên thiết bị khác, **When** mở phiên mới, **Then** hiện cảnh báo và cho hủy hoặc tiếp tục đóng phiên cũ.
- **Given** chủ tiệm hủy phiên, **When** xác nhận, **Then** không có đơn tại quầy nào được tạo.
- **Given** chủ tiệm rời màn hình, **When** quay lại, **Then** phiên cũ vẫn còn.

### Story 5.2: Quét và thêm món vào hóa đơn

As a chủ tiệm,
I want quét hoặc tìm món để thêm vào hóa đơn,
So that tôi tính tiền nhanh hơn gõ giá.

**Acceptance Criteria:**

- **Given** phiên đang mở, **When** quét mã có trong danh mục, **Then** dòng món xuất hiện với số lượng 1 và tổng tiền cập nhật.
- **Given** quét lại cùng mã, **When** nhận mã, **Then** số lượng dòng tăng 1.
- **Given** camera lỗi, **When** chủ tiệm tìm theo tên, **Then** chọn được món từ tối đa 10 kết quả để thêm.
- **Given** chủ tiệm sửa số lượng hoặc xóa dòng, **When** thay đổi, **Then** tổng tiền cập nhật ngay.
- **Given** món hết hàng, **When** thêm, **Then** hiện cảnh báo nhưng vẫn cho bán vì tồn kho là best-effort.

### Story 5.3: Tạo nhanh mặt hàng khi mã chưa có

As a chủ tiệm,
I want tạo nhanh món mới ngay trong lúc bán,
So that tôi không phải bỏ qua món khách muốn mua.

**Acceptance Criteria:**

- **Given** quét mã chưa có trong danh mục, **When** hệ thống nhận mã, **Then** mở hộp thoại tạo nhanh với mã đã điền.
- **Given** hộp thoại mở, **When** chủ tiệm nhập tên và giá, **Then** có thể lưu với đơn vị mặc định "cái", tồn kho 0.
- **Given** lưu thành công, **When** quay lại phiên, **Then** món có trong danh mục và trong hóa đơn hiện tại.
- **Given** chủ tiệm bấm "Bỏ qua", **When** đóng hộp thoại, **Then** không tạo mặt hàng và không thêm vào hóa đơn.
- **Given** tên hoặc giá không hợp lệ, **When** lưu, **Then** lỗi hiện trong hộp thoại và dữ liệu không mất.

### Story 5.4: Hoàn tất bán và biên nhận

As a chủ tiệm,
I want xác nhận thanh toán và xem biên nhận,
So that tôi kết thúc giao dịch và có bản ghi để đối chiếu.

**Acceptance Criteria:**

- **Given** hóa đơn có món, **When** chủ tiệm bấm "Tính tiền", **Then** nhập được tiền khách đưa và thấy tiền thừa.
- **Given** xác nhận hoàn tất, **When** lưu, **Then** đơn tại quầy tạo `HOAN_TAT`, tồn kho trừ và có order event.
- **Given** biên nhận hiển thị, **When** xem, **Then** có mã đơn, thời gian, danh sách món, tổng, tiền khách đưa/tiền thừa.
- **Given** hóa đơn trống, **When** bấm hoàn tất, **Then** nút bị vô hiệu và không tạo đơn.
- **Given** bấm "Hoàn tất" hai lần, **When** request thứ hai tới, **Then** chỉ một đơn tại quầy được tạo.

## Epic 6: Theo dõi đơn và thông báo

Khách hàng và chủ tiệm luôn biết đơn đang ở đâu qua trạng thái, dòng thời gian, thông báo trong ứng dụng và email; đơn nhận tại tiệm quá hạn được xử lý tự động.

### Story 6.1: Trạng thái đơn cho khách hàng

As a khách hàng,
I want xem trạng thái và dòng thời gian đơn,
So that tôi biết khi nào cần chuẩn bị tiền mặt hoặc tới lấy hàng.

**Acceptance Criteria:**

- **Given** khách có đơn, **When** mở `/orders/{id}`, **Then** thấy mã đơn, trạng thái hiện tại, dòng thời gian, món, tổng và hình thức nhận.
- **Given** trạng thái đổi, **When** trang tự làm mới sau 30 giây, **Then** trạng thái mới hiển thị mà không cần tải lại.
- **Given** đơn đã `HOAN_TAT`, **When** xem chi tiết, **Then** có nút "Đặt lại".
- **Given** khách A, **When** mở đơn của khách B, **Then** nhận 404.
- **Given** trạng thái hiển thị, **When** xem nhãn, **Then** dùng đúng bộ từ: Chờ tiệm xác nhận, Tiệm đã xác nhận, Đang soạn, Sẵn sàng nhận, Đang giao, Hoàn tất, Đã hủy, Không nhận.

### Story 6.2: Thông báo trong ứng dụng và email

As a khách hàng hoặc chủ tiệm,
I want nhận thông báo khi đơn đổi trạng thái,
So that tôi không phải kiểm tra thủ công.

**Acceptance Criteria:**

- **Given** đơn mới được tạo, **When** transaction commit, **Then** chủ tiệm nhận thông báo trong ứng dụng và email sau commit, không phải trong transaction.
- **Given** đơn được xác nhận/hủy/hoàn tất, **When** trạng thái đổi, **Then** khách nhận email tương ứng.
- **Given** gửi email lỗi, **When** SMTP trả lỗi, **Then** hệ thống ghi log, không rollback đơn và không chặn giao dịch.
- **Given** người dùng mở ứng dụng, **When** có thông báo chưa đọc, **Then** badge hiển thị và danh sách thông báo có nội dung tiếng Việt.
- **Given** kênh thông báo, **When** v1 chạy, **Then** không gửi SMS/Zalo.

### Story 6.3: Xử lý đơn nhận tại tiệm quá hạn

As a chủ tiệm,
I want đơn quá giờ hẹn được đánh dấu không nhận,
So that hàng đã soạn không bị treo và tồn kho được hoàn.

**Acceptance Criteria:**

- **Given** đơn `SAN_SANG` có `pickup_hold_until` đã qua, **When** job chạy mỗi 5 phút, **Then** đơn chuyển `KHONG_NHAN`, tồn kho hoàn và khách nhận thông báo.
- **Given** hai instance cùng chạy job, **When** job chạy, **Then** `pg_try_advisory_lock` đảm bảo chỉ một instance xử lý.
- **Given** đơn đã `KHONG_NHAN`, **When** job chạy lại, **Then** không xử lý lại và không hoàn tồn kho lần hai.
- **Given** còn 30 phút trước hết hạn, **When** job nhắc, **Then** khách nhận email nhắc nhở.
- **Given** đơn giao hàng, **When** quá hạn, **Then** không áp dụng `KHONG_NHAN`.

## Epic 7: Pilot an toàn, nhanh và đáng tin

Đưa các NFR và ràng buộc kiến trúc vào sản phẩm: hiệu năng, bảo mật, quyền riêng tư, accessibility, quan sát, migration, backup, test harness và triển khai pilot.

### Story 7.1: Test harness và CI gate

As a thành viên đội phát triển,
I want một bộ test tích hợp với PostgreSQL thật và cổng `mvn verify`,
So that thay đổi không phá state machine, tồn kho hoặc quyền truy cập.

**Acceptance Criteria:**

- **Given** test suite, **When** chạy `mvn verify`, **Then** dùng JUnit 5, MockMvc và Testcontainers PostgreSQL 18, không có H2.
- **Given** state machine, **When** chạy bảng test chuyển trạng thái, **Then** mọi cặp hợp lệ/không hợp lệ đều được kiểm.
- **Given** tồn kho, **When** chạy test đồng thời, **Then** không có tồn âm và số event khớp số thay đổi.
- **Given** adapter barcode/geo, **When** chạy contract test, **Then** timeout, cache và khoảng cách đúng.
- **Given** test fail, **When** CI chạy, **Then** build fail và không cho merge.

### Story 7.2: Hiệu năng và index

As a khách hàng,
I want danh sách tiệm và trang cửa hàng tải nhanh,
So that tôi không bỏ đi vì chờ đợi.

**Acceptance Criteria:**

- **Given** 500 cửa hàng seed, **When** đo tìm gần, **Then** p95 ≤ 800 ms và dùng bounding box + `distance_km`.
- **Given** trang cửa hàng, **When** đo trên 4G, **Then** p95 ≤ 1,5 s.
- **Given** lưu mặt hàng, **When** đo, **Then** ≤ 1 s; checkout p95 ≤ 2 s.
- **Given** danh sách, **When** kiểm tra query, **Then** có index cho `stores(lat,lng)`, `products(store_id, deleted_at)`, `orders(store_id, status, created_at)`, `orders(customer_id, created_at)`.
- **Given** danh sách dài, **When** tải, **Then** phân trang và không có N+1.

### Story 7.3: Bảo mật và quyền riêng tư

As a người dùng pilot,
I want dữ liệu và vị trí của mình được bảo vệ,
So that tôi tin tưởng dùng nền tảng cho đơn hàng thật.

**Acceptance Criteria:**

- **Given** mọi request thay đổi trạng thái, **When** thiếu CSRF token, **Then** trả 403.
- **Given** cookie phiên, **When** kiểm tra, **Then** có `HttpOnly`, `Secure`, `SameSite=Lax`.
- **Given** người dùng mở object không thuộc sở hữu, **When** truy cập, **Then** trả 404 và không lộ dữ liệu.
- **Given** upload ảnh, **When** tệp sai magic bytes hoặc > 2 MB, **Then** bị từ chối.
- **Given** log, **When** kiểm tra, **Then** không có token, email đầy đủ, số điện thoại hoặc toạ độ; log vị trí giữ ≤ 30 ngày, đơn giữ 24 tháng.
- **Given** login/lookup/tạo đơn, **When** vượt ngưỡng, **Then** bị giới hạn tần suất.

### Story 7.4: Accessibility và responsive polish

As a người dùng dùng điện thoại một tay,
I want giao diện dễ đọc, dễ chạm và không bị vỡ khi phóng to,
So that tôi dùng được ngoài trời và khi mắt yếu.

**Acceptance Criteria:**

- **Given** luồng khách hàng, **When** kiểm tra WCAG 2.1 AA, **Then** không có lỗi tương phản, nhãn form, focus hoặc thứ tự tab.
- **Given** mọi nút tương tác, **When** đo, **Then** vùng chạm ≥ 44×44 px và cách nhau ≥ 8 px.
- **Given** quét thành công hoặc lỗi form, **When** trình đọc màn hình chạy, **Then** thông báo qua `aria-live` và không đọc lặp.
- **Given** zoom 200% và màn hình 360 px, **When** xem bottom nav, **Then** nhãn không bị ẩn/cắt.
- **Given** `prefers-reduced-motion`, **When** bật, **Then** bỏ hiệu ứng nháy nhưng vẫn có phản hồi màu/nội dung.
- **Given** dark mode, **When** bật trong tài khoản, **Then** token `*-dark` áp dụng và status chip vẫn đạt tương phản.

### Story 7.5: Observability và hợp đồng lỗi

As a đội vận hành,
I want log, metric và lỗi nhất quán,
So that tôi tìm được nguyên nhân khi đơn hoặc quét mã thất bại.

**Acceptance Criteria:**

- **Given** một request, **When** xem log, **Then** có `requestId`, `userId`, `orderCode` (nếu có) và không có PII.
- **Given** lỗi JSON, **When** trả về, **Then** dùng `ProblemDetail` kèm `code` và HTTP status đúng.
- **Given** lỗi trình duyệt, **When** xảy ra, **Then** render template `404.html`, `403.html`, `500.html` tiếng Việt.
- **Given** metrics, **When** xem dashboard, **Then** có tỷ lệ quét thành công, tỷ lệ tra cứu trúng, thời gian chuyển trạng thái và lỗi email.
- **Given** health check, **When** ứng dụng hoặc DB lỗi, **Then** `/actuator/health` phản ánh trạng thái và chỉ mở ở cổng nội bộ.

### Story 7.6: Migration, backup, môi trường và deploy pilot

As a đội vận hành,
I want triển khai một container với PostgreSQL managed và backup,
So that pilot chạy được, khôi phục được và không mất dữ liệu.

**Acceptance Criteria:**

- **Given** schema thay đổi, **When** deploy, **Then** Flyway chạy migration, `ddl-auto=validate` không lỗi và migration đã phát hành không bị sửa.
- **Given** profile `local`, `test`, `pilot`, **When** khởi động, **Then** secret đọc từ env, không có secret trong repo.
- **Given** pilot deploy, **When** kiểm tra, **Then** HTTPS bật, health check pass và ứng dụng chạy một container.
- **Given** PostgreSQL managed, **When** kiểm tra, **Then** backup hằng ngày và giữ 14 ngày.
- **Given** deploy lỗi, **When** rollback, **Then** có migration bù hoặc image trước đó và không mất dữ liệu.
