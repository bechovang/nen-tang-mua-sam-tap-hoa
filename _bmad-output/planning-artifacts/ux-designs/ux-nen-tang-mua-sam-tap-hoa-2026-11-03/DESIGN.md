---
name: Tạp Hóa Gần
description: Giao diện ấm, rõ và nhanh cho chợ tạp hóa khu phố — phone-first, tiếng Việt, tối ưu cho một tay và ánh sáng ngoài trời.
status: final
sources:
  - ../../prds/prd-nen-tang-mua-sam-tap-hoa-2026-11-03/prd.md
  - ../../briefs/brief-nen-tang-mua-sam-tap-hoa-2026-11-03/brief.md
updated: 2026-11-03
colors:
  surface-base: '#FAF6EE'
  surface-raised: '#FFFFFF'
  surface-sunken: '#F1EADF'
  ink-primary: '#1E241F'
  ink-secondary: '#5B665C'
  ink-muted: '#677167'
  border-hairline: '#E2DACB'
  primary: '#1F6B4F'
  on-primary: '#FFFFFF'
  primary-hover: '#18543E'
  primary-soft: '#E3F0E8'
  accent: '#D98A2B'
  accent-ink: '#8A4E0F'
  on-accent: '#241505'
  accent-soft: '#FBEBD5'
  success: '#276A4C'
  success-soft: '#E4F3EA'
  warning: '#8A5514'
  warning-soft: '#F8ECD8'
  danger: '#B3402F'
  on-danger: '#FFFFFF'
  danger-soft: '#FBE4E0'
  focus-ring: '#1F6B4F'
  surface-base-dark: '#141814'
  surface-raised-dark: '#1D231E'
  surface-sunken-dark: '#101410'
  ink-primary-dark: '#F3F0E7'
  ink-secondary-dark: '#B9C2B8'
  ink-muted-dark: '#7F8A80'
  border-hairline-dark: '#2E362F'
  primary-dark: '#7BC4A3'
  on-primary-dark: '#0C1F16'
  primary-soft-dark: '#1B2E24'
  accent-dark: '#F0B15E'
  accent-ink-dark: '#F7C98A'
  accent-soft-dark: '#3A2A16'
  success-dark: '#7FD3A9'
  success-soft-dark: '#163026'
  warning-dark: '#E6B36A'
  warning-soft-dark: '#33240F'
  danger-dark: '#F09A8C'
  danger-soft-dark: '#351A17'
typography:
  display-lg:
    fontFamily: 'Lora'
    fontSize: 40px
    fontWeight: '600'
    lineHeight: '1.12'
    letterSpacing: -0.01em
  display-md:
    fontFamily: 'Lora'
    fontSize: 32px
    fontWeight: '600'
    lineHeight: '1.18'
  headline:
    fontFamily: 'Lora'
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.25'
  title:
    fontFamily: 'Be Vietnam Pro'
    fontSize: 20px
    fontWeight: '600'
    lineHeight: '1.3'
  body:
    fontFamily: 'Be Vietnam Pro'
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.55'
  body-sm:
    fontFamily: 'Be Vietnam Pro'
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.5'
  label:
    fontFamily: 'Be Vietnam Pro'
    fontSize: 13px
    fontWeight: '600'
    lineHeight: '1.4'
  caption:
    fontFamily: 'Be Vietnam Pro'
    fontSize: 12px
    fontWeight: '400'
    lineHeight: '1.4'
  price:
    fontFamily: 'Be Vietnam Pro'
    fontSize: 18px
    fontWeight: '700'
    lineHeight: '1.2'
    letterSpacing: 0
rounded:
  sm: 8px
  md: 12px
  lg: 16px
  xl: 24px
  full: 9999px
spacing:
  '1': 4px
  '2': 8px
  '3': 12px
  '4': 16px
  '5': 20px
  '6': 24px
  '8': 32px
  '10': 40px
  '12': 48px
  margin-mobile: 16px
  gutter-desktop: 24px
  bottom-nav-height: 64px
components:
  button-primary:
    background: '{colors.primary}'
    foreground: '{colors.on-primary}'
    radius: '{rounded.md}'
    height: 48px
    padding-inline: 20px
  button-secondary:
    background: '{colors.surface-raised}'
    foreground: '{colors.ink-primary}'
    border: '{colors.border-hairline}'
    radius: '{rounded.md}'
    height: 48px
  button-ghost:
    background: transparent
    foreground: '{colors.primary}'
    radius: '{rounded.md}'
    height: 44px
  input:
    background: '{colors.surface-raised}'
    foreground: '{colors.ink-primary}'
    border: '{colors.border-hairline}'
    focus-border: '{colors.focus-ring}'
    radius: '{rounded.md}'
    height: 48px
  card-store:
    background: '{colors.surface-raised}'
    foreground: '{colors.ink-primary}'
    border: '{colors.border-hairline}'
    radius: '{rounded.lg}'
    padding: '{spacing.4}'
  card-product:
    background: '{colors.surface-raised}'
    foreground: '{colors.ink-primary}'
    border: '{colors.border-hairline}'
    radius: '{rounded.md}'
    padding: '{spacing.3}'
  status-chip:
    background: '{colors.primary-soft}'
    foreground: '{colors.primary}'
    radius: '{rounded.full}'
    padding: 6px 10px
  price:
    foreground: '{colors.accent-ink}'
    typography: '{typography.price}'
  bottom-nav:
    background: '{colors.surface-raised}'
    border-top: '{colors.border-hairline}'
    height: '{spacing.bottom-nav-height}'
  scan-frame:
    border: '{colors.accent}'
    radius: '{rounded.xl}'
  toast:
    background: '{colors.ink-primary}'
    foreground: '{colors.surface-base}'
    radius: '{rounded.md}'
  fab-scan:
    background: '{colors.primary}'
    foreground: '{colors.on-primary}'
    radius: '{rounded.full}'
    size: 56px
  quantity-stepper:
    background: '{colors.surface-raised}'
    border: '{colors.border-hairline}'
    radius: '{rounded.md}'
    touch-target: 44px
---

## Brand & Style

Tạp Hóa Gần trông như một tiệm tạp hóa quen được sắp lại gọn gàng, không phải một app giao hàng hào nhoáng. Nền giấy ấm, mực đậm, một màu xanh rau củ cho hành động chính và một màu cam nghệ cho giá. Chữ Việt rõ, số tiền đọc được trong một giây, khoảng thở đủ để không rối.

Tính cách: **thân thiện nhưng không trẻ con; đáng tin nhưng không quan liêu; nhanh nhưng không vội**. Chủ tiệm đang đứng ở quầy, một tay cầm điện thoại, một tay cầm hàng — giao diện phải phục vụ tư thế đó trước. Khách hàng đang đi bộ hoặc ngồi trong chung cư — thông tin phải trả lời được "tiệm này có món mình cần không, giá bao nhiêu, bao xa" mà không cần mở thêm trang.

Bootstrap 5.3 là nền kỹ thuật. Tài liệu này chỉ định nghĩa **brand layer**: token màu, typography, bo góc, nhịp và các component mang dấu ấn sản phẩm. Component nào Bootstrap làm đúng thì giữ nguyên; không viết lại để trông khác.

Palette và typography dưới đây là **đề xuất v1**, chưa phải brand guideline đã chốt. Token được đặt tên theo vai trò để có thể thay giá trị mà không phải sửa component.

Ba nguyên tắc thị giác:

1. **Một hành động chính mỗi màn hình.** Màu xanh chỉ dành cho hành động đó; không cạnh tranh bằng nhiều nút nổi.
2. **Trạng thái bằng chữ trước, màu sau.** "Còn hàng", "Hết hàng", "Đã xác nhận" luôn có nhãn; màu chỉ hỗ trợ.
3. **Ấm nhưng có kỷ luật.** Bo góc mềm, viền mảnh, bóng gần như không có. Không gradient, không hiệu ứng trang trí.

## Colors

Palette lấy từ vật liệu của chợ: giấy gói, lá rau, nghệ, gạch nung. Tất cả cặp màu chữ/nền chính đạt tương phản tối thiểu 4,5:1; nhãn phụ và viền không dùng để truyền tải thông tin một mình.

- **Xanh Rau (`{colors.primary}`)** — màu hành động: nút chính, tab đang chọn, liên kết, viền focus. Không dùng cho trạng thái thành công (đã có `success`) và không dùng làm nền chữ dài.
- **Cam Nghệ (`{colors.accent}`)** — điểm nhấn: khung quét, huy hiệu số đếm, gạch chân mục đang xem. Không dùng làm nền nút chính.
- **Cam Nghệ Đậm (`{colors.accent-ink}`)** — màu chữ cho giá bán. Đây là ngoại lệ có chủ đích: giá là thông tin quan trọng nhất trên thẻ hàng, và cam đậm đọc rõ trên nền giấy hơn cam tươi.
- **Giấy Ấm (`{colors.surface-base}`)** — nền toàn cục. Không dùng trắng tinh làm nền trang để giảm chói khi đứng ngoài trời.
- **Trắng Nổi (`{colors.surface-raised}`)** — thẻ, sheet, thanh điều hướng. Tách khỏi nền bằng viền mảnh, không bằng bóng.
- **Mực (`{colors.ink-primary}` / `{colors.ink-secondary}`)** — chữ chính và chữ phụ. `ink-muted` chỉ dùng cho metadata không quan trọng.
- **Xanh Thành Công (`{colors.success}`)** — "Đã xác nhận", "Đã giao", "Còn hàng". Luôn đi kèm nhãn chữ.
- **Nghệ Cảnh Báo (`{colors.warning}`)** — "Sắp hết", "Chờ xác nhận", "Đơn quá 15 phút".
- **Gạch Lỗi (`{colors.danger}`)** — "Hết hàng", "Đã hủy", lỗi biểu mẫu. Không dùng cho giá hoặc khuyến mãi.

Dark mode là tùy chọn, không phải mặc định. Token `*-dark` giữ cùng vai trò, không đảo ngược ngữ nghĩa. Không dùng màu để mã hóa danh mục hàng.

## Typography

Hai họ chữ, hai nhiệm vụ:

- **Lora** — giọng của tiệm: tên cửa hàng, tiêu đề onboarding, trạng thái trống, khoảnh khắc "đã xong". Dùng tiết chế; không đặt toàn bộ giao diện trong serif.
- **Be Vietnam Pro** — giọng làm việc: nhãn, nút, danh sách, giá, biểu mẫu. Hỗ trợ tiếng Việt đầy đủ, đọc tốt ở cỡ nhỏ.

Ramp: `{typography.display-lg}` cho màn hình chào/onboarding; `{typography.display-md}` cho tiêu đề trang; `{typography.headline}` cho tên cửa hàng; `{typography.title}` cho tiêu đề khối; `{typography.body}` cho nội dung; `{typography.body-sm}` cho mô tả phụ; `{typography.label}` cho nhãn và nút; `{typography.caption}` cho metadata; `{typography.price}` cho giá.

Quy tắc:

- Giá dùng `{typography.price}` với `font-variant-numeric: tabular-nums` để các con số thẳng cột.
- Không dùng chữ in hoa toàn bộ cho tiêu đề tiếng Việt; dấu thanh làm chữ khó đọc. Nhãn nhỏ có thể viết hoa và giãn chữ 0,06em.
- Không xuống dưới 12px cho bất kỳ chữ nào; nhãn nút tối thiểu 14px.
- Số điện thoại, mã đơn, mã vạch dùng chữ đơn cách (monospace) hoặc tabular; không ngắt dòng giữa mã.

## Layout & Spacing

Thang 4px: `{spacing.1}` đến `{spacing.12}`. Lề mobile `{spacing.margin-mobile}`; khoảng giữa các khối `{spacing.6}`; padding trong thẻ `{spacing.4}`.

- **Mobile (< 768px):** một cột, lề 16px, thanh hành động dính đáy, bottom nav cao `{spacing.bottom-nav-height}` cộng vùng an toàn. Nội dung chính không bị che bởi nav.
- **Tablet (768–1023px):** một cột rộng tối đa 720px cho luồng khách; dashboard tiệm có thể hai cột.
- **Desktop (≥ 1024px):** khách hàng giữ cột tối đa 720px; khu vực tiệm dùng sidebar trái 240px và vùng nội dung tối đa 1200px. POS ưu tiên bố cục ngang: danh sách hóa đơn bên trái, tổng tiền và thanh toán bên phải.
- **Vùng ngón tay cái:** hành động chính nằm trong 1/3 dưới màn hình. Không đặt nút quan trọng ở góc trên phải.
- **Safe area:** bottom nav và thanh CTA cộng `env(safe-area-inset-bottom)`.

## Elevation & Depth

Chiều sâu đến từ **tầng tông màu và viền mảnh**, không từ bóng. Thẻ nổi dùng `{colors.surface-raised}` trên `{colors.surface-base}`, viền `{colors.border-hairline}` 1px. Bóng chỉ xuất hiện ở:

- Thanh CTA dính đáy và bottom nav: bóng mờ hướng lên, rất nhẹ.
- Toast và sheet: bóng mờ để tách khỏi nội dung.
- Không dùng bóng cho thẻ hàng, thẻ cửa hàng hoặc trạng thái hover.

Dark mode dùng cùng logic: nền tối hơn, thẻ sáng hơn một bậc, viền mảnh thay cho bóng.

## Shapes

Bo góc mềm, không bo tròn hoàn toàn cho bề mặt lớn:

- `{rounded.sm}` (8px) — input nhỏ, chip nội bộ.
- `{rounded.md}` (12px) — nút, input, thẻ hàng, stepper.
- `{rounded.lg}` (16px) — thẻ cửa hàng, sheet, khối onboarding.
- `{rounded.xl}` (24px) — khung quét, ảnh hero.
- `{rounded.full}` — chỉ dùng cho chip trạng thái, avatar, nút quét nổi.

Khung quét dùng bốn góc bo `{rounded.xl}` với bracket màu `{colors.accent}` dày 3px; không dùng hình vuông sắc cạnh.

## Components

Mô tả thị giác; hành vi nằm trong `EXPERIENCE.md`.

- **Nút chính** — `{components.button-primary}`. Cao 48px, chữ 16px/600, một nút mỗi màn hình. Trạng thái disabled dùng `surface-sunken` + `ink-muted`; không giảm opacity toàn nút.
- **Nút phụ** — `{components.button-secondary}`. Viền 1px, nền trắng, chữ mực. Dùng cho "Quay lại", "Sửa", "Hủy".
- **Nút chữ** — `{components.button-ghost}`. Chỉ dùng cho hành động nhẹ, không dùng cho hành động phá hủy.
- **Ô nhập** — `{components.input}`. Nhãn phía trên, lỗi phía dưới, viền focus 2px `{colors.focus-ring}`. Không dùng placeholder thay nhãn.
- **Thẻ cửa hàng** — `{components.card-store}`. Tên tiệm dùng `{typography.headline}`; khoảng cách và trạng thái mở dùng `{typography.caption}`; chip giao/nhận dùng `status-chip`.
- **Thẻ hàng** — `{components.card-product}`. Tên 16px, đơn vị 13px, giá `{components.price}`. Nút thêm nằm bên phải, cao 44px.
- **Chip trạng thái** — `{components.status-chip}`. Nền `*-soft`, chữ màu tương ứng, bo full. Luôn có nhãn chữ; không chỉ chấm màu.
- **Bottom nav** — `{components.bottom-nav}`. Icon 24px, nhãn 11px/600. Mục đang chọn dùng `{colors.primary}`; badge số đếm dùng `{colors.accent}` với chữ `{colors.on-accent}`.
- **Khung quét** — `{components.scan-frame}`. Nền tối mờ, khung giữa 70% chiều rộng, tỷ lệ 1:1, bracket cam. Nút đèn pin và "Nhập mã" nằm trong vùng ngón tay cái.
- **Toast** — `{components.toast}`. Tối đa 2 dòng, tự ẩn sau 4 giây, có nút đóng.
- **Nút quét nổi** — `{components.fab-scan}`. Chỉ dùng trong khu vực tiệm, đặt giữa bottom nav hoặc góc dưới phải; không dùng đồng thời với bottom nav trùng chức năng.
- **Stepper số lượng** — `{components.quantity-stepper}`. Hai nút 44px, số ở giữa, không dùng input số thô.

## Do's and Don'ts

| Do | Don't |
|---|---|
| Dùng một màu hành động chính (`{colors.primary}`) cho mỗi màn hình | Dùng xanh cho cả nút, badge, link và trạng thái — mất tín hiệu |
| Đặt giá bằng `{colors.accent-ink}` và tabular-nums | Dùng đỏ cho giá hoặc dùng màu để mã hóa danh mục |
| Luôn viết nhãn trạng thái bằng tiếng Việt | Chỉ dùng chấm màu hoặc icon để báo "hết hàng" |
| Giữ nền giấy ấm, viền mảnh, bóng tối thiểu | Gradient, glassmorphism, bóng đổ nặng |
| Bo góc 12–16px cho bề mặt tương tác | Bo tròn hoàn toàn mọi thứ thành pill |
| Dùng Lora cho khoảnh khắc thương hiệu | Đặt toàn bộ body hoặc bảng trong Lora |
| Giữ chữ tối thiểu 12px, nhãn nút 14px | Chữ 10–11px để nhồi thông tin |
| Tôn trọng vùng an toàn và ngón tay cái | Đặt CTA chính ở góc trên màn hình |
