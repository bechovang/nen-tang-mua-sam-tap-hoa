---
name: Tạp Hóa Gần
status: final
sources:
  - ../../prds/prd-nen-tang-mua-sam-tap-hoa-2026-11-03/prd.md
  - ../../briefs/brief-nen-tang-mua-sam-tap-hoa-2026-11-03/brief.md
updated: 2026-11-03
---

# Tạp Hóa Gần — Experience Spine

> Hành vi, luồng và trạng thái. Nhận diện thị giác nằm ở `DESIGN.md`; tài liệu này tham chiếu token bằng `{path.to.token}`. Khi mock, wireframe hoặc bản nháp lệch với hai spine, spine thắng.

## Foundation

Responsive web, **phone-first**, dựng bằng Thymeleaf + Bootstrap 5.3. Không có app native, không có PWA cài đặt, không có chế độ offline. Camera trình duyệt là cách nhập mã vạch chính; nhập tay là phương án dự phòng bắt buộc.

Một tài khoản có thể ở hai chế độ:

- **Chế độ mua** — khách hàng xem tiệm, đặt hàng, theo dõi đơn.
- **Chế độ bán** — chủ tiệm quản lý cửa hàng, danh mục, đơn và POS.

Người dùng thường chỉ thấy một chế độ. Người vừa mở tiệm vừa mua hàng thấy nút chuyển chế độ ở header; chuyển chế độ không đăng xuất và không mất giỏ hàng.

Ngôn ngữ giao diện: tiếng Việt. Light mode mặc định; dark mode là tùy chọn trong tài khoản. Tiền tệ VND, múi giờ `Asia/Ho_Chi_Minh`.

## Information Architecture

### Bề mặt

| Bề mặt | Đường dẫn | Vào từ | Mục đích | Vai trò |
|---|---|---|---|---|
| Nhà | `/` | Mở app, tab Nhà | Chọn vị trí, xem tiệm gần, vào nhanh đơn đang xử lý | Khách |
| Tìm | `/search` | Tab Tìm | Tìm tiệm hoặc món trong khu vực | Khách |
| Cửa hàng | `/stores/{id}` | Thẻ tiệm, kết quả tìm | Xem danh mục, thêm vào giỏ | Khách |
| Giỏ | `/cart` | Tab Giỏ, nút thêm món | Sửa số lượng, chọn giao/nhận | Khách |
| Đặt hàng | `/checkout` | Giỏ | Địa chỉ/khung giờ, xác nhận, đặt | Khách |
| Đơn của tôi | `/orders/{id}` | Sau khi đặt, tab Đơn | Trạng thái, dòng thời gian, đặt lại | Khách |
| Đơn hàng | `/orders` | Tab Đơn | Lịch sử, đặt lại | Khách |
| Mở tiệm | `/store/onboarding` | Lời mời, nút "Mở tiệm" | Hồ sơ → quét → đăng bán | Chủ tiệm |
| Tiệm | `/store` | Tab Tiệm | Hồ sơ, trạng thái mở/đóng, tóm tắt | Chủ tiệm |
| Danh mục | `/store/catalog` | Tab Danh mục | Xem, tìm, sửa, bật/tắt còn hàng | Chủ tiệm |
| Quét | `/store/scan` | Tab Quét, nút quét | Phiên quét tạo/cập nhật mặt hàng | Chủ tiệm |
| Đơn của tiệm | `/store/orders` | Tab Đơn | Hàng đợi đơn, lọc trạng thái | Chủ tiệm |
| Chi tiết đơn | `/store/orders/{id}` | Đơn của tiệm | Xác nhận, soạn, xử lý thiếu, chuyển trạng thái | Chủ tiệm |
| Bán tại quầy | `/pos` | Tab POS | Quét, tính tiền, biên nhận | Chủ tiệm |
| Đăng nhập | `/oauth2/authorization/auth0` | Nút đăng nhập | Google qua Auth0 | Cả hai |

`[GIẢ ĐỊNH: Không có trang chi tiết món riêng trong v1; thẻ món trong trang cửa hàng là nơi thêm vào giỏ.]` Lý do: giảm số bước, phù hợp giỏ nhỏ mua nhanh.

### Điều hướng

**Chế độ mua — bottom nav 4 mục:** Nhà · Tìm · Giỏ · Đơn. Header gồm vị trí hiện tại (bấm để đổi), nút chuyển chế độ nếu có cửa hàng, và menu tài khoản (đăng xuất, dark mode).

**Chế độ bán — bottom nav 5 mục:** Đơn · Danh mục · **Quét** (giữa, nút nổi) · Bán · Tiệm. Nhãn ngắn để vừa màn hình 360px; không ẩn mục nào.

**Desktop:** khách hàng giữ cột tối đa 720px, không kéo full chiều ngang. Khu vực tiệm dùng sidebar trái 240px; POS dùng hai cột.

Modal và bottom sheet chỉ mở một tầng. Không mở sheet trên sheet, không mở dialog trên dialog.

`[GIẢ ĐỊNH: Không có bản đồ trong v1; khoảng cách và danh sách tiệm đủ để chọn.]` Bản đồ là ứng viên v2 sau khi đo nhu cầu.

### Đóng bề mặt

| Nhu cầu từ PRD | Bề mặt đáp ứng | Luồng |
|---|---|---|
| Xem tiệm gần, biết còn hàng | Nhà, Tìm | Flow 2, Flow 3 |
| Xem danh mục và giá | Cửa hàng | Flow 2, Flow 3 |
| Chọn giao hoặc nhận, đặt hàng | Giỏ, Đặt hàng | Flow 2, Flow 3 |
| Theo dõi và đặt lại đơn | Đơn của tôi, Đơn hàng | Flow 2, Flow 3, Flow 6 |
| Mở tiệm và đưa danh mục lên | Mở tiệm, Quét | Flow 1 |
| Quản lý danh mục | Danh mục, Quét | Flow 1 |
| Xác nhận và soạn đơn | Đơn của tiệm, Chi tiết đơn | Flow 5 |
| Bán tại quầy | Bán tại quầy | Flow 4 |
| Bật/tắt nhận đơn, sửa hồ sơ | Tiệm | Flow 1, Flow 5 |

Không có nhu cầu nào trong PRD chưa có bề mặt. Không có bề mặt nào không phục vụ một nhu cầu.

## Voice and Tone

Giọng của một người đứng sau quầy, nói với khách quen: rõ, lịch sự, không khách sáo, không lên giọng dạy. Xưng "bạn" với cả khách và chủ tiệm. Gọi cửa hàng là "tiệm". Trong microcopy khách hàng, "món" là cách nói thân mật của "mặt hàng"; trong màn hình quản trị dùng "mặt hàng".

| Do | Don't |
|---|---|
| "Tiệm đang mở" | "OPEN" / "Đang hoạt động" |
| "Còn 12 món" | "12 items available" |
| "Cách bạn 800 m" | "0.8km away" |
| "Đã gửi tới tiệm" | "Đặt hàng thành công!" |
| "Tiệm đã xác nhận" | "Order confirmed ✓" |
| "Không tìm thấy mã này. Nhập tên giúp tiệm nhé." | "Lỗi: barcode not found" |
| "Camera chưa bật. Bật quyền hoặc nhập mã tay." | "Permission denied" |
| "Mất mạng — món đã nhập vẫn được giữ." | "Network error" |
| "Món này đã hết. Chọn món khác hoặc quay lại sau." | "Out of stock" |
| "Đơn đã hủy. Tiệm báo hết hàng." | "Order cancelled by store" |
| Số tiền: "25.000 ₫" | "25,000 VND" |
| Thời gian: "18:30–19:00" | "6:30 PM" |

Không dùng emoji trong microcopy sản phẩm, không dùng dấu chấm than, không dùng "quý khách", không dùng từ tiếng Anh khi có từ Việt tương đương. Thuật ngữ kỹ thuật (mã vạch, POS) chỉ xuất hiện khi người dùng đã quen hoặc trong màn hình quản trị.

## Component Patterns

Hành vi; thị giác nằm ở `DESIGN.md.Components`.

| Component | Dùng ở | Quy tắc hành vi |
|---|---|---|
| Thẻ tiệm | Nhà, Tìm | Hiện tên, khoảng cách, mở/đóng, giao/nhận, số món còn hàng. Chạm vào thẻ mở trang tiệm. Không có nút "theo dõi" trong v1. |
| Thẻ món | Cửa hàng | Tên, đơn vị, giá, trạng thái còn/hết. Nút "+" thêm 1 vào giỏ ngay tại chỗ; không mở trang chi tiết. Hết hàng thì nút bị vô hiệu và có nhãn. |
| Stepper số lượng | Giỏ, POS | Nút trừ/cộng 44px; giữ nguyên vị trí khi số đổi. Không cho giảm dưới 1 ở giỏ; ở POS cho phép xóa dòng. |
| Dòng giỏ | Giỏ | Ảnh nhỏ (nếu có), tên, đơn vị, đơn giá, stepper. Vuốt để xóa không phải cách duy nhất; luôn có nút xóa. |
| Khung quét | Quét, POS | Mở camera sau khi người dùng chạm nút, không tự mở khi vào trang. Có đèn pin, "Nhập mã", và nút đóng. Sau khi nhận mã, hiện sheet xác nhận ở đáy. |
| Ô nhập mã | Quét, POS | Nhận 6–32 ký tự chữ/số, tự chuyển chữ hoa. Hiện lỗi ngay dưới ô. Không xóa dữ liệu đã nhập khi lỗi. |
| Checklist mở tiệm | Mở tiệm | Ba bước: hồ sơ, quét ít nhất 1 món, đăng bán. Bước đang làm được đánh dấu; bước xong có dấu kiểm. Có thể quay lại bước trước. |
| Thẻ đơn | Đơn hàng, Đơn của tiệm | Mã đơn, trạng thái, thời gian, tổng tiền, hình thức. Chạm mở chi tiết. Badge số đơn mới ở tab Đơn. |
| Chip trạng thái | Toàn bộ | Nền nhạt + chữ đậm + nhãn. Không chỉ dùng màu. Trạng thái đơn dùng đúng từ ở §State Patterns. |
| Dòng soạn hàng | Chi tiết đơn | Tên, cần soạn, đã soạn, nút quét. Quét đúng tăng "đã soạn"; quét sai hiện cảnh báo. |
| Dòng POS | Bán tại quầy | Tên, đơn giá, stepper, nút xóa. Tổng tiền dính đáy. |
| CTA dính đáy | Giỏ, Đặt hàng, POS | Một nút chính, luôn thấy khi cuộn. Không che nội dung cuối; cộng vùng an toàn. |
| Toast | Toàn bộ | Tối đa 2 dòng, 4 giây, có nút đóng. Không dùng toast cho lỗi cần sửa; lỗi nằm cạnh trường. |
| Chuyển chế độ | Header | Chỉ hiện khi người dùng có cửa hàng. Nhãn rõ: "Sang chế độ bán" / "Sang chế độ mua". Không tự chuyển. |
| Bottom nav | Chế độ mua/bán | 4 hoặc 5 mục; mục đang chọn có nhãn và màu; badge số đếm chỉ cho Giỏ và Đơn. |
| Ô tìm kiếm | Tìm | Tìm theo tên tiệm hoặc tên món; kết quả nhóm "Tiệm" và "Món". Không cần nhấn Enter. |
| Ô địa chỉ giao | Đặt hàng | Chọn phường trong khu vực pilot + nhập số nhà/đường; không geocode địa chỉ tự do. Mặc định theo vị trí hiện tại nhưng sửa được. |
| Chọn khung giờ | Đặt hàng | Các khung 30 phút trong giờ mở cửa; khung gần nhất còn nhận được chọn sẵn; không cho chọn khung đã qua. |
| Dòng phí và tổng | Giỏ, Đặt hàng, POS | Tạm tính, phí giao, tổng; tổng dùng `{typography.price}`; không làm tròn gây lệch. |
| Nhắc vị trí | Nhà | Chỉ hỏi khi người dùng chạm "Dùng vị trí của tôi" hoặc lần đầu vào Nhà. Có lựa chọn "Chọn phường". |
| Trạng thái trống | Toàn bộ | Một câu giải thích + một hành động. Không minh họa lớn, không emoji. |

## State Patterns

| Trạng thái | Bề mặt | Xử lý |
|---|---|---|
| Chưa có vị trí | Nhà | Hiện lời mời chọn vị trí; danh sách tiệm trống có hướng dẫn. Không tự xin quyền khi mở trang. |
| Từ chối quyền vị trí | Nhà | Hiện danh sách phường của khu vực pilot; ghi nhớ lựa chọn trong session. |
| Đang tải tiệm gần | Nhà | Skeleton 3 thẻ tiệm; không hiện spinner toàn trang. |
| Không có tiệm gần | Nhà | "Chưa có tiệm nào trong bán kính này. Thử chọn phường khác." Một nút đổi phường. |
| Tiệm đóng | Thẻ tiệm, trang tiệm | Nhãn "Đang đóng"; không cho thêm vào giỏ; vẫn xem được danh mục. |
| Tìm không có kết quả | Tìm | "Không tìm thấy tiệm hoặc món này." Gợi ý xóa bộ lọc hoặc đổi phường. |
| Món hết hàng | Trang tiệm | Nhãn "Hết hàng"; nút "+" vô hiệu; không đưa lên đầu danh sách. |
| Giá đổi khi vào giỏ | Giỏ, Đặt hàng | Banner: "Giá đã cập nhật từ lúc bạn thêm món." Hiện giá mới và tổng mới; không tự đặt. |
| Thêm món từ tiệm khác | Cửa hàng | Sheet hỏi: "Giỏ đang có món từ {tiệm}. Xóa giỏ và thêm món mới?" Chỉ xóa sau khi khách xác nhận. |
| Dưới giá trị đơn tối thiểu | Giỏ, Đặt hàng | Hiện "Tiệm nhận đơn từ {số tiền}". CTA bị vô hiệu kèm lý do; không tự thêm món. |
| POS đang có phiên khác | Bán tại quầy | Hiện "Phiên bán đang mở trên thiết bị khác. Tiếp tục sẽ đóng phiên cũ." Cho hủy hoặc tiếp tục. |
| Đơn chờ quá 15 phút | Đơn của tiệm | Đánh dấu cảnh báo, đưa lên đầu danh sách; không tự hủy. |
| Ngoài bán kính giao | Giỏ, Đặt hàng | Vô hiệu lựa chọn giao hàng; đề nghị nhận tại tiệm hoặc đổi tiệm. |
| Camera bị từ chối | Quét, POS | Hướng dẫn bật quyền + ô nhập mã tay. Không chặn toàn bộ màn hình. |
| Không tìm thấy mã | Quét | Sheet nhập tên/giá với mã đã điền; nút "Lưu món" và "Bỏ qua". |
| Quét trùng | Quét | Mở sheet cập nhật món hiện có; hiện giá/tồn cũ; nút "Cập nhật". |
| Mất mạng khi quét | Quét, POS | Giữ dữ liệu đã nhập; banner "Mất mạng — thử lại khi có kết nối." Không tự xóa phiên. |
| Đơn chờ xác nhận | Đơn của tôi | Chip "Chờ tiệm xác nhận"; hiện thời gian đã chờ; cho hủy trước khi tiệm xác nhận. |
| Tiệm từ chối đơn | Đơn của tôi | Chip "Đã hủy"; hiện lý do; gợi ý tiệm khác. |
| Thiếu hàng | Đơn của tôi | Banner "Tiệm báo thiếu 1 món"; hiện số lượng mới và chênh lệch tiền; khách chấp nhận hoặc hủy. |
| Quá hạn nhận | Đơn của tôi | Chip "Không nhận"; giải thích thời gian giữ; không cho đặt lại tự động. |
| Phiên hết hạn | Toàn bộ | Chuyển tới đăng nhập; sau đăng nhập quay lại đúng màn hình và giữ giỏ. |
| POS trống | Bán tại quầy | "Chưa có món nào. Quét món đầu tiên." Nút quét lớn. |
| Lỗi lưu | Toàn bộ biểu mẫu | Lỗi cạnh trường; giữ dữ liệu; nút "Thử lại". Không toast chung chung. |
| Email không gửi được | Không hiển thị | Ghi log; không báo người dùng; trạng thái đơn vẫn đúng trong ứng dụng. |

## Interaction Primitives

- **Chạm để làm.** Không dùng vuốt làm cách duy nhất cho hành động quan trọng.
- **Một tay.** Hành động chính nằm trong 1/3 dưới màn hình; nút quét và thanh toán nằm trong tầm ngón cái.
- **Quét là một nhịp.** Chạm "Quét" → camera mở → nhận mã → phản hồi rung/nháy → sheet xác nhận. Không rời màn hình giữa các bước.
- **Chống quét trùng:** bỏ qua cùng mã trong 1,5 giây; không hiện hai sheet liên tiếp.
- **Phản hồi:** rung nhẹ khi nhận mã nếu trình duyệt hỗ trợ Vibration API; iOS Safari không hỗ trợ, nên luôn kèm nháy xanh và thay đổi nội dung để không phụ thuộc rung. Có thể tắt trong Tiệm → Cài đặt. Không dùng âm thanh mặc định.
- **Kéo để làm mới** chỉ trên danh sách (Nhà, Đơn, Đơn của tiệm). Không kéo để làm mới trong POS.
- **Bottom sheet** một tầng; đóng bằng nút hoặc chạm nền. Không đóng bằng cách vuốt nếu đang nhập liệu.
- **Bàn phím desktop:** POS hỗ trợ nhập mã bằng bàn phím và Enter để thêm; `Esc` đóng sheet. Không cần phím tắt cho luồng khách.
- **Banned:** carousel, infinite scroll, hover-only, auto-play, double-tap để thích, modal lồng nhau, gamification, badge gây áp lực, pop-up xin đánh giá.

## Accessibility Floor

Hành vi; tương phản thị giác nằm ở `DESIGN.md.Colors`.

- WCAG 2.1 AA cho toàn bộ luồng khách hàng và các hành động chính của chủ tiệm.
- Vùng chạm tối thiểu 44×44px; khoảng cách giữa hai nút liền kề tối thiểu 8px.
- Mọi trạng thái có nhãn chữ, không chỉ màu. Chip trạng thái đọc được bởi trình đọc màn hình.
- Ảnh món có `alt` là tên món; ảnh trang trí có `alt=""`.
- Form có nhãn liên kết; lỗi được thông báo qua `aria-live` và đặt cạnh trường.
- Khi quét thành công, trình đọc màn hình đọc "Đã nhận mã {mã}. {tên món}." Không đọc mỗi lần quét thất bại.
- Camera có phương án thay thế: nhập mã tay và tìm theo tên. Không có hành động nào chỉ dùng camera.
- Tôn trọng `prefers-reduced-motion`: bỏ hiệu ứng nháy, giữ phản hồi rung và màu.
- Chữ phóng to 200% không làm mất nội dung hoặc cắt nút; bottom nav giữ nhãn, cho phép xuống dòng hoặc tăng chiều cao, không ẩn nhãn sau tooltip.
- Thứ tự tab theo thứ tự đọc; focus ring dùng `{colors.focus-ring}` và luôn hiển thị.
- Ngôn ngữ trang khai báo `lang="vi"`.

## Responsive & Platform

| Breakpoint | Khách hàng | Chủ tiệm |
|---|---|---|
| `< 480px` | Một cột; bottom nav 4 mục; CTA dính đáy; thẻ món một cột. | Bottom nav 5 mục; POS một cột; danh sách đơn một cột. |
| `480–767px` | Một cột rộng hơn; thẻ món có thể hai cột. | Danh mục hai cột; POS một cột. |
| `768–1023px` | Cột tối đa 720px; bottom nav giữ nguyên. | Sidebar thu gọn; dashboard hai cột; POS hai cột. |
| `≥ 1024px` | Cột tối đa 720px, căn giữa; không kéo full chiều ngang. | Sidebar 240px; nội dung tối đa 1200px; POS hai cột. |

- Camera yêu cầu HTTPS hoặc localhost. Trên iOS Safari, camera trong trình duyệt có thể tạm dừng khi chuyển app; khi quay lại, hiện nút "Tiếp tục quét" thay vì tự mở lại.
- Không hỗ trợ offline. Khi mất mạng, giữ dữ liệu đã nhập và hiện trạng thái thử lại.
- Xoay ngang: POS và trang đơn của tiệm hỗ trợ; luồng khách vẫn dọc.
- Không có dark mode tự động theo hệ thống trong v1; người dùng chọn trong tài khoản.

## Inspiration & Anti-patterns

- **Lấy từ phiếu chợ giấy:** biên nhận POS gọn, đọc từ trên xuống, tổng tiền rõ, không logo chiếm chỗ.
- **Lấy từ POS quét mã:** một tay cầm máy, một tay cầm hàng; phản hồi ngay sau mỗi lần quét; không bắt xác nhận từng món.
- **Lấy từ thói quen đặt hàng qua chat:** câu ngắn, xác nhận rõ, không bắt khách hiểu thuật ngữ.
- **Lấy từ các app giao hàng địa phương:** thẻ tiệm cho biết khoảng cách, mở/đóng, giao/nhận ngay trên danh sách.
- **Loại bỏ — flash sale và đếm ngược:** không phù hợp với mua sắm hằng ngày và gây áp lực giả.
- **Loại bỏ — gamification, điểm thưởng, streak:** chủ tiệm không cần thêm việc; khách không cần lý do quay lại giả tạo.
- **Loại bỏ — infinite scroll và carousel:** danh sách có điểm dừng, dễ quay lại vị trí cũ.
- **Loại bỏ — bắt đăng nhập trước khi xem:** làm mất khách lần đầu; chỉ đăng nhập ở checkout.
- **Loại bỏ — đánh giá, xếp hạng sao trong v1:** chưa đủ dữ liệu và tạo động cơ sai; thay bằng trạng thái đơn và độ chính xác.
- **Loại bỏ — bản đồ làm bề mặt chính:** tốn pin, khó dùng một tay, và không cần để chọn tiệm trong bán kính ngắn.

## Key Flows

### Flow 1 — Chị Lan mở tiệm trong 15 phút

Chị Lan, 42 tuổi, chủ tiệm tạp hóa trong hẻm, nhận link pilot từ đội vận hành. Chị chưa có tài khoản.

1. Chị mở link, thấy màn hình giới thiệu ngắn và nút "Mở tiệm bằng Google".
2. Chị đăng nhập Google qua Auth0; hệ thống tạo tài khoản khách hàng.
3. Wizard mở ở bước 1: tên tiệm, địa chỉ, ghim vị trí, số điện thoại, bán kính, giao/nhận, giờ mở cửa.
4. Chị bật "Nhận tại tiệm", đặt bán kính 1 km, lưu.
5. Bước 2 mở màn hình quét với dòng chữ "Quét món đầu tiên trên kệ". Chị chạm "Bật camera".
6. Chị quét lần lượt 40 mã. 32 mã được điền tên; 8 mã chị tự nhập tên, giá, đơn vị. Sau mỗi lần lưu, app quay lại quét và hiện "Đã thêm: {tên}".
7. Chị bấm "Đăng bán".
8. **Cao điểm:** màn hình hiện "40 món đang bán" và bản xem trước trang tiệm đúng như khách sẽ thấy — tên, khoảng cách, danh mục, giá. Chị thấy tiệm mình đã sống.
9. Chị vào tab Đơn; dashboard trống với lời nhắc "Đơn mới sẽ xuất hiện ở đây".

**Thất bại:** mất mạng giữa phiên quét. App giữ các món đã lưu và món đang nhập; hiện banner "Mất mạng — thử lại khi có kết nối." Chị tiếp tục được khi mạng trở lại, không phải quét lại từ đầu.

### Flow 2 — Anh Minh đặt giao trong một giờ

Anh Minh, 31 tuổi, ở chung cư cách tiệm 800 m, vừa hết nước mắm và trứng.

1. Anh mở site trên Safari, chưa đăng nhập.
2. Nhà hiện lời mời chọn vị trí. Anh chạm "Dùng vị trí của tôi" và cho phép.
3. Danh sách hiện 4 tiệm trong 1,2 km, sắp theo khoảng cách. Thẻ đầu tiên ghi "Cách bạn 800 m · Đang mở · Giao hàng · 42 món".
4. Anh mở tiệm, thấy danh mục và giá. Anh thêm 6 món; nút "+" đổi thành stepper tại chỗ; badge Giỏ tăng lên 6.
5. Anh mở Giỏ, kiểm tra số lượng. Hệ thống chọn sẵn "Giao hàng" vì tiệm nằm trong bán kính. Anh thấy phí giao và tổng tiền.
6. Anh chạm "Đặt hàng"; app chuyển tới Google, anh đăng nhập.
7. Về lại Đặt hàng với giỏ còn nguyên. Anh nhập số điện thoại, chọn phường, nhập số nhà/đường (mặc định theo vị trí), ghi chú "gọi trước khi tới".
8. **Cao điểm:** sau khi bấm "Đặt hàng", màn hình xác nhận hiện mã đơn, tổng tiền, "Tiệm đã nhận đơn" và dòng thời gian bắt đầu chạy. Anh không phải gọi điện hay nhắn tin.
9. Anh đóng app; khi mở lại, tab Đơn hiện trạng thái mới nhất.

**Thất bại:** tiệm từ chối vì hết trứng. Đơn chuyển "Đã hủy", hiện lý do "Hết trứng", và gợi ý 2 tiệm gần còn món tương tự. Anh bấm vào tiệm gợi ý; app dựng lại giỏ từ đơn bị hủy, bỏ món không còn, để anh chỉ cần xác nhận lại.

### Flow 3 — Chị Hoa đặt trước và ghé lấy

Chị Hoa, 38 tuổi, tan làm lúc 18 giờ, muốn ghé tiệm trên đường về.

1. Chị mở app, đã đăng nhập từ lần trước. Nhà hiện "Gần đây" với tiệm quen.
2. Chị mở tiệm, thêm 4 món.
3. Ở Giỏ, chị chọn "Nhận tại tiệm". App hiện các khung 30 phút trong giờ mở cửa; khung 18:30–19:00 được chọn sẵn.
4. Chị chạm "Đặt hàng"; vì đã đăng nhập, app đi thẳng tới xác nhận. Chị kiểm tra tổng tiền và đặt.
5. **Cao điểm:** màn hình hiện mã nhận hàng lớn, khung giờ, địa chỉ tiệm và "Tiệm đã xác nhận". Chị chụp màn hình hoặc để đó.
6. Chị đến tiệm, đọc mã; chủ tiệm đối chiếu trên màn hình đơn.

**Thất bại:** chị đến muộn 40 phút. Đơn vẫn ở "Sẵn sàng nhận" tới 21:00 — 2 giờ sau khung hẹn 18:30–19:00; sau đó chuyển "Không nhận" và hoàn tồn kho. App ghi rõ chính sách ngay trên màn hình xác nhận.

### Flow 4 — Chị Lan bán tại quầy

Một khách không dùng app bước vào tiệm.

1. Chị Lan mở tab POS. Màn hình trống hiện "Quét món đầu tiên".
2. Chị chạm nút quét, camera mở. Chị quét 5 món; mỗi lần quét, dòng món xuất hiện và tổng tiền cập nhật.
3. Một mã không có trong danh mục. Sheet hiện "Mã chưa có trong tiệm" với tên và giá trống. Chị nhập "Nước mắm Nam Ngư 500ml", giá 35.000 ₫, lưu.
4. Chị kiểm tra hóa đơn, bấm "Tính tiền".
5. Chị nhập số tiền khách đưa; app hiện tiền thừa. Chị bấm "Hoàn tất".
6. **Cao điểm:** biên nhận hiện đủ 5 món, tổng tiền, tiền thừa, và "Đã bán". Tồn kho của các món giảm; món mới xuất hiện trong Danh mục.
7. Chị bấm "Bán đơn mới" để quay lại POS trống.

**Thất bại:** camera không đọc mã vì bao bì nhăn. Chị chạm "Nhập mã", gõ mã hoặc tìm theo tên; món vẫn được thêm, không phải bỏ qua.

### Flow 5 — Chị Lan soạn đơn online

1. Tab Đơn hiện badge "1". Chị mở đơn mới ở "Chờ xác nhận".
2. Chị xem danh sách, bấm "Xác nhận". Đơn chuyển "Đã xác nhận"; khách nhận thông báo.
3. Chị bấm "Soạn hàng". Màn hình hiện từng dòng: cần 3, đã soạn 0.
4. Chị quét từng món. Mỗi lần quét đúng, "đã soạn" tăng; dòng đủ chuyển màu thành công.
5. Tới nước mắm, chị chỉ còn 2 chai. Chị bấm "Thiếu hàng", nhập 2; app tính lại tổng và hiện "Khách sẽ trả ít hơn 25.000 ₫".
6. Chị xác nhận điều chỉnh. Khách nhận thông báo và chấp nhận.
7. Chị chọn "Đang giao" (hoặc "Sẵn sàng nhận" nếu khách lấy).
8. **Cao điểm:** màn hình đơn hiện "Đủ hàng · Đã báo khách · Đang giao", và tồn kho đã trừ đúng. Chị không phải mở sổ hay nhắn tin.
9. Khi giao xong, chị bấm "Hoàn tất" và đánh dấu đã thu tiền.

**Thất bại:** khách không đồng ý với thiếu hàng và hủy. App hoàn tồn kho, đơn chuyển "Đã hủy", cả hai bên nhận thông báo.

### Flow 6 — Anh Minh đặt lại đơn cũ

1. Anh mở tab Đơn, chọn đơn tuần trước.
2. Anh bấm "Đặt lại". App dựng giỏ từ đơn cũ.
3. App báo: "1 món đã ngừng bán, đã bỏ. 2 món đổi giá." Anh thấy giá mới ngay trong giỏ.
4. Anh chọn giao hàng, kiểm tra tổng, đặt.
5. **Cao điểm:** đơn mới được tạo với giá hiện tại, không phải giá cũ; anh thấy mã đơn mới và trạng thái "Chờ tiệm xác nhận".

**Thất bại:** tiệm đã đóng. App vô hiệu nút đặt, hiện "Tiệm đang đóng. Mở lại lúc 7:00." và giữ giỏ.

## Open Items

- `[GIẢ ĐỊNH: Không có trang chi tiết món; thẻ món là điểm thêm vào giỏ.]` — xác nhận sau usability test đầu tiên.
- `[GIẢ ĐỊNH: Không có đánh giá/xếp hạng sao trong v1.]` — tránh dữ liệu giả và động cơ sai.
- `[GIẢ ĐỊNH: Không có bản đồ trong v1.]` — danh sách + khoảng cách đủ cho bán kính ngắn.
- `[GIẢ ĐỊNH: POS dùng bàn phím trên desktop; trên điện thoại chỉ quét.]` — đo nhu cầu nếu tiệm dùng máy tính.
- `[GHI CHÚ CHO UX]` Cần kiểm thử nhãn "Sẵn sàng nhận" và "Đang giao" với 5 chủ tiệm thật; có thể họ dùng "Đã soạn xong".
- `[GHI CHÚ CHO UX]` Cần đo thời gian quét 40 món trên thiết bị tầm trung; nếu > 15 phút, giảm số bước xác nhận.
