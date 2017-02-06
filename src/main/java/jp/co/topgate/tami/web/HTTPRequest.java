package jp.co.topgate.tami.web;

import java.io.*;
import java.net.URLDecoder;

public class HTTPRequest {

    /**
     * クライアントからのsocket通信の中身を格納するための変数
     */
    private InputStream inputStream;
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
    // private String queryString;

    /**
     * コンストラクタ、set~で各フィールドを初期設定する
     *
     */
    public HTTPRequest(InputStream inputStream) {
        this.inputStream = inputStream;
        this.setHTTPRequestLine();
        if (requestLine == null) {
            return;
        }
        this.setRequestURI();
        this.setRequestMethod();
    }

    /**
     * クライアントからのリクエストから、リクエストラインを抽出してフィールドに設定するメソッド
     */
    private void setHTTPRequestLine() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
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
    }

    /**
     * クライアントからのリクエストから、リクエストURIを抽出してフィールドに設定するメソッド
     */
    public  void setRequestURI() {
        int firstEmpty = this.requestLine.indexOf(" ");

        String secondSentence = this.requestLine.substring(firstEmpty + 1,
                this.requestLine.indexOf(" ", firstEmpty + 1));

        try {
            secondSentence = URLDecoder.decode(secondSentence, "UTF-8");
            this.requestURI = secondSentence;
            System.out.println("secondSentenceは"+secondSentence);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public String getRequestResource() {
        String requestResource;
        if ((this.getRequestURI().endsWith("/")) || !(this.getRequestURI().substring(this.getRequestURI().lastIndexOf("/"), this.getRequestURI().length()).contains("."))) {
            requestResource = "src/main/resource" + this.getRequestURI() + "index.html";
        } else {
            requestResource = "src/main/resource" + this.getRequestURI();
        }
        if (requestResource.indexOf("?") != -1) {
            requestResource = requestResource.substring(requestResource.indexOf(""), requestResource.lastIndexOf("?"));

        }

        System.out.println("要求されているファイルは" + requestResource);
        return requestResource;

    }

    public String getRequestResourceExtension(String requestResource) {

        String extension = requestResource.substring(requestResource.lastIndexOf(".") + 1,
                requestResource.lastIndexOf(""));
        System.out.println("ファイルの拡張子は" + extension);

        return extension;
    }


    /**
     * クライアントからのリクエストから、リクエストメソッドを抽出してフィールドに設定するメソッド
     */
    private void setRequestMethod() {
        this.requestMethod = this.requestLine.substring(0, this.requestLine.indexOf(" "));
        System.out.println("リクエストメソッドは" + this.requestMethod);
    }

    /**
     * リクエストラインを返すメソッド
     *
     * @return リクエストラインを返す
     */
    public String getRequestLine() {
        return this.requestLine;
    }

    /**
     * リクエストメソッドを返すメソッド
     *
     * @return リクエストメソッドを返す
     */
    public String getRequestMethod() {
        return this.requestMethod;
    }

    /**
     * リクエストURIを返すメソッド
     *
     * @return リクエストURIを返す
     */
    public String getRequestURI() {
        return this.requestURI;
    }


}
