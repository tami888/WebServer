package jp.co.topgate.tami.web;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//TCP サーバーAPI、通常はクライアントソケットからの接続を 受け入れる
import java.net.ServerSocket;
//TCP クライアント API、通常はリモートホストに接続するために使用される
import java.net.Socket;


public class WebServer {

    private static final int port = 8080;

    public static void main(String[] args) throws IOException {
        WebServer webServer = new WebServer();

        webServer.init();
    }

    public void init()throws IOException {
//        String userDir=null;
//        userDir = System.getProperties().getProperty("user.dir");

        ServerSocket serverSocket = null;
        Socket socket = null;
        System.out.println("START Web_Server http://localhost:8080");

        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                //接続要求を待って新しいソケットを作成
                socket = serverSocket.accept();
                System.out.println("リクエストを待っています");

                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                HTTPRequest httpRequest = new HTTPRequest(inputStream);
                HTTPResponse httpResponse = new HTTPResponse(outputStream);

                String requestMethod = httpRequest.getRequestMethod();
                Handler handler = new Handler();

                if (requestMethod.equals("GET")) {
                    handler.handleGET(httpRequest, httpResponse);
                }else{
                    System.out.println("リクエストメソッドが不正です");
                }
            }
        } catch (IOException e) {
            System.err.println("エラー" + e.getMessage());
            System.exit(1);
        } finally {
            if (serverSocket != null) {
                System.out.println("サーバーソケットを閉じます");
                serverSocket.close();
            }

            if (socket != null) {
                socket.close();
            }


        }
    }

}