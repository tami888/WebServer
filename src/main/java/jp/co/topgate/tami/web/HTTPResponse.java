package jp.co.topgate.tami.web;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


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
     * 動的なレスポンスボディ
     */
    private byte[] dynamicResponseBody;

    /**
     * レスポンスボディ
     */
    private File ResponseBody;

    /**
     * 動的レスポンスボディ
     */
    public void setDynamicResponseBody(byte[] responseBody) {
        this.dynamicResponseBody = responseBody;
    }

    /**
     * レスポンスボディを設定するメソッド
     */
    public void setResponseBody(File file){
        this.ResponseBody = file;
    }




    public void sendResponse(int message, String causing, String fileEx) throws IOException {

        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        if (this.dynamicResponseBody != null) {
            // 引数で受け取ったステータスラインとレスポンスヘッダを結合
            byte[] responseHead = ("HTTP/1.1 " + message + " " + causing + "\n" + this.contentType(fileEx) + "\n").getBytes();
            dataOutputStream.write(responseHead);
            dataOutputStream.write(this.dynamicResponseBody);
            dataOutputStream.flush();
            dataOutputStream.close();
        } else {
            FileDataSource fileDataSource = new FileDataSource(this.ResponseBody);
            DataHandler dataHandler = new DataHandler(fileDataSource);
            byte[] responseHead = ("HTTP/1.1 " + causing + " " + causing + "\n" + this.contentType(fileEx) + "\n").getBytes();
            dataOutputStream.write(responseHead);
            dataHandler.writeTo(dataOutputStream);
            dataOutputStream.flush();
            dataOutputStream.close();
        }
    }


    public String contentType(String fileExt) {

        Map<String, String> contentTypeMap = new HashMap<>();
        contentTypeMap.put("html", "Content-Type: text/html" + "\n");
        contentTypeMap.put("css", "Content-Type: text/css" + "\n");
        contentTypeMap.put("js", "Content-Type: text/js" + "\n");
        contentTypeMap.put("jpeg", "Content-Type: image/jpeg" + "\n");
        contentTypeMap.put("png", "Content-Type: image/png" + "\n");
        contentTypeMap.put("gif", "Content-Type: image/gif" + "\n");

        String contentType = contentTypeMap.get(fileExt);

        if (contentType == null) {
            contentType = "Content-Type: application/octet-stream" + "\n";
        }

        System.out.println("レスポンスヘッダは" + contentType);
        return contentType;

    }




}
