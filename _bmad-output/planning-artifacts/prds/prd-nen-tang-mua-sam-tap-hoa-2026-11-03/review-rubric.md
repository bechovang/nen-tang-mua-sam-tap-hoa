# Đánh giá chất lượng PRD — Nền tảng mua sắm tạp hóa khu phố

## Kết luận tổng thể

PRD đủ chắc để UX, kiến trúc và story bắt đầu: luận điểm barcode-first xuyên suốt, 36 FR đều có hệ quả kiểm thử, 6 hành trình có nhân vật cụ thể, phạm vi và non-goals rõ ràng. Rủi ro còn lại nằm ở các giả định vận hành — độ phủ mã vạch hàng Việt, một cửa hàng mỗi chủ tiệm, giữ đơn 2 giờ — chứ không ở cấu trúc yêu cầu.

## 1. Decision-readiness — strong

Các quyết định lớn được nêu thành quyết định, không bị chôn trong "cân nhắc": chỉ tiền mặt, một cửa hàng mỗi chủ tiệm, cửa hàng tự giao, pilot miễn phí, không app native. Đánh đổi được gọi tên — ví dụ tiền mặt giới hạn giá trị giỏ và không có recourse; barcode lookup nhanh nhưng phụ thuộc độ phủ dữ liệu mở. Open Questions có điều kiện mở lại cụ thể.

### Findings

- **[high] Giả định chưa gắn inline, chỉ mục không roundtrip** (§12) — §0 tuyên bố mọi suy luận được đánh dấu `[GIẢ ĐỊNH: …]`, nhưng thân PRD không có tag nào; §12 chỉ liệt kê lại. Người đọc FR không biết đâu là quyết định đã xác nhận, đâu là suy luận. *Đã sửa:* gắn 10 tag inline tại FR-5, FR-6, FR-13, FR-18, FR-35, FR-36, §4.7, §5 NFR-5, §6 và đồng bộ §12.
- **[high] Thiếu yêu cầu địa chỉ giao hàng** (§4.5 FR-18) — FR-18 nói kiểm tra bán kính nhưng không nói địa chỉ giao lấy từ đâu; UJ-2 lại yêu cầu nhập địa chỉ. *Đã sửa:* thêm hệ quả về địa chỉ giao, số điện thoại, toạ độ dùng để kiểm tra bán kính.
- **[medium] Xung đột thời điểm giữ tồn kho** (§4.6 FR-29 và phụ lục) — PRD nói giữ tồn kho khi xác nhận đơn; bảng chuyển trạng thái phụ lục ghi khi bắt đầu soạn. *Đã sửa:* phụ lục chuyển ghi chú giữ tồn kho sang bước `CHO_XAC_NHAN → DA_XAC_NHAN`.
- **[low] NFR-2 99,5% với một container** (§5) — mục tiêu sẵn sàng có thể không đạt nếu triển khai lại giữa giờ pilot. Giữ nguyên có chủ đích: cửa sổ bảo trì ngoài 6:00–22:00 và một container có health check là đủ cho quy mô pilot; ghi nhận để kiến trúc xác nhận.

## 2. Substance over theater — strong

Không có persona thừa: hai vai trò chính đều dẫn tới FR cụ thể. Điểm khác biệt không phóng đại — PRD nói thẳng không có hào kỹ thuật. NFR có ngưỡng đo được thay vì "nhanh, an toàn, ổn định". Không có phần nào tồn tại chỉ để lấp template.

### Findings

- **[low] UJ-3 và UJ-6 ngắn hơn bốn UJ còn lại** (§2.3) — chấp nhận: hai luồng này đơn giản hơn và vẫn có entry/climax/resolution; không cần kéo dài để cân đối hình thức.

## 3. Strategic coherence — strong

Luận điểm rõ: onboarding bằng mã vạch là đòn bẩy kích hoạt, một luồng quét phục vụ cả kệ và quầy, cửa hàng tự giao trong bán kính ngắn. Các feature đều phục vụ luận điểm đó; không có tính năng lạc trục. Success Metrics đo đúng luận điểm (activation, barcode hit rate, scan-to-cart, order accuracy, repeat) thay vì chỉ đo lượt truy cập. Counter-metrics có thật và gắn với rủi ro cụ thể.

### Findings

- **[low] SM-1 gộp kích hoạt và chất lượng danh mục** (§10) — SM-C1 đã bù bằng cách không đánh đổi số cửa hàng lấy độ chính xác; giữ nguyên.

## 4. Done-ness clarity — adequate

Phần lớn FR có 3–5 hệ quả kiểm thử được với ngưỡng cụ thể (thời gian, số lượng, trạng thái, mã lỗi). Đây là điểm mạnh nhất của tài liệu. Một số chỗ vẫn dựa vào "hợp lệ" nhưng đã có ràng buộc định lượng đi kèm.

### Findings

- **[medium] FR-25 cho phép xác nhận đơn khi thiếu tồn kho** (§4.6) — hành vi có chủ đích vì tồn kho best-effort, nhưng cần test rõ: hệ thống phải hiển thị cảnh báo và vẫn cho xác nhận. *Đã sửa:* câu hệ quả nêu rõ "cảnh báo nếu tồn kho không đủ nhưng vẫn cho xác nhận".
- **[medium] FR-28 không nêu hành vi khi chuyển trạng thái đồng thời** (§4.6) — hai người bấm cùng lúc có thể ghi đè. *Đề xuất cho kiến trúc:* khóa lạc quan hoặc kiểm tra trạng thái hiện tại trong transaction; bổ sung vào phụ lục khi chốt.
- **[low] FR-16 không định nghĩa thứ hạng tìm kiếm** (§4.4) — chấp nhận cho v1: kết quả theo khoảng cách, không cần xếp hạng liên quan.

## 5. Scope honesty — strong

Non-goals rõ và rộng; §9.2 giải thích lý do deferral; §11 có 10 câu hỏi mở thực sự mở; §12 tổng hợp giả định. Không có tính năng nào bị giấu ở dạng "sẽ làm sau" mà không ghi.

### Findings

- **[medium] §8 và §9.2 từng trùng nhau** — cả hai liệt kê cùng các mục out-of-scope. *Đã sửa:* §8 rút về tuyên bố "sản phẩm không trở thành gì", §9.2 giữ danh sách deferral cụ thể kèm lý do.

## 6. Downstream usability — strong

Glossary đầy đủ; FR/UJ/SM ID liên tục và tham chiếu chéo giải quyết được; mỗi UJ có nhân vật và bối cảnh; §7 ánh xạ bề mặt màn hình. Kiến trúc có thể lấy FR làm đầu vào trực tiếp, UX có thể lấy UJ.

### Findings

- **[medium] Glossary drift "tiệm" và "cửa hàng"** (§1–§4) — "tiệm" xuất hiện khắp narrative nhưng không có trong Glossary. *Đã sửa:* thêm mục "Tiệm — cách gọi ngắn của cửa hàng", không phải thực thể khác.
- **[low] "POS" chưa được định nghĩa** (§3, §4.7) — *Đã sửa:* thêm "POS tại quầy" vào Glossary.
- **[low] Đường dẫn brief sai một cấp** (§0) — `../briefs/...` trỏ vào `prds/briefs`. *Đã sửa:* `../../briefs/...`.
- **[low] Thuật ngữ tiếng Anh rải rác** (bill, dashboard, badge, modal, snapshot, timeout, transaction, route, consent, no-show) — *Đã sửa:* Việt hóa nhất quán; giữ thuật ngữ kỹ thuật chuẩn (idempotent, CSRF, HTTPS, WCAG).

## 7. Shape fit — strong

Đây là sản phẩm tiêu dùng nhiều vai trò, có UX quan trọng và feed trực tiếp vào UX/kiến trúc/story, nên UJ có nhân vật là xương sống — đúng hình. PRD không bị over-formalize kiểu enterprise (không có stakeholder matrix, ROI, SLA on-call) và cũng không under-formalize (có NFR, state machine, scope). Độ dài gần 7.900 từ là hợp lý cho một pilot cần chốt hành vi trước khi build.

## Ghi chú cơ học

- FR-1 đến FR-36 liên tục, không trùng; UJ-1 đến UJ-6; SM-1 đến SM-8 và SM-C1 đến SM-C3.
- Tham chiếu chéo FR/UJ/SM đều trỏ tới ID tồn tại.
- Chỉ mục giả định đã roundtrip: 10 tag inline ↔ 10 mục §12.
- §7 dùng "Đường dẫn" thay cho "Route"; Glossary dùng "Tiệm" và "POS tại quầy".
- Không còn thuật ngữ drift đã liệt kê ở trên.

## Đã sửa trong lượt này

1. Gắn 10 giả định inline và đồng bộ chỉ mục.
2. Bổ sung yêu cầu địa chỉ giao hàng và kiểm tra bán kính theo vị trí giao.
3. Sửa xung đột thời điểm giữ tồn kho giữa PRD và phụ lục.
4. Thêm loại đơn `TAI_QUAY` và cờ `da_thu_tien` vào mô hình dữ liệu.
5. Rút gọn §8 để không lặp §9.2.
6. Việt hóa thuật ngữ UI và sửa đường dẫn brief.
7. Làm rõ khung giờ nhận tại tiệm, SM-8 theo ngày thay vì khung giờ giao không tồn tại.
