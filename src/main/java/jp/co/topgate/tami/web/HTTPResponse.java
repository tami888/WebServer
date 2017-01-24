package jp.co.topgate.tami.web;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import java.io.*;


public class HTTPResponse {

    /**
     * クライアントとのsocketを格納したOutputStream
     */
    private OutputStream outputStream;
    /**
     * クライアントへのレスポンスライン
     */
    private String statusLine;

    /**
     * コンストラクタ
     *
     * @param outputStream
     */
    HTTPResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * クライアントへ送信するレスポンスのうち、ステータスラインを設定するメソッド
     *
     * @param statusLine
     *
     */
    public void setStatusLine(String statusLine) {
        this.statusLine = statusLine;
    }

    /**
     * statusLineを取得するためのメソッド
     *
     * @return responseHeader
     */
    public String getStatusLine(){
        return this.statusLine;
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

    public String ContentType(File f){
        FileTypeMap map = FileTypeMap.getDefaultFileTypeMap();
        if (map instanceof MimetypesFileTypeMap) {
            try {
                ((MimetypesFileTypeMap)map).addMimeTypes("image/png png PNG");
                ((MimetypesFileTypeMap) map).addMimeTypes("text/javascript js JS");
                ((MimetypesFileTypeMap) map).addMimeTypes("text/css css CSS");
            } catch (Exception ignored) {}
        }
        String type = map.getContentType(f);
        return type;
    }



}
