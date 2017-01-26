package jp.co.topgate.tami.web;

import java.io.IOException;


public class ErrorPage {
    /**
     * エラーメッセージを受け取るフィールド
     */
    private String errMessage;

    /**
     * エラーメッセージをセットするメソッド
     */
    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public void writeHTML(HTTPRequest httpRequest, HTTPResponse httpResponse) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE html>")
                .append("<html lang=\"ja\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<title>エラー</title>")
                .append("</head>")
                .append("<body>")
                .append("<p>" + this.errMessage + "</p>")
                .append("</body>");


        httpResponse.setDynamicResponseBody(new String(stringBuilder).getBytes());
    }


}
