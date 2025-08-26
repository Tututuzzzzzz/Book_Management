package com.library.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LibraryServer {

    private static final int PORT = 21904;

    public static void main(String[] args) {
        Library library = new Library();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Library server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());

                // Mỗi client chạy trong thread riêng
                ClientHandle clientHandle = new ClientHandle(clientSocket, library);
                Thread t = new Thread(clientHandle);
                t.start();
            }

        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
