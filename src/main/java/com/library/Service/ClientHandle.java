
package com.library.Service;

import com.library.model.Book;
import com.library.protocol.Request;
import com.library.protocol.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.List;

public class ClientHandle implements Runnable {
    private Socket socket;
    private Library library;
    private static final File DATA_FILE = new File("library-data.bin");

    public ClientHandle(Socket socket, Library library) {
        this.socket = socket;
        this.library = library;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ) {
            while (true) {
                Request req = (Request) ois.readObject();
                Response resp;

                switch (req.getType()) {
                    case Request.ADD_BOOK:
                        Book book = (Book) req.getData();
                        boolean ok = library.addBook(book);
                        if (ok) persist();
                        resp = new Response(ok, null, ok ? "Thêm sách thành công!" : "ID đã tồn tại!");
                        break;

                    case Request.SEARCH_BY_TITLE:
                        String keyword = req.getKeyword(); // dùng getKeyword() cho đúng
                        List<Book> result = library.searchByTitle(keyword);
                        resp = new Response(true, result, null);
                        break;

                    case Request.GET_BOOKS_BY_CATEGORY:
                        resp = new Response(true, library.getBooksByCategory(), null);
                        break;
                    case Request.UPDATE_BOOK:
                        Book updated = (Book) req.getData();
                        boolean updatedOk = library.updateBook(updated);
                        if (updatedOk) persist();
                        resp = new Response(updatedOk, null, updatedOk ? "Cập nhật thành công" : "Không tìm thấy ID");
                        break;
                    case Request.DELETE_BOOK:
                        String delId = req.getKeyword();
                        boolean deleted = library.deleteBook(delId);
                        if (deleted) persist();
                        resp = new Response(deleted, null, deleted ? "Xóa thành công" : "Không tìm thấy ID");
                        break;

                    case Request.EXIT:
                        resp = new Response(true, null, "Kết nối đã đóng.");
                        oos.writeObject(resp);
                        oos.flush();
                        socket.close();
                        System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
                        return;

                    default:
                        resp = new Response(false, null, "Yêu cầu không hợp lệ.");
                        break;
                }

                // Gửi response tới client
                oos.writeObject(resp);
                oos.flush();
            }

        } catch (Exception e) {
            System.out.println("Máy khách bị ngắt kết nối: " + socket.getRemoteSocketAddress());
            e.printStackTrace(); // in ra stack trace để debug
        }
    }

    private synchronized void persist() {
        try {
            library.saveToFile(DATA_FILE);
        } catch (Exception ex) {
            System.err.println("Lỗi lưu dữ liệu: " + ex.getMessage());
        }
    }
}
