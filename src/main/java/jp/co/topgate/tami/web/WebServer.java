package jp.co.topgate.tami.web;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class WebServer {

    private static final int port = 8080;


    public static void main(String[] args) throws IOException {
        WebServer webServer = new WebServer();
        webServer.init();
    }

    public void init()throws IOException {
        System.out.println("START Web_Server http://localhost:8080");

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                //接続要求を待って新しいソケットを作成
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("リクエストを待っています");

                    Handler handler = new Handler();
                    ErrorPage errorPage = new ErrorPage();

                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();


                    HTTPRequest httpRequest = null;
                    HTTPResponse httpResponse = null;
                    try {
                        httpRequest = new HTTPRequest(inputStream);
                        httpResponse = new HTTPResponse(outputStream);
                    } catch (Exception e) {
                        System.err.println("エラー1" + e.getMessage());//ここにメッサージ以外の405とかを入れておく
                        errorPage.setErrMessage("405 Method Not Allowed");
                        handler.handleErr(httpResponse);
                    }

                    String requestMethod = httpRequest.getRequestMethod();

                    if ("GET".equals(requestMethod)) {
                        handler.handleGET(httpRequest, httpResponse);
                    }else{
                        System.out.println("リクエストメソッドが不正です");
                        errorPage.setErrMessage("400 Bad Request");
                        handler.handleErr(httpResponse);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("エラー2" + e.getMessage());
            System.exit(1);
        }
    }
}
