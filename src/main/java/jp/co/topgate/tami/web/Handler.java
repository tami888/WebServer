package jp.co.topgate.tami.web;

import java.io.File;
import java.io.IOException;

class Handler {

    static void handleGET(HTTPRequest httpRequest, HTTPResponse httpResponse) throws IOException {

        System.out.println("GETハンドルに移行しました");
        System.out.println("requestURIは"+ httpRequest.getRequestURI());

        String requestResource = httpRequest.getRequestResource();
        File file = new File(requestResource);

        System.out.println("リクエストリソースは" + requestResource);

        if (file.exists()) {
            System.out.println("ファイルを見つけました");
            System.out.println("レスポンスを送ります");
            httpResponse.readFile(requestResource);
            httpResponse.sendResponse(HTTPResponse.MESSAGE_OK, httpRequest.getRequestResourceExtension(requestResource));
        } else {
            ErrorPage errorPage = new ErrorPage();
            System.out.println("ファイルが見つかりませんでした");
            errorPage.setErrorMessage("404 NOT Found");
            errorPage.writeHTML(httpResponse);
            httpResponse.sendResponse(HTTPResponse.MESSAGE_NOT_FOUND, httpRequest.getRequestResourceExtension(requestResource));
        }
    }

    static void handleError(HTTPRequest httpRequest, HTTPResponse httpResponse) throws IOException {
        System.out.print("エラーページを表示します");
        ErrorPage errorPage = new ErrorPage();
        errorPage.writeHTML(httpResponse);
        String requestResource = httpRequest.getRequestResource();
        httpResponse.sendResponse(HTTPResponse.INTERNAL_SERVER_ERROR, httpRequest.getRequestResourceExtension(requestResource));
    }
}
