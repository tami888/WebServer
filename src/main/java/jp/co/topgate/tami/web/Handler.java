package jp.co.topgate.tami.web;


import java.io.File;
import java.io.IOException;


public class Handler {

    public static void handleGET(HTTPRequest httpRequest, HTTPResponse httpResponse) throws IOException {

        System.out.println("GETハンドルに移行しました");
        System.out.println("requestURIは"+ httpRequest.getRequestURI());

        String requestResource = httpRequest.getRequestResource();
        String ext = httpRequest.getRequestResourceExtension(requestResource);
        File file = new File(requestResource);


        if (file.exists()) {
            System.out.println("ファイルを見つけました");
            System.out.println("レスポンスを送ります");

            httpResponse.setResponseBody(file);
            httpResponse.sendResponse(HTTPResponse.message_OK, "OK", ext);


        } else {
            httpResponse.setResponseBody(file);
            httpResponse.sendResponse(HTTPResponse.message_OK, "OK", ext);
            System.out.println("レスポンスを送ります");
            httpResponse.response(file);

        }
    }

}
