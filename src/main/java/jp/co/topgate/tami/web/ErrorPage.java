package jp.co.topgate.tami.web;

import java.io.IOException;


public class ErrorPage {
    /**
     * エラーメッセージを受け取るフィールド
     */
    private String errorMessage;

    /**
     * エラーメッセージをセットするメソッド
     */
    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    void writeHTML(HTTPResponse httpResponse) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE html>")
                .append("<html lang=\"ja\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<title>エラー</title>")
                .append("</head>")
                .append("<body>")
                .append("<p>" + this.errorMessage + "</p>")
                .append("</body>");


        httpResponse.setResponseBody(new String(stringBuilder).getBytes());
    }
}
