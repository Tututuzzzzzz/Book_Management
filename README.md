# 📚 Library Management System (Java Socket)

### **Mô tả tổng quan**
Xây dựng một hệ thống quản lý thư viện cơ bản bao gồm các chức năng chính như thêm sách, tìm kiếm sách, hiển thị danh sách sách theo thể loại, và quản lý dữ liệu sách từ xa. Hệ thống sử dụng **Socket** để giao tiếp giữa **client** và **server**, hỗ trợ nhiều **client** đồng thời kết nối tới server.

---

### **Yêu cầu chi tiết**

1. **Hệ thống bao gồm hai thành phần chính:**
   - **Server**:
     - Quản lý danh sách sách trong thư viện.
     - Xử lý các yêu cầu từ client.
     - Gửi kết quả trả về cho client.
   - **Client**:
     - Gửi yêu cầu tới server (thêm sách, tìm kiếm sách, hiển thị danh sách...).
     - Hiển thị kết quả nhận được từ server.

2. **Các chức năng chính của hệ thống:**
   - **Thêm sách vào thư viện**:
     - Nhập thông tin sách: ID, tiêu đề, tác giả, thể loại, giá.
     - Gửi thông tin sách tới server để thêm vào danh sách quản lý.
   - **Tìm kiếm sách theo tiêu đề**:
     - Nhập từ khóa tìm kiếm.
     - Nhận danh sách các sách có tiêu đề chứa từ khóa từ server.
   - **Hiển thị danh sách sách theo thể loại**:
     - Hiển thị danh sách các sách được nhóm theo từng thể loại.
   - **Thoát**:
     - Đóng kết nối giữa client và server.

3. **Thông tin mỗi cuốn sách**:
   - `ID`: Mã định danh duy nhất của sách.
   - `Title`: Tiêu đề sách.
   - `Author`: Tác giả sách.
   - `Category`: Thể loại sách (ví dụ: Khoa học, Văn học, Lịch sử...).
   - `Price`: Giá sách.

4. **Yêu cầu kỹ thuật:**
   - Sử dụng **Java Core** để xây dựng toàn bộ hệ thống.
   - Sử dụng **Socket** (TCP) để giao tiếp giữa client và server.
   - Dùng **đa luồng** để xử lý nhiều client kết nối tới server.
   - Dữ liệu sách được lưu trữ trong bộ nhớ trên server (có thể mở rộng ra cơ sở dữ liệu).
   - Mã hóa và tuần tự hóa dữ liệu bằng `ObjectInputStream` và `ObjectOutputStream`.

5. **Mở rộng (tùy chọn không bắt buộc):**
   - Thêm chức năng xóa sách hoặc cập nhật thông tin sách.
   - Lưu dữ liệu sách vào file hoặc cơ sở dữ liệu.
   - Sử dụng giao diện đồ họa (Swing hoặc JavaFX) để cải thiện trải nghiệm người dùng.
   - Mã hoá nội dung thông tin khi truyền qua socket sử dụng AES, RSA…

---

### **Luồng hoạt động của hệ thống**

1. **Khởi động server:**
   - Server chạy và lắng nghe các kết nối từ client trên một cổng cố định (ví dụ: 12345).
2. **Kết nối client tới server:**
   - Client kết nối tới server thông qua địa chỉ IP và cổng.
   - Hiển thị menu để người dùng chọn chức năng.
3. **Thực hiện chức năng:**
   - Người dùng trên client chọn một chức năng (ví dụ: Thêm sách, Tìm kiếm sách).
   - Client gửi yêu cầu tới server.
   - Server xử lý yêu cầu, trả kết quả về client.
   - Client hiển thị kết quả.
4. **Kết thúc:**
   - Người dùng chọn thoát, client gửi yêu cầu đóng kết nối.
   - Server đóng kết nối với client.

---

### **Ví dụ kịch bản sử dụng**

1. **Client 1**:
   - Kết nối tới server.
   - Chọn chức năng **Thêm sách** và nhập thông tin:
     - ID: 101, Title: "Java Core Basics", Author: "John Doe", Category: "Programming", Price: 200.000.
   - Nhận phản hồi từ server: "Thêm sách thành công!".

2. **Client 2**:
   - Kết nối tới server.
   - Chọn chức năng **Tìm kiếm sách** với từ khóa: "Java".
   - Nhận danh sách sách chứa từ "Java" trong tiêu đề từ server.

3. **Client 3**:
   - Kết nối tới server.
   - Chọn chức năng **Hiển thị danh sách sách theo thể loại**.
   - Nhận danh sách sách được nhóm theo từng thể loại:
     - **Programming**: "Java Core Basics".
     - **Science**: "Astrophysics 101".

4. **Thoát**:
   - Một client chọn thoát, server đóng kết nối với client đó.
