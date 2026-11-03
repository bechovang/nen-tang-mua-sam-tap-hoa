---
title: "PRD: Nền tảng mua sắm tạp hóa khu phố"
status: final
created: 2026-11-03
updated: 2026-11-03
---

# PRD: Nền tảng mua sắm tạp hóa khu phố

*Tên làm việc — cần xác nhận trước khi ra mắt.*

## 0. Mục đích tài liệu

PRD này dành cho founder/PM, nhà thiết kế UX, kiến trúc sư và người viết story. Nó chuyển Product Brief tại `../../briefs/brief-nen-tang-mua-sam-tap-hoa-2026-11-03/brief.md` thành yêu cầu hành vi có thể kiểm thử; lập luận thị trường và định vị không được lặp lại ở đây. Thuật ngữ khóa nằm ở §3 và được dùng nguyên văn trong toàn bộ FR, UJ và chỉ số. FR được đánh số toàn cục để story và kiến trúc tham chiếu ổn định. Mọi suy luận chưa được founder xác nhận được đánh dấu `[GIẢ ĐỊNH: ...]` và tổng hợp ở §12.

## 1. Tầm nhìn

Nền tảng biến kệ hàng của tiệm tạp hóa thành một danh mục số sống, cập nhật bằng chính thao tác quét mã vạch. Chủ tiệm không phải nhập liệu từ đầu: quét món đang bán, đặt giá, đăng bán. Khách hàng gần đó mở web, thấy tiệm nào còn món mình cần, giá bao nhiêu, và chọn giao hàng hoặc ghé lấy.

Sản phẩm thắng bằng độ khớp quy trình, không bằng công nghệ. Một thao tác quét phục vụ cả việc dựng danh mục lẫn tính tiền tại quầy; một danh mục phục vụ cả khách online lẫn khách vãng lai. Vòng lặp này giữ dữ liệu đúng mà không thêm việc cho tiệm.

Pilot chạy trong một vài phường đông dân, miễn phí, không hoa hồng. Thành công của pilot là bằng chứng rằng tiệm nhỏ có thể lên số trong dưới 15 phút và giao đúng đơn — trước khi nghĩ tới doanh thu, mở rộng hay thanh toán online.

## 2. Người dùng mục tiêu

### 2.1 Việc cần hoàn thành (Jobs To Be Done)

- **Chủ tiệm — bán nhiều hơn mà không rời quầy:** "Khi khách hỏi tiệm có món này không, tôi muốn có danh mục số để trả lời và nhận đơn mà không phải gõ từng món."
- **Chủ tiệm — tính tiền nhanh:** "Khi khách đứng ở quầy, tôi muốn quét món và ra tổng tiền nhanh hơn gõ giá."
- **Chủ tiệm — soạn đơn không sai:** "Khi có đơn online, tôi muốn đối chiếu hàng bằng cách quét thay vì đọc lại tin nhắn."
- **Khách hàng — biết trước rồi mới đi/đặt:** "Khi cần vài món gần nhà, tôi muốn biết tiệm nào còn hàng và giá bao nhiêu trước khi đi bộ tới hoặc đặt giao."
- **Khách hàng — nhận đúng thứ mình đặt:** "Khi đặt hàng, tôi muốn theo dõi trạng thái và nhận đủ hàng, trả tiền mặt khi nhận."

### 2.2 Người dùng không thuộc phạm vi v1

- Chuỗi siêu thị hoặc cửa hàng có hệ thống tồn kho riêng cần đồng bộ.
- Người bán chỉ online, không có điểm bán vật lý.
- Khách ngoài khu vực pilot hoặc ngoài bán kính phục vụ.
- Đội giao hàng độc lập muốn nhận đơn trên nền tảng.
- Quản trị viên doanh nghiệp cần phân quyền chi tiết; vận hành pilot làm thủ công.

### 2.3 Hành trình người dùng chính

#### UJ-1. Chị Lan đưa kệ hàng 40 món lên trong một buổi sáng

Chị Lan, 42 tuổi, mở tiệm tạp hóa 20 m² trong hẻm, dùng điện thoại Android tầm trung và chưa từng dùng phần mềm bán hàng. Chị nhận link pilot từ đội vận hành, chưa có tài khoản. Chị mở link, đăng nhập Google, tạo hồ sơ cửa hàng với tên, địa chỉ, bán kính giao 1 km và bật nhận tại tiệm. Chị mở phiên quét, quét lần lượt 40 mã vạch trên kệ; 32 mã được tra sẵn tên, 8 mã chị tự nhập tên rồi đặt giá, đơn vị và tồn kho. Khi đăng bán, màn hình hiện "40 mặt hàng đang bán" và bản xem trước trang cửa hàng. **Cao điểm:** chị thấy cửa hàng của mình xuất hiện trong kết quả tìm kiếm gần đó. **Kết thúc:** chị đóng app, quay lại quầy. **Ngoại lệ:** nếu mất mạng giữa phiên, các mặt hàng đã lưu vẫn còn; món đang nhập dở được giữ nháp trên thiết bị.

#### UJ-2. Anh Minh đặt 6 món giao trong một giờ

Anh Minh, 31 tuổi, ở chung cư cách tiệm 800 m, vừa hết nước mắm và trứng. Anh mở site trên Safari, chưa đăng nhập, cho phép truy cập vị trí và thấy bốn cửa hàng trong bán kính 1,2 km. Anh chọn tiệm đang mở, thêm sáu mặt hàng, chọn giao hàng, rồi đăng nhập Google ở bước thanh toán. Anh nhập số điện thoại, địa chỉ và ghi chú "gọi trước khi tới". **Cao điểm:** màn hình xác nhận hiện mã đơn, tổng tiền gồm phí giao và khoảng thời gian dự kiến; tiệm nhận thông báo đơn mới. **Kết thúc:** anh theo dõi trạng thái và trả tiền mặt khi nhận. **Ngoại lệ:** tiệm từ chối vì hết trứng; anh nhận thông báo kèm lý do và được gợi ý cửa hàng khác.

#### UJ-3. Chị Hoa đặt trước và ghé lấy trên đường về

Chị Hoa, 38 tuổi, tan làm lúc 18 giờ và muốn ghé tiệm trên đường về. Chị đã đăng nhập từ lần trước. Chị mở danh sách cửa hàng gần đây, chọn tiệm quen, thêm bốn mặt hàng, chọn nhận tại tiệm và khung 18:30–19:00. **Cao điểm:** đơn chuyển sang đã xác nhận, hiện mã đơn và giờ hẹn. **Kết thúc:** chị đến tiệm, đọc mã đơn; chủ tiệm đối chiếu và thu tiền mặt. **Ngoại lệ:** chị đến muộn 40 phút; tiệm vẫn giữ đơn tới 19:40 theo chính sách giữ đơn.

#### UJ-4. Chị Lan quét mã tại quầy cho khách vãng lai

Một khách không dùng app bước vào tiệm. Chị Lan mở màn hình bán tại quầy, quét năm món; một mã không có trong danh mục, chị tạo nhanh mặt hàng với tên và giá rồi thêm vào hóa đơn. Chị bấm tính tiền, nhận tiền mặt và hoàn tất. **Cao điểm:** hóa đơn hiện đủ năm món, tổng đúng, tồn kho được trừ. **Kết thúc:** đơn tại quầy lưu vào lịch sử; chị quay lại quầy. **Ngoại lệ:** camera không đọc được mã vì bao bì nhăn; chị nhập mã tay hoặc tìm theo tên.

#### UJ-5. Chị Lan soạn đơn online bằng quét mã

Chị Lan có một đơn online mới ở trạng thái đã xác nhận. Chị mở đơn, bấm "Soạn hàng" và quét từng món; hệ thống tăng số lượng đã soạn tương ứng. Khi quét tới nước mắm, chị thấy khách đặt ba chai nhưng chỉ còn hai. Chị đánh dấu thiếu, nhập số lượng thực tế là hai; hệ thống tính lại tổng và hiện chênh lệch tiền mặt. **Cao điểm:** danh sách đủ/thiếu rõ ràng; khách nhận thông báo điều chỉnh trước khi đơn chuyển sang sẵn sàng nhận hoặc đang giao. **Kết thúc:** đơn hoàn tất khi giao; tồn kho đã trừ. **Ngoại lệ:** khách không đồng ý với điều chỉnh và hủy đơn; tồn kho được hoàn.

#### UJ-6. Anh Minh đặt lại đơn cũ

Anh Minh mở lịch sử đơn, chọn đơn tuần trước và bấm "Đặt lại". Hệ thống dựng giỏ từ đơn cũ, kiểm tra giá và tồn hiện tại, bỏ mặt hàng đã ngừng bán và cảnh báo món đã đổi giá. Anh xác nhận, chọn giao hàng và đặt. **Cao điểm:** đơn mới được tạo với giá hiện tại, không phải giá cũ. **Kết thúc:** anh theo dõi đơn mới. **Ngoại lệ:** một mặt hàng đã hết; hệ thống bỏ khỏi giỏ và hiển thị rõ lý do.

## 3. Thuật ngữ

- **Chủ tiệm** — người dùng sở hữu và quản lý một cửa hàng trên nền tảng.
- **Khách hàng** — người dùng đặt hàng hoặc mua tại quầy; có thể xem ẩn danh trước khi đăng nhập.
- **Cửa hàng** — đơn vị bán trên nền tảng, có địa chỉ, toạ độ, bán kính phục vụ và giờ mở cửa.
- **Tiệm** — cách gọi ngắn của cửa hàng trong tài liệu này; không phải một thực thể khác.
- **POS tại quầy** — màn hình bán hàng trực tiếp tại cửa hàng, dùng chung danh mục hàng với đơn online.
- **Danh mục hàng** — tập mặt hàng đang bán của một cửa hàng.
- **Mặt hàng** — một SKU trong danh mục hàng; có mã vạch, tên, giá bán, đơn vị bán và tồn kho.
- **Mã vạch** — chuỗi EAN/UPC/Code-128 in trên bao bì, dùng để nhận diện mặt hàng.
- **Phiên quét** — một lần mở camera để quét liên tiếp nhiều mã vạch.
- **Giỏ hàng** — tập mặt hàng khách chọn trước khi đặt đơn hàng.
- **Đơn hàng** — giao dịch đặt hàng online giữa khách hàng và cửa hàng.
- **Đơn tại quầy** — giao dịch bán trực tiếp tại cửa hàng, không đi qua giỏ hàng online.
- **Giao hàng** — cửa hàng tự vận chuyển đơn hàng tới địa chỉ khách hàng.
- **Nhận tại tiệm** — khách hàng đến cửa hàng lấy đơn hàng.
- **Bán kính phục vụ** — khoảng cách tối đa cửa hàng nhận giao hàng.
- **Trạng thái đơn** — một giá trị trong máy trạng thái ở §4.6.
- **Khu vực pilot** — vài phường đông dân được chọn để vận hành thí điểm.
- **Danh tính Google** — tài khoản Google được xác thực qua Auth0.
- **Tồn kho** — số lượng còn bán của mặt hàng; là dữ liệu best-effort, không phải tồn kho kế toán.
- **Đơn vị bán** — đơn vị khách mua: cái, gói, kg, lố, chai.
- **Giá bán** — giá niêm yết của mặt hàng tại thời điểm khách xem hoặc đặt.

## 4. Tính năng

### 4.1 Danh tính và phân quyền

**Mô tả:** Người dùng xác thực bằng Danh tính Google qua Auth0. Khách hàng xem công khai không cần tài khoản; đăng nhập chỉ bắt buộc khi đặt hàng hoặc vào khu vực cửa hàng. Một người có thể vừa là khách hàng vừa là chủ tiệm. Hiện thực hóa UJ-1 đến UJ-6.

#### FR-1: Đăng nhập bằng Google qua Auth0

Khách hàng và chủ tiệm có thể đăng nhập bằng Danh tính Google qua Auth0.

**Hệ quả kiểm thử được:**
- Nút "Đăng nhập bằng Google" chuyển hướng tới Auth0 với connection Google; không có form mật khẩu nội bộ.
- Sau callback thành công, hệ thống tạo hoặc cập nhật người dùng theo `sub` của Auth0; đăng nhập lại không tạo bản ghi trùng.
- Email đã xác thực từ Google được lưu và không phải nhập lại.
- Đăng nhập thất bại hiển thị thông báo lỗi và giữ nguyên trang hiện tại.

**Ngoài phạm vi:** mật khẩu nội bộ, OTP SMS, đăng nhập Facebook/Apple.

#### FR-2: Vai trò và phân quyền

Hệ thống phân quyền theo vai trò `KHACH_HANG` và `CHU_TIEM`; một người dùng có thể có cả hai.

**Hệ quả kiểm thử được:**
- Mọi người dùng đăng nhập mặc định có vai trò `KHACH_HANG`.
- Người dùng tạo hồ sơ cửa hàng thành công được cấp vai trò `CHU_TIEM`.
- Đường dẫn `/store/**` và `/pos/**` chỉ cho `CHU_TIEM` có cửa hàng đang hoạt động; người khác nhận 403.
- Đường dẫn `/orders/**` chỉ trả về đơn thuộc người dùng hiện tại; truy cập đơn của người khác trả 404 để không lộ sự tồn tại.
- Khách hàng ẩn danh chỉ truy cập được các đường dẫn công khai.

#### FR-3: Phiên và đăng xuất

Người dùng có thể đăng xuất và phiên hết hạn an toàn.

**Hệ quả kiểm thử được:**
- Đăng xuất xóa phiên cục bộ và chuyển qua logout của Auth0.
- Phiên idle hết hạn sau 8 giờ; cookie phiên có `HttpOnly`, `Secure`, `SameSite=Lax`.
- Sau khi hết hạn, thao tác cần đăng nhập chuyển tới Auth0 rồi quay lại đúng URL ban đầu.

#### FR-4: Xem ẩn danh

Khách hàng ẩn danh có thể xem trang chủ, danh sách cửa hàng gần, trang cửa hàng và danh mục hàng.

**Hệ quả kiểm thử được:**
- Không có cookie phiên vẫn xem được các trang công khai.
- Thêm vào giỏ được phép ẩn danh; giỏ hàng lưu trong session.
- Bấm đặt hàng khi chưa đăng nhập chuyển tới Auth0; sau đăng nhập quay lại checkout với giỏ hàng còn nguyên.
- Trang công khai không hiển thị email hoặc tên đầy đủ của người dùng khác.

### 4.2 Hồ sơ cửa hàng

**Mô tả:** Chủ tiệm tạo và duy trì hồ sơ cửa hàng — dữ liệu quyết định cửa hàng có xuất hiện trong kết quả gần hay không và khách có thể giao hàng hay nhận tại tiệm. Hiện thực hóa UJ-1, UJ-2, UJ-3.

#### FR-5: Tạo hồ sơ cửa hàng

Chủ tiệm có thể tạo một cửa hàng với tên, địa chỉ, toạ độ, số điện thoại, bán kính phục vụ, hình thức nhận hàng, giờ mở cửa, phí giao hàng và giá trị đơn tối thiểu.

**Hệ quả kiểm thử được:**
- Form yêu cầu tên 2–80 ký tự, số điện thoại Việt Nam hợp lệ, bán kính 0,5–5 km, giờ mở cửa hợp lệ.
- Toạ độ được chọn bằng cách ghim trên bản đồ; hệ thống lưu vĩ độ và kinh độ.
- Chủ tiệm có thể bật/tắt giao hàng và nhận tại tiệm độc lập; phải bật ít nhất một hình thức.
- Lưu thành công chuyển thẳng tới phiên quét đầu tiên.
- Một người dùng chỉ có một cửa hàng trong v1; tạo cửa hàng thứ hai trả lỗi nghiệp vụ. `[GIẢ ĐỊNH: Một chủ tiệm chỉ có một cửa hàng trong v1.]`

#### FR-6: Cập nhật hồ sơ và bán kính

Chủ tiệm có thể sửa mọi trường của hồ sơ cửa hàng.

**Hệ quả kiểm thử được:**
- Thay đổi bán kính hoặc hình thức nhận hàng có hiệu lực với tìm kiếm trong vòng 60 giây.
- Thay đổi toạ độ cập nhật khoảng cách hiển thị cho khách.
- Không thể tắt cả giao hàng và nhận tại tiệm; hệ thống yêu cầu chọn ít nhất một.
- Giá trị đơn tối thiểu mặc định là 0 và có thể đặt tới 5.000.000 ₫. `[GIẢ ĐỊNH: Giá trị đơn tối thiểu mặc định 0 và do cửa hàng tùy chọn.]`

#### FR-7: Trạng thái mở, tạm đóng và giờ mở cửa

Cửa hàng có trạng thái mở hoặc tạm đóng; ngoài giờ mở cửa được coi là đóng.

**Hệ quả kiểm thử được:**
- Cửa hàng tạm đóng hoặc ngoài giờ không xuất hiện trong danh sách gần và không nhận đơn mới.
- Đơn đã tạo trước khi đóng vẫn tiếp tục xử lý.
- Chủ tiệm có thể tạm đóng ngay cả trong giờ mở cửa; trạng thái hiển thị cho khách.
- Trang cửa hàng hiển thị giờ mở cửa và nhãn "đang đóng" khi áp dụng.

### 4.3 Danh mục hàng và quét mã vạch

**Mô tả:** Đây là đòn bẩy kích hoạt. Chủ tiệm quét mã vạch trên kệ, hệ thống tra cứu tên/ảnh và điền sẵn, chủ tiệm xác nhận giá và đơn vị bán. Quét lại mã đã có sẽ cập nhật thay vì tạo trùng. Hiện thực hóa UJ-1, UJ-4, UJ-5.

#### FR-8: Quét mã vạch bằng camera

Chủ tiệm có thể quét mã vạch bằng camera sau của điện thoại trong trình duyệt.

**Hệ quả kiểm thử được:**
- Hỗ trợ EAN-13, EAN-8, UPC-A và Code-128.
- Sau khi mã nằm trong khung, hệ thống trả kết quả trong vòng 1 giây trên điện thoại tầm trung.
- Có phản hồi rung và nháy xanh khi nhận mã; âm thanh có thể tắt.
- Nếu quyền camera bị từ chối, hiển thị hướng dẫn cấp quyền và ô nhập mã tay.
- Tính năng chỉ hoạt động trên HTTPS hoặc localhost; không lưu ảnh camera.

#### FR-9: Tra cứu mã vạch và điền sẵn

Khi nhận mã vạch, hệ thống tra cứu tên và ảnh từ CSDL hàng hóa mở và điền sẵn vào form tạo mặt hàng.

**Hệ quả kiểm thử được:**
- Tra cứu có thời gian chờ 3 giây; quá thời gian hoặc lỗi mạng không chặn tạo mặt hàng.
- Kết quả tra cứu được lưu vào bộ nhớ đệm nội bộ theo mã; lần quét sau không gọi lại nguồn ngoài trong 30 ngày.
- Form hiển thị nguồn dữ liệu "Tra cứu" hoặc "Tự nhập".
- Không có kết quả thì form mở với mã đã điền và trường tên trống.

#### FR-10: Tạo mặt hàng

Chủ tiệm có thể tạo mặt hàng mới với mã vạch, tên, giá bán, đơn vị bán, tồn kho và ảnh tùy chọn.

**Hệ quả kiểm thử được:**
- Giá bán lớn hơn 0; tên 1–120 ký tự; đơn vị bán 1–20 ký tự; tồn kho là số nguyên không âm.
- Mặt hàng được gắn với cửa hàng hiện tại và mặc định ở trạng thái còn hàng.
- Sau khi lưu, hệ thống quay lại phiên quét và hiển thị thông báo đã thêm.
- Ảnh tùy chọn tối đa 2 MB; hệ thống nén trước khi lưu.

#### FR-11: Cập nhật khi quét lại

Khi quét một mã đã có trong danh mục hàng của cửa hàng, hệ thống mở form cập nhật thay vì tạo mặt hàng mới.

**Hệ quả kiểm thử được:**
- Form hiển thị giá bán và tồn kho hiện tại; chủ tiệm sửa nhanh được cả hai.
- Không thể tạo bản ghi trùng cho cùng cặp (cửa hàng, mã vạch).
- Mã đã thuộc cửa hàng khác vẫn được phép tạo mới trong cửa hàng hiện tại.
- Cập nhật giá không làm thay đổi giá trong các đơn hàng đã tạo trước đó.

#### FR-12: Quản lý danh mục hàng

Chủ tiệm có thể xem, tìm, lọc, sửa và xóa mềm mặt hàng trong danh mục hàng.

**Hệ quả kiểm thử được:**
- Danh sách phân trang 50 dòng, tìm theo tên hoặc mã vạch, lọc theo còn hàng/hết hàng.
- Xóa mềm ẩn mặt hàng khỏi danh sách bán nhưng không làm hỏng đơn hàng cũ.
- Màn hình hiển thị tổng số mặt hàng đang bán.
- Sắp xếp mặc định là mới cập nhật gần nhất.

#### FR-13: Kiểm tra mã vạch và nhập tay

Hệ thống kiểm tra tính hợp lệ và duy nhất của mã vạch, đồng thời cho phép nhập mã khi không quét được.

**Hệ quả kiểm thử được:**
- Mã nhập tay dài 6–32 ký tự chữ và số; hệ thống chuẩn hóa khoảng trắng và chuyển về chữ hoa.
- Lưu trùng mã trong cùng cửa hàng trả lỗi và mở mặt hàng hiện có.
- Mã không hợp lệ hiển thị lỗi ngay trên trường nhập, không mất dữ liệu khác.
- Mã vạch là bắt buộc cho mọi mặt hàng trong v1. `[GIẢ ĐỊNH: Mọi mặt hàng đều có mã vạch; không hỗ trợ mặt hàng bán theo cân không mã.]`

### 4.4 Khám phá cửa hàng gần

**Mô tả:** Khách hàng thấy cửa hàng đang mở, có hàng, trong khoảng cách hợp lý, kèm khả năng giao hàng hoặc nhận tại tiệm. Hiện thực hóa UJ-2, UJ-3.

#### FR-14: Xác định vị trí khách hàng

Khách hàng có thể chia sẻ vị trí hiện tại hoặc chọn phường trong khu vực pilot.

**Hệ quả kiểm thử được:**
- Nếu khách từ chối quyền vị trí, hệ thống hiển thị danh sách phường của khu vực pilot để chọn.
- Vị trí chỉ lưu trong session và không lưu vĩnh viễn nếu khách không đặt hàng.
- Khoảng cách hiển thị được làm tròn tới 100 m.
- Không hiển thị toạ độ chính xác của khách cho cửa hàng trước khi khách đặt hàng.

#### FR-15: Danh sách cửa hàng gần

Hệ thống trả về các cửa hàng trong bán kính 5 km hoặc trong bán kính phục vụ của cửa hàng, đang mở và có ít nhất một mặt hàng còn hàng.

**Hệ quả kiểm thử được:**
- Kết quả sắp xếp theo khoảng cách tăng dần; cùng khoảng cách thì nhiều mặt hàng còn hàng hơn đứng trước.
- Mỗi thẻ hiển thị tên cửa hàng, khoảng cách, giao hàng/nhận tại tiệm, giờ mở cửa và số mặt hàng còn hàng.
- Cửa hàng ngoài bán kính phục vụ vẫn hiện nếu cho nhận tại tiệm và nằm trong 5 km.
- Không có kết quả thì hiển thị gợi ý mở rộng bán kính hoặc chọn phường khác.
- Thời gian phản hồi p95 ≤ 800 ms với 500 cửa hàng.

#### FR-16: Trang cửa hàng và tìm kiếm

Khách hàng có thể mở trang công khai của cửa hàng và tìm kiếm theo tên cửa hàng hoặc tên mặt hàng.

**Hệ quả kiểm thử được:**
- Trang cửa hàng hiển thị tên, địa chỉ, khoảng cách, hình thức nhận hàng, giờ mở cửa và danh mục hàng.
- Mặt hàng hết hàng vẫn hiển thị nhưng có nhãn "hết hàng" và không thêm được vào giỏ hàng.
- Tìm kiếm theo tên mặt hàng trả về các cửa hàng gần đang bán mặt hàng đó.
- Trang được kết xuất phía server và có title/meta mô tả cho từng cửa hàng.

### 4.5 Đặt hàng

**Mô tả:** Khách hàng chọn hàng, chọn giao hàng hoặc nhận tại tiệm, đăng nhập ở bước cuối và đặt đơn. Thanh toán tiền mặt. Hiện thực hóa UJ-2, UJ-3, UJ-6.

#### FR-17: Giỏ hàng

Khách hàng có thể thêm, sửa số lượng và xóa mặt hàng trong giỏ hàng của một cửa hàng.

**Hệ quả kiểm thử được:**
- Giỏ hàng chỉ chứa mặt hàng từ một cửa hàng; thêm mặt hàng từ cửa hàng khác hỏi có xóa giỏ hiện tại không.
- Giỏ hàng ẩn danh lưu trong session; sau đăng nhập được gộp vào tài khoản.
- Tạm tính, phí giao hàng và tổng tiền được tính lại khi thay đổi số lượng hoặc hình thức nhận hàng.
- Khi vào checkout, giá được làm mới; nếu giá thay đổi, khách thấy cảnh báo trước khi đặt.
- Không thêm được mặt hàng hết hàng hoặc đã xóa mềm.

#### FR-18: Chọn giao hàng hoặc nhận tại tiệm

Khách hàng chọn hình thức nhận hàng dựa trên khả năng của cửa hàng và khoảng cách.

**Hệ quả kiểm thử được:**
- Giao hàng chỉ khả dụng khi khoảng cách từ cửa hàng tới địa chỉ khách không vượt bán kính phục vụ.
- Nhận tại tiệm chỉ khả dụng khi cửa hàng bật hình thức này.
- Nhận tại tiệm cho phép khách chọn khung giờ 30 phút trong giờ mở cửa; khung giờ mặc định là khung gần nhất còn nhận.
- Nếu chỉ một hình thức khả dụng, hệ thống chọn sẵn và không bắt khách chọn lại.
- Giao hàng hiển thị phí giao hàng và giá trị đơn tối thiểu; nhận tại tiệm hiển thị địa chỉ và khung giờ. `[GIẢ ĐỊNH: Phí giao hàng là số tiền cố định do cửa hàng đặt, không tính theo khoảng cách.]`
- Giao hàng yêu cầu địa chỉ giao và số điện thoại; địa chỉ mặc định lấy từ vị trí hiện tại, khách sửa được bằng cách chọn phường và nhập số nhà.
- Khoảng cách kiểm tra bán kính dùng toạ độ của vị trí giao đã chọn, không dùng toạ độ lúc mở trang.
- Hệ thống chặn đặt hàng nếu ngoài bán kính, ngoài giờ mở cửa hoặc dưới giá trị đơn tối thiểu.

#### FR-19: Checkout và đăng nhập

Khách hàng có thể đặt hàng sau khi đăng nhập, với tóm tắt đơn và phương thức thanh toán tiền mặt.

**Hệ quả kiểm thử được:**
- Người chưa đăng nhập được chuyển tới Auth0 và quay lại checkout với giỏ hàng còn nguyên.
- Trang checkout hiển thị danh sách mặt hàng, số lượng, đơn giá, phí giao hàng, tổng tiền và hình thức nhận hàng.
- Nút "Đặt hàng" có tính idempotent: bấm hai lần liên tiếp chỉ tạo một đơn hàng.
- Hệ thống lưu bản chụp tên, đơn giá và đơn vị bán tại thời điểm đặt.
- Mã đơn hàng ngắn, duy nhất, hiển thị cho cả khách hàng và cửa hàng.

#### FR-20: Tạo đơn hàng

Hệ thống tạo đơn hàng ở trạng thái `CHO_XAC_NHAN` và thông báo cho cửa hàng.

**Hệ quả kiểm thử được:**
- Đơn hàng, các dòng mặt hàng và sự kiện tạo đơn được ghi trong cùng một giao dịch CSDL.
- Nếu cửa hàng chuyển sang tạm đóng giữa lúc checkout, hệ thống trả lỗi nghiệp vụ, giữ giỏ hàng và không tạo đơn.
- Cửa hàng nhận thông báo đơn mới trong vòng 60 giây.
- Khách hàng thấy trang xác nhận với mã đơn, tổng tiền và hình thức nhận hàng.

#### FR-21: Thanh toán tiền mặt

Đơn hàng v1 chỉ hỗ trợ thanh toán tiền mặt khi giao hàng hoặc tại tiệm.

**Hệ quả kiểm thử được:**
- Checkout hiển thị rõ "Thanh toán tiền mặt khi nhận hàng" hoặc "Thanh toán tại tiệm".
- Hệ thống không thu thập, truyền hoặc lưu thông tin thẻ hoặc ví điện tử.
- Chủ tiệm đánh dấu đã thu tiền khi hoàn tất đơn hàng; cờ này được lưu cùng đơn hàng và hiển thị trong lịch sử.
- Không có luồng hoàn tiền online; điều chỉnh thiếu hàng xử lý bằng chênh lệch tiền mặt.

#### FR-22: Hủy đơn hàng

Khách hàng và chủ tiệm có thể hủy đơn hàng trong các trạng thái được phép.

**Hệ quả kiểm thử được:**
- Khách hàng hủy được khi đơn ở `CHO_XAC_NHAN` hoặc `DA_XAC_NHAN` và chưa chuyển sang `DANG_SOAN`.
- Chủ tiệm hủy được trước `HOAN_TAT` và bắt buộc nhập lý do.
- Hủy đơn hoàn lại tồn kho đã trừ và gửi thông báo cho cả hai bên.
- Không hủy được đơn đã `HOAN_TAT`.

#### FR-23: Lịch sử và đặt lại đơn

Khách hàng có thể xem lịch sử đơn hàng và đặt lại một đơn cũ.

**Hệ quả kiểm thử được:**
- Lịch sử hiển thị đơn mới nhất trước, phân trang 20 đơn.
- "Đặt lại" dựng giỏ hàng từ đơn cũ, bỏ mặt hàng ngừng bán hoặc hết hàng và hiển thị mặt hàng bị bỏ.
- Nếu giá hiện tại khác giá cũ, giỏ hàng hiển thị giá mới và cảnh báo.
- "Đặt lại" không tự tạo đơn; khách hàng phải xác nhận lại.

### 4.6 Quản lý đơn hàng tại cửa hàng

**Mô tả:** Chủ tiệm xác nhận, soạn, giao hoặc chuẩn bị đơn nhận tại tiệm, và xử lý thiếu hàng. Quét mã là cách đối chiếu chính. Hiện thực hóa UJ-2, UJ-3, UJ-5.

#### FR-24: Danh sách đơn hàng

Chủ tiệm có thể xem đơn mới, đang xử lý và đã hoàn tất.

**Hệ quả kiểm thử được:**
- Bảng điều khiển hiển thị số đếm đơn `CHO_XAC_NHAN` và tự làm mới mỗi 30 giây hoặc theo nút làm mới.
- Lọc được theo trạng thái và khoảng ngày.
- Mỗi dòng hiển thị mã đơn, tên khách, tổng tiền, hình thức nhận hàng và giờ hẹn.
- Đơn quá 15 phút chưa xác nhận được đánh dấu cảnh báo.

#### FR-25: Xác nhận hoặc từ chối đơn

Chủ tiệm có thể xác nhận hoặc từ chối đơn hàng mới.

**Hệ quả kiểm thử được:**
- Xác nhận chuyển đơn sang `DA_XAC_NHAN` và gửi thông báo cho khách hàng.
- Từ chối chuyển sang `DA_HUY`, bắt buộc chọn lý do và gửi thông báo kèm lý do.
- Hệ thống cảnh báo nếu tồn kho không đủ nhưng vẫn cho xác nhận vì tồn kho là best-effort.
- Chỉ đơn ở `CHO_XAC_NHAN` mới xác nhận hoặc từ chối được.

#### FR-26: Soạn hàng bằng quét mã

Chủ tiệm có thể soạn đơn hàng bằng cách quét mã vạch từng mặt hàng.

**Hệ quả kiểm thử được:**
- Mỗi lần quét mã thuộc đơn hàng tăng số lượng đã soạn của mặt hàng đó lên 1.
- Màn hình hiển thị tiến độ "đã soạn / cần soạn" cho từng dòng.
- Quét mã không thuộc đơn hàng hiển thị cảnh báo và không thêm vào đơn.
- Quét vượt số lượng cần soạn hiển thị cảnh báo nhưng không tự tăng số lượng.
- Hoàn tất soạn khi mọi dòng đủ số lượng, hoặc chuyển sang FR-27 nếu thiếu.

#### FR-27: Xử lý thiếu hàng

Chủ tiệm có thể đánh dấu mặt hàng thiếu và nhập số lượng thực tế.

**Hệ quả kiểm thử được:**
- Hệ thống tính lại tổng tiền đơn hàng và hiển thị chênh lệch tiền mặt so với ban đầu.
- Khách hàng nhận thông báo điều chỉnh trước khi đơn chuyển sang `SAN_SANG` hoặc `DANG_GIAO`.
- Khách hàng có thể chấp nhận điều chỉnh hoặc hủy đơn; hủy hoàn tồn kho.
- Phần số lượng giảm do thiếu hàng được hoàn lại vào tồn kho.
- Không có hoàn tiền online; chênh lệch được xử lý khi thanh toán tiền mặt.

#### FR-28: Máy trạng thái đơn hàng

Hệ thống chỉ cho phép các chuyển trạng thái hợp lệ và ghi lại từng lần chuyển.

**Hệ quả kiểm thử được:**
- Các trạng thái hợp lệ: `CHO_XAC_NHAN`, `DA_XAC_NHAN`, `DANG_SOAN`, `SAN_SANG`, `DANG_GIAO`, `HOAN_TAT`, `DA_HUY`, `KHONG_NHAN`.
- Luồng giao hàng: `CHO_XAC_NHAN → DA_XAC_NHAN → DANG_SOAN → DANG_GIAO → HOAN_TAT`.
- Luồng nhận tại tiệm: `CHO_XAC_NHAN → DA_XAC_NHAN → DANG_SOAN → SAN_SANG → HOAN_TAT` hoặc `SAN_SANG → KHONG_NHAN`.
- Chuyển trạng thái không hợp lệ trả lỗi và không thay đổi dữ liệu.
- Mỗi lần chuyển lưu người thực hiện, thời điểm, trạng thái cũ và mới.
- Đơn tại quầy được tạo trực tiếp ở `HOAN_TAT` và không đi qua các trạng thái của đơn online.

#### FR-29: Trừ tồn kho và nhật ký kho

Hệ thống cập nhật tồn kho theo đơn hàng và đơn tại quầy.

**Hệ quả kiểm thử được:**
- Đơn hàng được xác nhận sẽ giữ tồn kho; hủy đơn hoàn lại; hoàn tất đơn tại quầy trừ tồn kho.
- Tồn kho không xuống dưới 0; nếu thiếu, hệ thống giữ ở 0 và ghi cảnh báo.
- Mỗi thay đổi tồn kho ghi một sự kiện gồm mặt hàng, số lượng thay đổi, lý do và tham chiếu đơn hàng.
- Không có nghiệp vụ kiểm kê hoặc nhập kho trong v1.

### 4.7 POS tại quầy

**Mô tả:** Chủ tiệm dùng cùng hạ tầng quét mã để bán trực tiếp cho khách vãng lai. Đơn tại quầy không cần khách hàng đăng nhập. `[GIẢ ĐỊNH: Đơn tại quầy không gắn với khách hàng đã đăng ký.]` Hiện thực hóa UJ-4.

#### FR-30: Mở phiên bán tại quầy

Chủ tiệm có thể mở một phiên bán tại quầy.

**Hệ quả kiểm thử được:**
- Mỗi cửa hàng có tối đa một phiên bán đang mở; mở phiên mới khi phiên cũ còn mở hiển thị cảnh báo và cho phép hủy phiên cũ.
- Phiên bán được lưu phía server để không mất khi tải lại trang.
- Chủ tiệm hủy được phiên bán mà không tạo đơn tại quầy.
- Phiên bán không hiển thị cho khách hàng.

#### FR-31: Quét và thêm mặt hàng vào hóa đơn

Chủ tiệm có thể thêm mặt hàng vào hóa đơn bằng cách quét mã hoặc tìm theo tên.

**Hệ quả kiểm thử được:**
- Quét mã có trong danh mục hàng thêm dòng với số lượng 1; quét lại tăng số lượng.
- Tìm theo tên hiển thị tối đa 10 kết quả và cho chọn để thêm.
- Chủ tiệm sửa số lượng hoặc xóa dòng; tổng tiền cập nhật ngay.
- Thêm mặt hàng hết hàng hiển thị cảnh báo nhưng vẫn cho bán vì tồn kho là best-effort.

#### FR-32: Tạo nhanh mặt hàng khi mã chưa có

Khi quét mã chưa có trong danh mục hàng, chủ tiệm có thể tạo nhanh mặt hàng ngay trong phiên bán.

**Hệ quả kiểm thử được:**
- Hộp thoại tạo nhanh yêu cầu tên và giá bán; đơn vị bán mặc định "cái"; tồn kho mặc định 0.
- Lưu thành công thêm mặt hàng vào danh mục hàng và vào hóa đơn.
- Chủ tiệm có thể bỏ qua mã đó mà không tạo mặt hàng.
- Mặt hàng tạo nhanh xuất hiện trong danh mục hàng sau khi kết thúc phiên bán.

#### FR-33: Hoàn tất bán và biên nhận

Chủ tiệm có thể xác nhận thanh toán tiền mặt và hoàn tất đơn tại quầy.

**Hệ quả kiểm thử được:**
- Hoàn tất tạo đơn tại quầy ở trạng thái `HOAN_TAT`, trừ tồn kho và lưu lịch sử.
- Biên nhận hiển thị mã đơn, thời gian, danh sách mặt hàng, tổng tiền và số tiền khách đưa/tiền thừa nếu chủ tiệm nhập.
- Không in qua máy in trong v1; biên nhận hiển thị để chụp hoặc gửi.
- Hóa đơn trống không thể hoàn tất.

### 4.8 Trạng thái đơn và thông báo

**Mô tả:** Khách hàng và chủ tiệm luôn biết đơn đang ở đâu. Thông báo trong ứng dụng và email là kênh chính trong v1. Hiện thực hóa UJ-2, UJ-3, UJ-5.

#### FR-34: Trạng thái đơn cho khách hàng

Khách hàng có thể xem trạng thái và dòng thời gian của đơn hàng.

**Hệ quả kiểm thử được:**
- Trang đơn hiển thị mã đơn, trạng thái hiện tại, dòng thời gian sự kiện, danh sách mặt hàng, tổng tiền và hình thức nhận hàng.
- Trang tự làm mới mỗi 30 giây hoặc có nút làm mới.
- Sau `HOAN_TAT`, trang hiển thị nút "Đặt lại".
- Khách hàng chỉ xem được đơn của mình.

#### FR-35: Thông báo đơn hàng

Hệ thống thông báo cho khách hàng và chủ tiệm khi đơn hàng thay đổi trạng thái quan trọng.

**Hệ quả kiểm thử được:**
- Chủ tiệm nhận thông báo trong ứng dụng và email khi có đơn mới.
- Khách hàng nhận email khi đặt thành công, khi đơn được xác nhận, khi bị hủy và khi hoàn tất.
- Nếu gửi email lỗi, hệ thống ghi log và không chặn giao dịch.
- Không gửi SMS hoặc Zalo trong v1. `[GIẢ ĐỊNH: Email giao dịch là kênh thông báo chính; hệ thống có SMTP cấu hình được.]`

#### FR-36: Chính sách giữ đơn nhận tại tiệm

Đơn nhận tại tiệm được giữ trong một khoảng thời gian sau giờ hẹn.

**Hệ quả kiểm thử được:**
- Thời gian giữ mặc định là 2 giờ sau giờ hẹn và có thể cấu hình. `[GIẢ ĐỊNH: Giữ đơn nhận tại tiệm 2 giờ sau giờ hẹn.]`
- Quá hạn, chủ tiệm có thể đánh dấu `KHONG_NHAN`; hệ thống hoàn tồn kho và thông báo khách hàng.
- Trước khi hết hạn 30 phút, khách hàng nhận nhắc nhở qua email.
- `KHONG_NHAN` không áp dụng cho đơn giao hàng.

## 5. Yêu cầu phi chức năng xuyên suốt

- **NFR-1 Hiệu năng:** tìm cửa hàng gần p95 ≤ 800 ms với 500 cửa hàng; tải trang cửa hàng p95 ≤ 1,5 s trên 4G; lưu một mặt hàng ≤ 1 s; checkout p95 ≤ 2 s.
- **NFR-2 Sẵn sàng:** 99,5% trong khung 6:00–22:00 hằng ngày suốt pilot; bảo trì ngoài khung giờ này.
- **NFR-3 Bảo mật:** kiểm soát truy cập ở mọi đường dẫn và điểm cuối API; chống CSRF cho form; cookie phiên `HttpOnly`/`Secure`; giới hạn tần suất đăng nhập và điểm cuối tra cứu mã vạch.
- **NFR-4 Quyền riêng tư:** chỉ lấy vị trí sau khi khách đồng ý; không hiển thị toạ độ chính xác của khách cho cửa hàng; log truy vấn vị trí giữ tối đa 30 ngày; đơn hàng giữ 24 tháng.
- **NFR-5 Tương thích:** iOS Safari 16+ và Chrome Android 110+; bố cục dùng được ở chiều rộng 360 px; camera yêu cầu HTTPS. `[GIẢ ĐỊNH: Khách hàng dùng điện thoại tầm trung Android hoặc iPhone trong 3 năm gần đây.]`
- **NFR-6 Khả năng tiếp cận:** luồng khách hàng đạt WCAG 2.1 AA; vùng chạm tối thiểu 44×44 px; độ tương phản đạt chuẩn; form có nhãn và thông báo lỗi rõ ràng.
- **NFR-7 Quan sát:** log có cấu trúc kèm request ID; theo dõi tỷ lệ quét thành công, tỷ lệ tra cứu trúng, thời gian chuyển trạng thái và lỗi gửi email.
- **NFR-8 Toàn vẹn dữ liệu:** bản chụp giá tại thời điểm đặt; tồn kho không âm; tạo đơn idempotent; mọi chuyển trạng thái ghi nhật ký thay đổi.
- **NFR-9 Ngôn ngữ và định dạng:** tiếng Việt, UTF-8, tiền tệ VND, múi giờ `Asia/Ho_Chi_Minh`.
- **NFR-10 Chịu lỗi:** tra cứu mã vạch lỗi hoặc quá 3 giây thì chuyển sang nhập tay; lỗi email không chặn đơn; mất mạng khi quét hiển thị thử lại và không mất dữ liệu đã nhập.

## 6. Ràng buộc và bảo vệ

- **Danh tính:** Auth0 là nhà cung cấp danh tính duy nhất; Google là connection duy nhất trong v1. Vai trò nghiệp vụ lưu trong hệ thống, không phụ thuộc hoàn toàn vào claim của Auth0.
- **Thanh toán:** chỉ tiền mặt; không xử lý dữ liệu thẻ, không tích hợp cổng thanh toán.
- **Thiết bị:** web responsive; camera trình duyệt; không có app native, không có chế độ offline.
- **Dữ liệu mã vạch:** tra cứu từ CSDL hàng hóa mở có thể thiếu hoặc sai; chủ tiệm luôn có quyền sửa tên và giá. Cần ghi nguồn dữ liệu và tôn trọng điều khoản của nguồn.
- **Vị trí:** chỉ dùng cho khám phá và kiểm tra bán kính; không dùng cho quảng cáo hoặc chia sẻ với bên thứ ba.
- **Vận hành pilot:** cửa hàng được onboard thủ công bởi đội vận hành; không có công cụ quản trị tự phục vụ trong v1. `[GIẢ ĐỊNH: Cửa hàng pilot được onboard thủ công, không cần quy trình tự đăng ký công khai.]` `[GIẢ ĐỊNH: Không có công cụ quản trị trong v1; đội vận hành dùng thao tác trực tiếp.]`

## 7. Kiến trúc thông tin

| Khu vực | Đường dẫn | Người dùng | Mục đích |
|---|---|---|---|
| Công khai | `/` | Khách ẩn danh | Giới thiệu, chọn vị trí, danh sách cửa hàng gần |
| Công khai | `/stores/{id}` | Khách ẩn danh | Trang cửa hàng và danh mục hàng |
| Công khai | `/search` | Khách ẩn danh | Tìm cửa hàng hoặc mặt hàng trong khu vực |
| Khách hàng | `/cart` | Khách ẩn danh/đã đăng nhập | Giỏ hàng |
| Khách hàng | `/checkout` | Khách đã đăng nhập | Xác nhận và đặt hàng |
| Khách hàng | `/orders`, `/orders/{id}` | Khách đã đăng nhập | Lịch sử và trạng thái đơn |
| Chủ tiệm | `/store` | Chủ tiệm | Hồ sơ và trạng thái cửa hàng |
| Chủ tiệm | `/store/catalog` | Chủ tiệm | Danh mục hàng |
| Chủ tiệm | `/store/scan` | Chủ tiệm | Phiên quét tạo/cập nhật mặt hàng |
| Chủ tiệm | `/store/orders` | Chủ tiệm | Danh sách và xử lý đơn hàng |
| Chủ tiệm | `/pos` | Chủ tiệm | Bán tại quầy |
| Xác thực | `/oauth2/authorization/auth0`, `/logout` | Tất cả | Đăng nhập, đăng xuất |

## 8. Ngoài phạm vi (tường minh)

PRD này không biến sản phẩm thành:

- **Sàn thương mại điện tử đa ngành:** không bán ngoài tạp hóa, không mở cho chủ tiệm ngoài khu vực pilot.
- **Hệ thống POS/ERP đầy đủ:** không có kiểm kê, nhập kho, công nợ, quản lý nhà cung cấp hoặc ca làm việc.
- **Nền tảng logistics:** không điều phối shipper, không tích hợp đối tác giao hàng, không tối ưu tuyến.
- **Công cụ tăng trưởng:** không loyalty, voucher, quảng cáo, thuê bao hoặc hoa hồng.
- **Cổng thanh toán:** không xử lý thẻ, ví, chuyển khoản hoặc hoàn tiền tự động.
- **Ứng dụng native/PWA:** không phát hành app store, không hỗ trợ offline.
- **Hệ thống quản trị doanh nghiệp:** không có phân quyền chi tiết, SSO doanh nghiệp hoặc báo cáo tài chính.
- **Sản phẩm đa ngôn ngữ/đa tiền tệ:** chỉ tiếng Việt và VND trong v1.

Danh sách deferral cụ thể của MVP nằm ở §9.2.

## 9. Phạm vi MVP

### 9.1 Trong phạm vi

- Đăng nhập Google qua Auth0; phân quyền khách hàng/chủ tiệm; xem ẩn danh.
- Hồ sơ cửa hàng, bán kính phục vụ, giờ mở cửa, phí giao hàng, đơn tối thiểu.
- Quét mã vạch, tra cứu và điền sẵn, tạo/cập nhật mặt hàng, quản lý danh mục hàng.
- Khám phá cửa hàng gần theo vị trí hoặc phường; tìm kiếm trong khu vực.
- Giỏ hàng, checkout, giao hàng hoặc nhận tại tiệm, thanh toán tiền mặt.
- Quản lý đơn hàng, soạn hàng bằng quét, xử lý thiếu hàng, máy trạng thái.
- POS tại quầy dùng chung danh mục hàng.
- Trạng thái đơn, email thông báo, chính sách giữ đơn nhận tại tiệm.
- NFR và bảo vệ dữ liệu ở §5 và §6.

### 9.2 Ngoài phạm vi MVP

- Thanh toán online và hoàn tiền tự động — lý do: chưa có nhu cầu đã kiểm chứng và làm tăng phạm vi tuân thủ.
- App native — lý do: web mobile đủ cho pilot và giảm chi phí.
- Điều phối giao hàng — lý do: cửa hàng tự giao trong bán kính ngắn.
- Tài khoản nhân viên — lý do: pilot chủ yếu tiệm một người; `[GHI CHÚ CHO PM]` xem lại nếu cửa hàng đông khách.
- Bảng quản trị — lý do: đội vận hành có thể thao tác trực tiếp trong pilot.
- Đa ngôn ngữ — lý do: thị trường pilot chỉ tiếng Việt.
- Tích hợp Zalo/SMS — lý do: email và thông báo trong ứng dụng đủ cho v1.

## 10. Chỉ số thành công

**Chính**

- **SM-1 Kích hoạt cửa hàng:** ≥ 70% cửa hàng đăng ký đăng ≥ 20 mặt hàng trong 24 giờ. Kiểm chứng FR-5, FR-8, FR-10, FR-11.
- **SM-2 Tỷ lệ tra cứu mã vạch:** ≥ 80% mã quét ra tên mà không phải nhập tay. Kiểm chứng FR-9.
- **SM-3 Tốc độ quét tại quầy:** thêm một mặt hàng vào hóa đơn dưới 2 giây trên điện thoại tầm trung. Kiểm chứng FR-8, FR-31.
- **SM-4 Độ chính xác đơn hàng:** ≥ 95% đơn online giao đủ và đúng. Kiểm chứng FR-26, FR-27.
- **SM-5 Nhu cầu lặp lại:** ≥ 30% khách hàng đặt đơn thứ hai trong 30 ngày. Kiểm chứng FR-20, FR-23.

**Phụ**

- **SM-6 Thời gian xác nhận đơn:** trung vị ≤ 10 phút từ lúc đặt tới lúc cửa hàng xác nhận. Kiểm chứng FR-24, FR-25.
- **SM-7 Tỷ lệ hủy do thiếu hàng:** ≤ 10% đơn online. Kiểm chứng FR-27.
- **SM-8 Tỷ lệ giao hoàn tất trong ngày:** ≥ 90% đơn giao hàng hoàn tất trong ngày đặt. Kiểm chứng FR-28.

**Chỉ số đối trọng**

- **SM-C1 Số cửa hàng onboard so với độ chính xác danh mục:** không tăng số cửa hàng nếu tỷ lệ mặt hàng có giá/tên đúng giảm. Đối trọng SM-1.
- **SM-C2 Tốc độ quét so với tỷ lệ sửa tay:** không tối ưu tốc độ nếu chủ tiệm phải sửa lại thông tin tra cứu quá 20% số lần. Đối trọng SM-2, SM-3.
- **SM-C3 Tăng trưởng đơn so với tỷ lệ hủy/hoàn:** không đẩy đơn nếu tỷ lệ hủy do thiếu hàng vượt 15%. Đối trọng SM-5, SM-7.

## 11. Câu hỏi mở

1. **Thanh toán ngoài tiền mặt:** khi nào bổ sung chuyển khoản/ví? Điều kiện mở lại: có ≥ 100 đơn lặp lại/tháng và tỷ lệ khách không đến nhận > 5%.
2. **Chính sách giữ đơn nhận tại tiệm:** 2 giờ sau giờ hẹn có phù hợp với thói quen khách Việt Nam không? Cần đo tỷ lệ `KHONG_NHAN` trong 4 tuần đầu.
3. **Phí giao hàng:** cửa hàng tự đặt phí cố định hay tính theo khoảng cách? v1 dùng phí cố định; xem lại khi có dữ liệu.
4. **Xác minh cửa hàng:** có cần giấy phép kinh doanh hoặc xác minh địa chỉ trong pilot? Hiện tại onboard thủ công bởi đội vận hành.
5. **Chất lượng dữ liệu mã vạch:** nguồn mở có đủ cho hàng Việt không? Cần đo trên 500 mã thực tế trước khi cam kết SM-2.
6. **Quyền riêng tư vị trí:** có cần xin sự đồng ý riêng ngoài hộp thoại trình duyệt không? Tư vấn pháp lý trước khi mở rộng ngoài pilot.
7. **Đơn hàng nhận tại tiệm có cần thanh toán trước không?** v1 không; theo dõi tỷ lệ khách không đến nhận.
8. **Một người dùng có thể mở nhiều cửa hàng?** v1 không; xem lại nếu có nhu cầu từ chuỗi nhỏ.
9. **Kênh thông báo thay thế:** Zalo OA hoặc SMS có cần cho nhóm khách không dùng email? Đánh giá sau pilot.
10. **Đo lường tồn kho:** có cần trường "không theo dõi tồn" thay vì bắt buộc nhập số? Theo dõi tỷ lệ chủ tiệm bỏ trống trường này.

## 12. Chỉ mục giả định

- `[GIẢ ĐỊNH: Một chủ tiệm chỉ có một cửa hàng trong v1.]` — §4.2 FR-5.
- `[GIẢ ĐỊNH: Mọi mặt hàng đều có mã vạch; không hỗ trợ mặt hàng bán theo cân không mã.]` — §4.3 FR-13. Cần xác nhận vì rau củ và hàng đong có thể không có mã.
- `[GIẢ ĐỊNH: Phí giao hàng là số tiền cố định do cửa hàng đặt, không tính theo khoảng cách.]` — §4.5 FR-18.
- `[GIẢ ĐỊNH: Giá trị đơn tối thiểu mặc định 0 và do cửa hàng tùy chọn.]` — §4.2 FR-6.
- `[GIẢ ĐỊNH: Giữ đơn nhận tại tiệm 2 giờ sau giờ hẹn.]` — §4.8 FR-36.
- `[GIẢ ĐỊNH: Email giao dịch là kênh thông báo chính; hệ thống có SMTP cấu hình được.]` — §4.8 FR-35.
- `[GIẢ ĐỊNH: Cửa hàng pilot được onboard thủ công, không cần quy trình tự đăng ký công khai.]` — §6.
- `[GIẢ ĐỊNH: Khách hàng dùng điện thoại tầm trung Android hoặc iPhone trong 3 năm gần đây.]` — §5 NFR-5.
- `[GIẢ ĐỊNH: Không có công cụ quản trị trong v1; đội vận hành dùng thao tác trực tiếp.]` — §6.
- `[GIẢ ĐỊNH: Đơn tại quầy không cần gắn với khách hàng đã đăng ký.]` — §4.7.
