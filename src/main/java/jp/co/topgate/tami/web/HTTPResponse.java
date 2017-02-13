package jp.co.topgate.tami.web;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


public class HTTPResponse {

    /**
     * クライアントとのsocketを格納したOutputStream
     */
    private OutputStream outputStream;

    /**
     * リクエストが成功した場合のコード
     */
    static final int MESSAGE_OK = 200;

    /**
     * 該当のページが存在しない時
     */
    static final int MESSAGE_NOT_FOUND = 404;

    /**
     * サーバー内部エラーの時
     */
    static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * コンストラクタ
     */
    HTTPResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    /**
     * レスポンスボディ
     */
    private byte[] responseBody;

    /**
     * レスポンスボディを設定するメソッド
     */
    void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    /**
     * レスポンスボディを送るメソッド
     */
    void sendResponse(int statusCode, String fileEx) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        byte[] statusLine = ("HTTP/1.1 " + statusCode + " " + createReasonPhrase(statusCode) + "\n").getBytes();
        byte[] entityHeader = (createEntityHeader(fileEx).getBytes());
        dataOutputStream.write(statusLine);
        dataOutputStream.write(entityHeader);
        dataOutputStream.write(this.responseBody);
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    /**
     * Content-TypeやContent-Length等のエンティティを設定するメソッド
     */
    String createEntityHeader(String fileEx) {
        String contentLength = "";

        return this.createContentTypeHeader(fileEx) + "\n" + contentLength;
    }


    void makeResponseBody(File requestResource, HTTPResponse httpResponse) throws IOException {
        byte[] body = null;

        if (requestResource.exists()) {
            body = Files.readFile(requestResource);
        } else {
            body = ErrorPage.writeHTML();
        }
        httpResponse.setResponseBody(body);
    }


    //htmlと入力したら意図するものはtext/htmlでありContent-Type: text/htmlではない
    //sendResponseの処理に同じ処理があるため一つにまとめる、バグ対策のため
    //setterとgetterの慣習に則ていないものがある
    //sendResponseのテストを書く
    //ハンドラーのテストも書いたほうが良さげ
    //InputStreamやOutputStreamを避けている節がある。
    //メソッド名は動詞を入れることが原則、メソッド名を見てすぐ挙動が理解しやすいように工夫する。
    //メソッドを追加したならそのテストも書かないと追加した意味がない
    //Errとかの略語はあまり使用しない。場所によって使い分けること
    //causingをわざわざ指定するのは意味がないのでは
    //javaの定数の命名規則に合致していないものがある
    //設計思想を明確にしていない部分が多々ある
    //拡張子でもcontentTypeの判別はいいんだけどcontentTypeだけをレスポンスヘッダーに入れるのではなく新たに発生するcontentLengthなどを入れるメソッドを一つ、それを再構築するメソッドを一つとメソッドを分けて行うことが望ましい。それによって新しいものが追加された場合でも再利用ができる。今のコードはcontentTypeのためだけのメソッドであるため再利用性が悪い


    String createReasonPhrase(int statusCode) {
        String phrase = null;
        switch (statusCode) {
            case 200:
                phrase = "OK";
                break;
            case 400:
                phrase = "Bad Request";
                break;
            case 404:
                phrase = "Not Found";
                break;
            case 500:
                phrase = "Internal Server Error";
                break;
            default:
                System.out.println("reasonPhraseは見つかりませんでした");
        }
        return phrase;
    }

    String createContentTypeHeader(String fileExtension) {
        String fileType = createContentType(fileExtension);
        return "Content-Type: " + fileType + "\n";
    }

    String createContentType(String file) {
        String fileType = null;
        switch (file) {
            case "html":
                fileType = "text/html";
                break;
            case "css":
                fileType = "text/css";
                break;
            case "js":
                fileType = "text/js";
                break;
            case "jpeg":
            case "jpg":
                fileType = "image/jpeg";
                break;
            case "png":
                fileType = "image/png";
                break;
            case "gif":
                fileType = "image/gif";
                break;
            default:
                System.out.println("contentTypeは見つかりませんでした");
        }
        return fileType;
    }
}
