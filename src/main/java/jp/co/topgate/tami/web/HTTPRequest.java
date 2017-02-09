package jp.co.topgate.tami.web;

import java.io.*;
import java.net.URLDecoder;

public class HTTPRequest {
    /**
     * クライアントからのリクエストライン
     */
    private String requestLine;
    /**
     * クライアントからのリクエストメソッド
     */
    private String requestMethod;
    /**
     * クライアントからのリクエストURI
     */
    private String requestURI;
    /**
     * コンストラクタ、set~で各フィールドを初期設定する
     *
     */
    HTTPRequest(InputStream inputStream) {
        this.setHTTPRequest(inputStream );
    }


    /**
     * クライアントからのリクエスト中のリクエストメソッド、リクエストURI、を抽出してフィールドに設定する
     */
    void setHTTPRequest(InputStream inputStream ) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            this.requestLine = bufferedReader.readLine();
            System.out.println("リクエストラインは" + requestLine);
            if (requestLine == null) {
                System.out.println("リクエストラインがありません");


            }
        } catch (IOException e) {
            System.err.println("エラー:" + e.getMessage());
            e.printStackTrace();
        }

        int firstEmpty = this.requestLine.indexOf(" "); // クライアントからのリクエストから、リクエストURIを抽出してフィールドに設定するメソッド
        String secondSentence = this.requestLine.substring(firstEmpty + 1,
                this.requestLine.indexOf(" ", firstEmpty + 1));
        try {
            secondSentence = URLDecoder.decode(secondSentence, "UTF-8");
            this.requestURI = secondSentence;
            System.out.println("secondSentenceは" + secondSentence);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        this.requestMethod = this.requestLine.substring(0, this.requestLine.indexOf(" ")); // リクエストからのリクエストメソッドを抽出してフィールドに設定するメソッド
        System.out.println("リクエストメソッドは" + this.requestMethod);

    }


    String getRequestResource() {
        String requestResource;
        if ((this.getRequestURI().endsWith("/")) || !(this.getRequestURI().substring(this.getRequestURI().lastIndexOf("/"), this.getRequestURI().length()).contains("."))) {
            requestResource = "src/main/resource" + this.getRequestURI() + "index.html";
        } else {
            requestResource = "src/main/resource" + this.getRequestURI();
        }
        if (requestResource.contains("?")) {
            requestResource = requestResource.substring(requestResource.indexOf(""), requestResource.lastIndexOf("?"));

        }

        System.out.println("要求されているファイルは" + requestResource);
        return requestResource;

    }

    String getRequestResourceExtension(String requestResource) {

        String extension = requestResource.substring(requestResource.lastIndexOf(".") + 1,
                requestResource.lastIndexOf(""));
        System.out.println("ファイルの拡張子は" + extension);

        return extension;
    }

    /**
     * リクエストメソッドを返すメソッド
     *
     * @return リクエストメソッドを返す
     */
    String getRequestMethod() {
        return this.requestMethod;
    }

    /**
     * リクエストURIを返すメソッド
     *
     * @return リクエストURIを返す
     */
    String getRequestURI() {
        return this.requestURI;
    }

}
