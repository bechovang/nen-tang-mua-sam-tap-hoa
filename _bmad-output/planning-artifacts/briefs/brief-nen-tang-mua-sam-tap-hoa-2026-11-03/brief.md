---
title: "Product Brief: Nền tảng mua sắm tạp hóa khu phố"
status: final
created: 2026-11-03
updated: 2026-11-03
---

# Product Brief: Nền tảng mua sắm tạp hóa khu phố

## Tóm tắt điều hành

Một nền tảng web kết nối các cửa hàng tạp hóa trong khu phố với khách hàng sống cách đó vài con phố. Chủ tiệm đăng nhập bằng Google qua Auth0, quét mã vạch sản phẩm bằng camera điện thoại để đưa danh mục hàng lên, và quét lại chính mã đó tại quầy để tính tiền. Khách hàng xem các cửa hàng gần mình mà không cần tài khoản, thấy hàng và giá thật, rồi đặt giao hàng hoặc giữ đơn để đến lấy.

Nền tảng số hóa đúng quy trình tiệm nhỏ đang làm — kệ hàng, giá, quầy thu ngân — thay vì áp một hệ thống quản lý tồn kho kiểu kho trung tâm. Onboarding là quét, đặt giá, đăng bán. Bán online dùng lại chính danh mục tiệm đã duy trì. Nhờ vậy công sức đủ thấp cho tiệm một người và danh mục hàng thật sự mang tính địa phương — đúng thứ khách gần đó cần.

Thị trường ban đầu: một vài phường đông dân trong một thành phố của Việt Nam. Mô hình kinh doanh: pilot miễn phí, không hoa hồng, không thuê bao; mục tiêu đầu tiên là kích hoạt cửa hàng và độ chính xác đơn hàng, chưa phải doanh thu.

## Vấn đề

- **Khách không nhìn thấy tiệm quanh nhà.** Họ biết tiệm tồn tại nhưng không biết tiệm có món mình cần không, giá bao nhiêu, hoặc có ai mang tới được không. Họ mặc định đi siêu thị hoặc dùng app giao hàng với danh mục kiểu kho.
- **Chủ tiệm không có kệ hàng số với công sức thấp.** Các nền tảng hiện có bắt chụp ảnh, đặt tên, phân loại và niêm yết giá từng SKU bằng tay. Với tiệm gia đình có hàng trăm SKU, việc đó không bao giờ xong.
- **Đơn hàng đến qua chat và điện thoại.** Tiệm đọc lại tin nhắn, đoán tồn kho, không có danh mục dùng lại được và không có sổ bán hàng. Sai sót phổ biến và không tích lũy được gì.

## Giải pháp

**Cho chủ tiệm**

1. Đăng nhập Google (Auth0), tạo hồ sơ cửa hàng: tên, địa chỉ, bán kính giao hàng, khả năng nhận tại tiệm.
2. Quét mã vạch bằng camera điện thoại. App tra tên và ảnh từ CSDL sản phẩm mở rồi điền sẵn; chủ tiệm xác nhận hoặc tự nhập tên, sau đó đặt giá, đơn vị bán và tồn kho.
3. Đăng bán. Quét lại mã đã có sẽ cập nhật giá hoặc tồn kho thay vì tạo trùng.
4. Tại quầy, mở màn hình tính tiền và quét từng món để lập bill. Cùng màn hình đó dùng để soạn và đối chiếu đơn online.

**Cho khách hàng**

1. Mở site, cho phép truy cập vị trí (hoặc nhập địa chỉ), xem các cửa hàng gần có giao hàng hoặc cho nhận tại tiệm.
2. Xem danh mục trực tiếp của cửa hàng, thêm vào giỏ, chọn giao hàng hoặc nhận tại tiệm.
3. Chỉ đăng nhập Google ở bước thanh toán. Trả tiền mặt khi giao hoặc tại tiệm.
4. Theo dõi trạng thái đơn: đã đặt, đã xác nhận, sẵn sàng nhận hoặc đang giao, hoàn tất.

**Nền tảng**

- Web app mobile chạy Spring Boot + Thymeleaf, quét mã bằng camera trong trình duyệt; không có app native ở v1.
- Auth0 xử lý danh tính Google cho cả hai vai trò; phân quyền tách năng lực chủ tiệm và khách hàng.

## Điểm khác biệt

1. **Onboarding bằng mã vạch.** Danh mục được tạo bằng cách quét thứ đã có trên kệ — không phải gõ vào một CSDL sản phẩm. Đây là đòn bẩy kích hoạt.
2. **Một luồng quét cho cả kệ và quầy.** Chủ tiệm chỉ học một thao tác; danh mục online và quầy thu ngân luôn khớp nhau.
3. **Bản địa khu phố.** Bán kính gần, ưu tiên nhận tại tiệm, giao hàng do tiệm kiểm soát — không phải danh mục toàn quốc cạnh tranh với siêu thị.

Không có hào kỹ thuật. Lợi thế là độ khớp quy trình và tốc độ onboarding cho những tiệm mà nền tảng lớn bỏ qua. Thực thi và mật độ nguồn cung địa phương là tài sản phòng thủ.

## Ai được phục vụ

**Chính — Chủ tiệm.** Tiệm một người hoặc gia đình, dùng smartphone, đang có lãi, dị ứng với việc giấy tờ. Muốn thêm đơn mà không phải rời quầy. Thành công: danh mục lên trong dưới 15 phút; quét tại quầy nhanh hơn gõ giá; đơn online lặp lại mà không thêm sổ sách.

**Chính — Khách gần đó.** Mua giỏ nhỏ thường xuyên, coi trọng tiện lợi hơn danh mục rộng. Muốn biết tiệm có món đó trước khi đi bộ tới hoặc đặt. Thành công: tìm được tiệm phù hợp, nhận đúng đơn, quay lại.

## Tiêu chí thành công

Mục tiêu pilot là giả thuyết cần kiểm chứng với nhóm đầu tiên:

| Tín hiệu | Mục tiêu (giả thuyết) | Vì sao quan trọng |
|---|---|---|
| Kích hoạt cửa hàng | ≥ 70% lượt đăng ký đăng ≥ 20 mặt hàng trong 24 giờ | Chứng minh onboarding bằng mã vạch xóa được rào cản nhập liệu |
| Tra cứu mã vạch | ≥ 80% mã quét ra tên mà không phải gõ tay | Chứng minh cách tra cứu phù hợp hàng Việt |
| Tốc độ tính tiền | Thêm vào bill dưới 2 giây/món trên điện thoại tầm trung | Chứng minh luồng quầy nhanh hơn gõ giá |
| Độ chính xác đơn | ≥ 95% đơn online giao đủ và đúng | Niềm tin là ràng buộc của nhu cầu quay lại |
| Nhu cầu lặp lại | ≥ 30% khách đặt đơn thứ hai trong 30 ngày | Chứng minh bài toán khu phố là thật |

Chỉ số đối trọng: không tối ưu số cửa hàng đã onboard nếu độ chính xác danh mục hoặc chất lượng giao hàng giảm. Một cụm dày và chính xác thắng vùng phủ rộng nhưng cũ.

## Phạm vi

**Trong v1**

- Đăng nhập Google qua Auth0 cho chủ tiệm và khách; khách xem ẩn danh.
- Hồ sơ cửa hàng với địa chỉ, bán kính giao hàng, khả năng nhận tại tiệm.
- Quét mã vạch → tạo/cập nhật mặt hàng (tra tên, fallback nhập tay, giá, đơn vị, tồn kho, ảnh tùy chọn).
- Khám phá cửa hàng gần theo vị trí.
- Khách xem danh mục, giỏ hàng, chọn giao hàng hoặc nhận tại tiệm, đặt đơn.
- Quét mã tại quầy cho bán tại tiệm và soạn đơn online.
- Thanh toán tiền mặt (COD hoặc trả tại tiệm).
- Theo dõi trạng thái đơn cho khách và cửa hàng.
- Web UI responsive Spring Boot + Thymeleaf, quét bằng camera trình duyệt.

**Ngoài v1**

- App iOS/Android native.
- Thanh toán online, ví, xử lý thẻ.
- Đội giao hàng của nền tảng hoặc tích hợp đối tác giao hàng.
- Chuỗi nhiều cửa hàng, tồn kho kiểu kho, đơn mua hàng.
- Loyalty, voucher, quảng cáo, thuê bao, hoa hồng.
- Tài khoản nhân viên và phân quyền chi tiết.
- Tự động phân loại sản phẩm và merchandising phong phú.

## Rủi ro và câu hỏi mở

| Rủi ro / câu hỏi | Vì sao quan trọng | Hướng xử lý hiện tại |
|---|---|---|
| Độ phủ mã vạch hàng Việt trên CSDL mở không đều | Tỷ lệ tra cứu trúng quyết định tốc độ onboarding | Làm tra cứu + nhập tay + cache nội bộ mã đã resolve; đo tỷ lệ trúng trước khi cam kết onboarding dựa vào tra cứu |
| Chủ tiệm có thể không giữ tồn kho chính xác | Bán quá tồn làm mất niềm tin khách | Coi tồn kho là best-effort; cho bật/tắt "còn hàng / hết hàng" và thêm bước xác nhận khi nhận đơn |
| Chất lượng giao hàng phụ thuộc cửa hàng | Giao hàng tệ làm hỏng thương hiệu nền tảng | Bắt đầu bằng nhận tại tiệm và giao bán kính ngắn; định nghĩa kỳ vọng phục vụ khi onboarding |
| Chỉ thanh toán tiền mặt | Không trả trước dẫn tới no-show và không có recourse; tiền mặt giới hạn giá trị giỏ | Chấp nhận cho pilot; chốt phạm vi tiền mặt, xem lại thanh toán online sau khi có dữ liệu đơn lặp lại |
| Dữ liệu cá nhân và vị trí | Vị trí và lịch sử đơn là dữ liệu nhạy cảm | Thu thập tối thiểu vị trí cần thiết; ghi rõ thời hạn lưu trước khi ra mắt công khai |

## Tầm nhìn

Nếu pilot thành công, mọi tiệm tạp hóa trong cụm đều có kệ hàng số trực tiếp, chính xác để khách xem và đặt, còn thao tác quét tại quầy trở thành POS của tiệm. Nền tảng khi đó trở thành lớp nhu cầu cho bán lẻ độc lập nhỏ: khám phá địa phương, danh mục đáng tin, và lịch sử thanh toán mở ra mua sỉ, mật độ giao hàng và sản phẩm vốn lưu động — mà không biến chủ tiệm thành nhân viên nhập liệu.
