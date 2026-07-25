## Nhóm 1: Phản hồi thông tin (1xx)
* **100 Continue:** Máy chủ đã nhận phần đầu của yêu cầu và khách hàng có thể tiếp tục gửi phần tiếp theo.
* **101 Switching Protocols:** Máy chủ đồng ý chuyển đổi giao thức theo yêu cầu của máy chủ khách.

## Nhóm 2: Thành công (2xx)
* **200 OK (★):** Yêu cầu thành công, đây là mã phổ biến nhất cho các trang web tải bình thường.
* **201 Created (★):** Yêu cầu thành công và một tài nguyên mới vừa được tạo (thường dùng trong API POST).
* **204 No Content (★):** Yêu cầu thành công nhưng không có nội dung nào trả về cho phía client.

## Nhóm 3: Chuyển hướng (3xx)
* **301 Moved Permanently (★):** Tài nguyên đã chuyển hướng vĩnh viễn sang địa chỉ URL mới.
* **302 Found / Moved Temporarily (★):** Tài nguyên tạm thời chuyển hướng sang địa chỉ URL khác.
* **304 Not Modified (★):** Dữ liệu không thay đổi so với bản lưu trong bộ nhớ đệm (cache), giúp tiết kiệm băng thông.

## Nhóm 4: Lỗi từ phía người dùng (4xx)
* **400 Bad Request (★):** Yêu cầu không hợp lệ, cú pháp bị sai hoặc không thể xử lý.
* **401 Unauthorized (★):** Cần xác thực danh tính (đăng nhập) mới được truy cập.
* **403 Forbidden (★):** Máy chủ hiểu yêu cầu nhưng từ chối cấp quyền truy cập.
* **404 Not Found (★):** Không tìm thấy trang hoặc tài nguyên được yêu cầu.
* **405 Method Not Allowed:** Phương thức HTTP (GET, POST...) không được phép sử dụng cho tài nguyên đó.
* **429 Too Many Requests:** Gửi quá nhiều yêu cầu trong một khoảng thời gian ngắn (chống cạn kiệt tài nguyên).

## Nhóm 5: Lỗi từ phía máy chủ (5xx)
* **500 Internal Server Error (★):** Lỗi nội bộ bất ngờ xảy ra ở phía máy chủ.
* **502 Bad Gateway:** Máy chủ nhận được phản hồi sai từ một máy chủ khác khi làm trung gian.
* **503 Service Unavailable (★):** Máy chủ đang quá tải hoặc đang bảo trì tạm thời.
* **504 Gateway Timeout:** Máy chủ trung gian không nhận được phản hồi kịp thời từ máy chủ chính.