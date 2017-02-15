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
    HTTPRequest(InputStream inputStream) throws Exception {
        this.setHTTPRequest(inputStream );
    }


    /**
     * クライアントからのリクエスト中のリクエストメソッド、リクエストURI、を抽出してフィールドに設定する
     */
    void setHTTPRequest(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        this.requestLine = bufferedReader.readLine();
        System.out.println("リクエストラインは" + requestLine);
        if (requestLine == null) {
            System.out.println("リクエストラインがありません");
            throw new IOException("リクエストラインがありません");
        }
        int firstEmpty = this.requestLine.indexOf(" "); // クライアントからのリクエストから、リクエストURIを抽出してフィールドに設定するメソッド
        String secondSentence = this.requestLine.substring(firstEmpty + 1,
                this.requestLine.indexOf(" ", firstEmpty + 1));
        try {
            secondSentence = URLDecoder.decode(secondSentence, "UTF-8");
            this.requestURI = secondSentence;
            System.out.println("secondSentenceは" + secondSentence);
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e);
        }
        this.requestMethod = this.requestLine.substring(0, this.requestLine.indexOf(" ")); // リクエストからのリクエストメソッドを抽出してフィールドに設定するメソッド
        System.out.println("リクエストメソッドは" + this.requestMethod);
    }


    File getRequestResource() {
        String requestResource;
        if ((this.getRequestURI().endsWith("/")) || !(this.getRequestURI().substring(this.getRequestURI().lastIndexOf("/"), this.getRequestURI().length()).contains("."))) {
            requestResource = "src/main/resource" + this.getRequestURI() + "index.html";
        } else {
            requestResource = "src/main/resource" + this.getRequestURI();
        }
        if (requestResource.contains("?")) {
            requestResource = requestResource.substring(requestResource.indexOf(""), requestResource.lastIndexOf("?"));
        }
        File file = new File(requestResource);

        System.out.println("要求されているファイルは" + requestResource);
        return file;
    }

    String getRequestResourceExtension(File requestResource) {
        String path = requestResource.toString();
        String extension = path.substring(path.lastIndexOf(".") + 1,
                path.lastIndexOf(""));
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
