package jp.co.topgate.tami.web;

import java.io.*;


public class Files {

    /**
     * ファイルを読み込むメソッド
     */
    static byte[] readFile(File fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bi = new BufferedInputStream(fis);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int tmp = 0;
        while ((tmp = bi.read()) != -1) {
            byteArrayOutputStream.write(tmp);

        }
        bi.close();
        byte[] body = byteArrayOutputStream.toByteArray();
        return body;
    }
}
