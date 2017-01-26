package jp.co.topgate.tami.web;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import java.io.*;
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




    void responseFailure(long len, String type, OutputStream outputStream)throws IOException{
        PrintWriter prn = new PrintWriter(outputStream);
        prn.print("HTTP/1.1 404 Not Found\r\n");
        prn.print("Connection: close\r\n");
        prn.print("Content-Length: ");
        prn.print(len);
        prn.print("\r\n");
        prn.print("Content-Type: ");
        prn.print(type);
        prn.print("\r\n\r\n");
        prn.flush();
    }

    void responseF(File f) throws IOException {
        responseFailure((int)f.length(),FileTypeMap.getDefaultFileTypeMap().getContentType(f), outputStream);
        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(f));
        try {
            for (int c = bi.read(); c >= 0; c = bi.read()) {
                outputStream.write(c);
            }
        } finally {
            bi.close();
        }
    }

    /**
     * クライアントへOK応答を返す。
     * @param len コンテンツ長
     * @param type コンテンツのMIMEタイプ
     */
    void responseSuccess(long len, String type, OutputStream outputStream) throws IOException {

        PrintWriter prn = new PrintWriter(outputStream);
        prn.print("HTTP/1.1 200 OK\r\n");
        prn.print("Connection: close\r\n");
        prn.print("Content-Length: ");
        prn.print(len);
        prn.print("\r\n");
        prn.print("Content-Type: ");
        prn.print(type);
        prn.print("\r\n\r\n");
        prn.flush();
    }

    /**
     * クライアントへ指定されたファイルを返送する。
     */
    void response(File f) throws IOException {
        FileTypeMap map = FileTypeMap.getDefaultFileTypeMap();
        if (map instanceof MimetypesFileTypeMap) {
            try {
                ((MimetypesFileTypeMap)map).addMimeTypes("image/png png PNG");
                ((MimetypesFileTypeMap) map).addMimeTypes("text/javascript js JS");
                ((MimetypesFileTypeMap) map).addMimeTypes("text/css css CSS");
            } catch (Exception ignored) {}
        }

        responseSuccess((int)f.length(),map.getContentType(f), outputStream);
        System.out.println("コンテンツタイプは" + FileTypeMap.getDefaultFileTypeMap().getContentType(f));
        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(f));
        try {
            for (int c = bi.read(); c >= 0; c = bi.read()) {
                outputStream.write(c);
            }
        } finally {
            bi.close();
        }
    }

//    public String contentType(String f){
//        FileTypeMap map = FileTypeMap.getDefaultFileTypeMap();
//        if (map instanceof MimetypesFileTypeMap) {
//            try {
//                ((MimetypesFileTypeMap)map).addMimeTypes("image/png png PNG");
//                ((MimetypesFileTypeMap) map).addMimeTypes("text/javascript js JS");
//                ((MimetypesFileTypeMap) map).addMimeTypes("text/css css CSS");
//            } catch (Exception ignored) {}
//        }
//        String type = "Content-Type: " + map.getContentType(f);
//        return type;
//    }

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
