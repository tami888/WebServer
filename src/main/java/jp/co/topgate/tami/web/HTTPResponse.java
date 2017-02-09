package jp.co.topgate.tami.web;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.*;


public class HTTPResponse {

    /**
     * クライアントとのsocketを格納したOutputStream
     */
    private OutputStream outputStream;
    /**
     * リクエストが成功した場合のコード
     */
    public static final int message_OK = 200;

    /**
     * リクエストにエラーがあります
     */
    public static final int Message_Bad_Request = 400;

    /**
     * 該当のページが存在しない時
     */
    public static final int message_NOT_FOUND = 404;

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
    void sendResponse(int message, String causing, String fileEx ) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        byte[] responseHead = ("HTTP/1.1 " + message + " " + causing + "\n" + this.createContentTypeHeader(fileEx) + "\n").getBytes();
        dataOutputStream.write(responseHead);
        dataOutputStream.write(this.responseBody);
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    /**
     * ファイルを読み込むメソッド
     */
    void readFile(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bi = new BufferedInputStream(fis);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int tmp = 0;
        while ((tmp = bi.read()) != -1) {
            byteArrayOutputStream.write(tmp);

        }
        bi.close();
        byte[] sss = byteArrayOutputStream.toByteArray();
        setResponseBody(sss);
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
    //設計思想を明確にしておかないと課題が通らない

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
