package jp.co.topgate.tami.web;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
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
     * 許可されていないメソッドタイプのリクエストを受けた。
     */
    public static final int message_METHOD_Not_ALLOWED = 405;

    /**
     * コンストラクタ
     */
    HTTPResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 動的なレスポンスボディ
     */
    private byte[] errResponseBody;

    /**
     * レスポンスボディ
     */
    private File ResponseBody;

    /**
     * 動的レスポンスボディ
     */
    public void setErrResponseBody(byte[] responseBody) {
        this.errResponseBody = responseBody;
    }

    /**
     * レスポンスボディを設定するメソッド
     */
    public void setResponseBody(File file){
        this.ResponseBody = file;
    }

    /**
     * レスポンスを送るメソッド
     */
    public void sendResponse(int message, String causing, String fileEx) throws IOException {

        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        FileDataSource fileDataSource = new FileDataSource(this.ResponseBody);
        DataHandler dataHandler = new DataHandler(fileDataSource);
        byte[] responseHead = ("HTTP/1.1 " + message + " " + causing + "\n" + this.contentTypeExt(fileEx) + "\n").getBytes();
        dataOutputStream.write(responseHead);

        if (this.errResponseBody != null) {
            dataOutputStream.write(this.errResponseBody);
        } else {
            dataHandler.writeTo(dataOutputStream);
        }
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    //htmlと入力したら意図するものはtext/htmlでありContent-Type: text/htmlではない
    //sendResponseの処理に同じ処理があるため一つにまとめる、バグ対策のため
    //setterとgetterの慣習に則ていないものがある
    //sendResponseのテストを書く
    //ハンドラーのテストも書いたほうが良さげ
    //InputStreamやOutputStreamを避けている節がある。

    public String contentTypeExt(String fileEx) {
        String fileType = contentType(fileEx);
        return "Content-Type: " + fileType + "\n";
    }

    public String contentType(String file) {
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
