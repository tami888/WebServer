package jp.co.topgate.tami.web;

import java.io.*;

/**
 * クライアントへ送信するHTTPレスポンスに関する責務を持つクラス
 *
 * @author sekiguchikai
 */
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
     * クライアントへのレスポンスヘッダ
     */
    private String responseHeader;
    /**
     * クライアントへのレスポンスボディ
     */
    private byte[] responseBody;

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


    public void getOutputStream(){
        this.outputStream = outputStream;
    }

    /**
     * クライアントへ送信するレスポンスのうち、レスポンスヘッダを設定するメソッド
     *
     * @param requestResource
     *            リクエストされたリソース
     * @param file
     *            ファイル
     */
    public void setResponseHeader(String requestResource, File file) {
        String fileExtension = null;
        if (file.exists()) {
            if (requestResource.indexOf("?") == -1) {
                fileExtension = requestResource.substring(requestResource.lastIndexOf(".") + 1,
                        requestResource.lastIndexOf(""));
            } else {
                fileExtension = requestResource.substring(requestResource.lastIndexOf(".") + 1,
                        requestResource.indexOf("?"));
            }

            if (fileExtension.equals("html") || fileExtension.equals("css") || fileExtension.equals("js")) {
                this.responseHeader = "Content-Type: text/" + fileExtension;
            } else if (fileExtension.equals("png") || fileExtension.equals("jpeg") || fileExtension.equals("gif")) {
                this.responseHeader = "Content-Type: image/" + fileExtension;
            }

        } else {
            this.responseHeader = "Content-Type: text/html";
        }

        System.out.println("レスポンスヘッダは" + this.responseHeader);

    }

    /**
     * クライアントへ送信するレスポンスのうち、レスポンスボディを設定するメソッド
     *
     * @param responseBody
     *            リクエストボディ
     */
    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }


//    public void sendBody(String URI){
//        String userDir= null;
//        userDir = System.getProperties().getProperty("user.dir");
//        File file =new File(userDir + URI);
//        System.out.println("fileは" + file);
//    }


    /**
     * クライアントにレスポンスを送信するためのメソッド
     */

//    void sendResponse(OutputStream outputStream,File file){
//        try{
//            // ファイルをそのまま転送、コード変換を行うと長さが変わる
//            FileInputStream fileInputStream=new FileInputStream(file);
//            byte line[]=new byte[500];
//            int i;
//            while (( i = fileInputStream.read(line,0,500)) != -1 ) {
//                outputStream.write(line,0,i);
//            }
//            outputStream.flush();
//            fileInputStream.close();
//        } catch(IOException e){
//            System.out.println("\n** send_File() IOException **");
//            e.printStackTrace();
//        }
//    }

    public void sendResponse(){
//		try {
//			System.out.println("クライアントに送信を開始します");
//			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//
//			// 引数で受け取ったステータスラインとレスポンスヘッダを結合
//
//			byte[] responseHead = (this.statusLine + "\n" + this.responseHeader + "\n" + "\n").getBytes();
//			byte[] responseContents = new byte[responseHead.length + this.responseBody.length];
//			// ResponseContentsにbyteResponseHeadを追加
//			System.arraycopy(responseHead, 0, responseContents, 0, responseHead.length);
//			// ResponseContentsにresponseBodyを追加
//			System.arraycopy(this.responseBody, 0, responseContents, responseHead.length, this.responseBody.length);
//
//			if (this.responseBody != null) {
//				dataOutputStream.write(responseContents, 0, responseContents.length);
//				dataOutputStream.flush();
//				dataOutputStream.close();
//			}
//
//		} catch (IOException e) {
//			System.err.println("エラー" + e.getMessage());
//			e.printStackTrace();
//		}












    }

    // 以下、テスト用の仕掛け

    /**
     * statusLineを取得するためのメソッド
     *
     * @return responseHeader
     */
    public String getStatusLine(){
        return this.statusLine;
    }

    /**
     * responseHeaderを取得するためのメソッド
     *
     * @return responseHeader
     */
    public String getResponseHeader() {
        return this.responseHeader;
    }

    /**
     * responseBodyを取得するためのメソッド
     *
     * @return responseBody
     */
    public byte[] getResponseBody() {
        return this.responseBody;
    }









    void send_Body(OutputStream outputStream,File file){
        try{
            // ファイルをそのまま転送、コード変換を行うと長さが変わる
            FileInputStream fileInputStream=new FileInputStream(file);
            byte line[]=new byte[500];
            int i;
            while (( i = fileInputStream.read(line,0,500)) != -1 ) {
                outputStream.write(line,0,i);
            }
            outputStream.flush();
            fileInputStream.close();
        } catch(IOException e){
            System.out.println("\n** send_File() IOException **");
            e.printStackTrace();
        }
    }





    /////////////////////////////////////////////////////////////////////////////////
    //
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
        responseSuccess((int)f.length(), "image/jpeg", outputStream);
        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(f));
        try {
            for (int c = bi.read(); c >= 0; c = bi.read()) {
                outputStream.write(c);
            }
        } finally {
            bi.close();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////


}
