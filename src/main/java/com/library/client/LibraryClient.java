package com.library.client;

import com.library.model.Book;
import com.library.protocol.Request;
import com.library.protocol.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryClient {


    private static final int ADD_BOOK = Request.ADD_BOOK;
    private static final int SEARCH_TITLE = Request.SEARCH_BY_TITLE;
    private static final int GET_BY_CATEGORY = Request.GET_BOOKS_BY_CATEGORY;
    private static final int UPDATE_BOOK = Request.UPDATE_BOOK;
    private static final int DELETE_BOOK = Request.DELETE_BOOK;
    private static final int EXIT = Request.EXIT;

    public static void main(String[] args) {
        new LibraryClient().run();
    }


    private static final Logger LOGGER = Logger.getLogger(LibraryClient.class.getName());

    private void run() {
        final String serverHost = "localhost";
        final int serverPort = 21904;

        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            LOGGER.info(() -> "Đã kết nối với máy chủ thư viện: " + serverHost + ':' + serverPort);

            boolean running = true;
            while (running) {
                int choice = promptMenu(scanner);
                Request req = buildRequest(choice, scanner);
                if (req == null) {
                    continue; 
                }
                sendRequest(oos, req);
                Response resp = receiveResponse(ois);
                handleResponse(resp);
                if (choice == EXIT) {
                    LOGGER.info("Thoát... Tạm biệt!");
                    running = false;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Lỗi kết nối đến máy chủ thư viện", e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Lỗi đọc phản hồi từ máy chủ", e);
        }
    }

    private int promptMenu(Scanner scanner) {
        System.out.println("\n===== Library Menu ======");
        System.out.println("1. Thêm sách");
        System.out.println("2. Tìm kiếm theo tiêu đề");
        System.out.println("3. Lấy sách theo thể loại");
        System.out.println("4. Cập nhật sách");
        System.out.println("5. Xóa sách");
        System.out.println("6. Thoát");
        System.out.print("Chọn một tùy chọn: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            LOGGER.warning("Bạn phải nhập một số!");
            return -1;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            LOGGER.warning("Số không hợp lệ, vui lòng thử lại.");
            return -1;
        }
    }

    private Request buildRequest(int choice, Scanner scanner) {
        switch (choice) {
            case ADD_BOOK:
                return buildAddBookRequest(scanner);
            case SEARCH_TITLE:
                return buildSearchRequest(scanner);
            case GET_BY_CATEGORY:
                return Request.byCategory();
            case UPDATE_BOOK:
                return buildUpdateBookRequest(scanner);
            case DELETE_BOOK:
                return buildDeleteBookRequest(scanner);
            case EXIT:
                return Request.exitRequest();
            default:
                if (choice != -1) {
                    LOGGER.warning("Lựa chọn không hợp lệ, vui lòng thử lại.");
                }
                return null;
        }
    }

    private Request buildAddBookRequest(Scanner scanner) {
        System.out.print("Nhập ID sách: ");
        String bookId = scanner.nextLine();
        System.out.print("Nhập tiêu đề sách: ");
        String bookTitle = scanner.nextLine();
        System.out.print("Nhập tác giả sách: ");
        String bookAuthor = scanner.nextLine();
        System.out.print("Nhập thể loại sách: ");
        String bookCategory = scanner.nextLine();
        double price = promptPrice(scanner);
    return Request.addBook(new Book(bookId, bookTitle, bookAuthor, bookCategory, price));
    }

    private Request buildUpdateBookRequest(Scanner scanner) {
        System.out.print("Nhập ID sách cần cập nhật: ");
        String id = scanner.nextLine();
        System.out.print("Tiêu đề mới (bỏ trống giữ nguyên): ");
        String title = scanner.nextLine();
        System.out.print("Tác giả mới (bỏ trống giữ nguyên): ");
        String author = scanner.nextLine();
        System.out.print("Thể loại mới (bỏ trống giữ nguyên): ");
        String category = scanner.nextLine();
        System.out.print("Giá mới (bỏ trống giữ nguyên): ");
        String priceRaw = scanner.nextLine().trim();

        // Vì server không có API lấy book cũ, yêu cầu người dùng nhập đầy đủ hoặc giữ nguyên bằng cách để trống -> hỏi lại nếu thiếu.
        // Thay thế đơn giản: nếu trường trống -> yêu cầu nhập lại vì cần đủ dữ liệu cho bản ghi.
        if (title.isEmpty() || author.isEmpty() || category.isEmpty() || priceRaw.isEmpty()) {
            LOGGER.warning("Bạn phải nhập đầy đủ các trường để cập nhật (hiện chưa hỗ trợ giữ nguyên). Hủy thao tác.");
            return null;
        }
        double price;
        try {
            price = Double.parseDouble(priceRaw);
        } catch (NumberFormatException e) {
            LOGGER.warning("Giá không hợp lệ.");
            return null;
        }
        return Request.updateBook(new Book(id, title, author, category, price));
    }

    private Request buildDeleteBookRequest(Scanner scanner) {
        System.out.print("Nhập ID sách cần xóa: ");
        String id = scanner.nextLine();
        if (id.isEmpty()) {
            LOGGER.warning("ID không được để trống.");
            return null;
        }
        return Request.deleteBook(id);
    }

    private double promptPrice(Scanner scanner) {
        while (true) {
            System.out.print("Nhập giá sách: ");
            String priceInput = scanner.nextLine().trim();
            try {
                return Double.parseDouble(priceInput);
            } catch (NumberFormatException e) {
                LOGGER.warning("Giá không hợp lệ, vui lòng nhập một số!");
            }
        }
    }

    private Request buildSearchRequest(Scanner scanner) {
        System.out.print("Nhập tiêu đề để tìm kiếm: ");
        String keyword = scanner.nextLine();
    return Request.search(keyword);
    }

    private void sendRequest(ObjectOutputStream oos, Request req) throws IOException {
        oos.writeObject(req);
        oos.flush();
    }

    private Response receiveResponse(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (Response) ois.readObject();
    }

    @SuppressWarnings("unchecked")
    private void handleResponse(Response resp) {
        if (resp == null) {
            LOGGER.warning("Phản hồi không hợp lệ từ server");
            return;
        }
        if (!resp.isSuccess()) {
            LOGGER.severe("Lỗi: " + resp.getMessage());
            return;
        }
        Object data = resp.getData();
        if (data == null) {
            if (resp.getMessage() != null) {
                LOGGER.info(resp.getMessage());
            }
            return;
        }
        if (data instanceof List) {
            List<Book> books = (List<Book>) data;
            if (books.isEmpty()) {
                LOGGER.info("Không có sách.");
            } else {
                System.out.println("\n=== Danh sách sách ===");
                books.forEach(this::printBookDetails);
            }
        } else if (data instanceof Map) {
            System.out.println("\n=== Sách theo thể loại ===");
            Map<String, List<Book>> map = (Map<String, List<Book>>) data;
            map.forEach((cat, list) -> {
                System.out.println("-- " + cat + " --");
                list.forEach(b -> System.out.println(b.getId() + " | " + b.getTitle() + " | " + b.getAuthor() + " | " + b.getPrice()));
            });
        } else {
            LOGGER.warning("Phản hồi không xác định.");
        }
    }

    private void printBookDetails(Book book) {
        System.out.println("ID sách: " + book.getId());
        System.out.println("Tiêu đề: " + book.getTitle());
        System.out.println("Tác giả: " + book.getAuthor());
        System.out.println("Thể loại: " + book.getCategory());
        System.out.println("Giá: " + book.getPrice());
        System.out.println("----------------------------");
    }
}
