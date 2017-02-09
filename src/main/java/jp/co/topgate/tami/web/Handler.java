package jp.co.topgate.tami.web;


import java.io.File;
import java.io.IOException;


public class Handler {

    public static void handleGET(HTTPRequest httpRequest, HTTPResponse httpResponse) throws IOException {

        System.out.println("GETハンドルに移行しました");
        System.out.println("requestURIは"+ httpRequest.getRequestURI());

        String requestResource = httpRequest.getRequestResource();
        File file = new File(requestResource);

        System.out.println("リクエストリソースは" + requestResource);

        ErrorPage errorPage = new ErrorPage();

        if (file.exists()) {
            System.out.println("ファイルを見つけました");
            System.out.println("レスポンスを送ります");
//            httpResponse.setResponseBody(file);
            String ext = httpRequest.getRequestResourceExtension(requestResource);
            httpResponse.readFile(requestResource);
            httpResponse.sendResponse(HTTPResponse.message_OK, "OK", ext);
        } else {
            System.out.println("ファイルが見つかりませんでした");
            errorPage.setErrorMessage("404 NOT Found");
            errorPage.writeHTML(httpResponse);
            httpResponse.sendResponse(HTTPResponse.message_NOT_FOUND, "Not Found", "html");
        }
    }


    public static void handleError(HTTPResponse httpResponse) throws IOException {
        System.out.print("エラーページを表示します");
        ErrorPage errorPage = new ErrorPage();
        errorPage.writeHTML(httpResponse);
        httpResponse.sendResponse(HTTPResponse.Message_Bad_Request, "Bad Request", "html");
    }
}
