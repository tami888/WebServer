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


                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    HTTPResponse httpResponse = new HTTPResponse(outputStream);

                    HTTPRequest httpRequest;
                    try {
                        httpRequest = new HTTPRequest(inputStream);
                    } catch (Exception e) {
                        ErrorPage errorPage = new ErrorPage();
                        errorPage.setErrorMessage("400 Bad Request");
                        e.printStackTrace();
                        continue;
                    }

                    String requestMethod = httpRequest.getRequestMethod();
                    Handler handler = new Handler();
                    if ("GET".equals(requestMethod)) {
                        try {
                            handler.handleGET(httpRequest, httpResponse);
                        } catch (IOException e) {
                            System.err.println("エラー:" + e.getMessage());
                            e.printStackTrace();

                            System.out.println("リクエストメソッドが不正です");
                            ErrorPage errorPage = new ErrorPage();
                            errorPage.setErrorMessage("500 Internal Server Error");
                            handler.handleError(httpRequest, httpResponse);
                        }
                    } else {
                        ErrorPage errorPage = new ErrorPage();
                        errorPage.setErrorMessage("400 Bad Request");
                        handler.handleError(httpRequest, httpResponse);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("エラー" + e.getMessage());
            System.exit(1);
        }
    }
}
