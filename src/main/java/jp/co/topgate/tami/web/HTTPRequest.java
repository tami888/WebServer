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
     * @paramsocketからのstreamを受け取る
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
                // TODO:throw exception => HTTP Status 400~
//                throw new RuntimeException("RequestHeader is INVALID!!");
            }
        } catch (IOException e) {
            System.err.println("エラー:" + e.getMessage());
            e.printStackTrace();
            // TODO: 2017/01/21 need handling
        }
    }

    /**
     * クライアントからのリクエストから、リクエストURIを抽出してフィールドに設定するメソッド
     */
    public  void setRequestURI() {
        int firstEmpty = this.requestLine.indexOf(" ");

        String secondSentence = this.requestLine.substring(firstEmpty + 1,
                this.requestLine.indexOf(" ", firstEmpty + 1));

        String dir = "";

        if (secondSentence.indexOf("/.") != -1){
            dir = secondSentence.substring(secondSentence.lastIndexOf("/."),
                    secondSentence.lastIndexOf("/"));
        }

        System.out.println("dirは"+dir);


        if (secondSentence.indexOf("?") == -1) {
            secondSentence = secondSentence.substring(secondSentence.lastIndexOf("/"),
                    secondSentence.lastIndexOf(""));
            secondSentence = dir + secondSentence;
            System.out.println(secondSentence);
        }else {
            secondSentence = secondSentence.substring(secondSentence.lastIndexOf("/") ,
                    secondSentence.indexOf("?"));
            secondSentence = dir + secondSentence;
            System.out.println(secondSentence);
        }

        System.out.println("secondSentenceは"+secondSentence);



        try {
            secondSentence = URLDecoder.decode(secondSentence, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (secondSentence.equals("/")){
            this.requestURI = "/index.html";
        }else if(secondSentence.equals(secondSentence)) {
            this.requestURI = secondSentence;
        }



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
