package geektime.hw.week3.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 创建了一个固定大小的线程池处理请求
public class BackendServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
        final ServerSocket serverSocket = new ServerSocket(8088);
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isReader = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isReader);
                String headerLine;
                while ((headerLine = br.readLine()).length() != 0) {
                    System.out.println(headerLine);
                }

                StringBuilder builder = new StringBuilder();
                while (br.ready()) {
                    builder.append((char) br.read());
                }
                System.out.println("Request Payload: " + builder);
                executorService.execute(() -> service(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "Reach Backend Server!";
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}