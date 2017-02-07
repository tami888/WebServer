package jp.co.topgate.tami.web;


import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by honmatakumi on 2017/01/24.
 */
public class HTTPResponseTest {

    private OutputStream outputStream = new ByteArrayOutputStream();
    private HTTPResponse httpResponse = new HTTPResponse(outputStream);

    @Test
    public void testContentType(){
        String fileExt[] = {"html", "css", "js", "jpeg", "png", "gif"};
        String expectedContents[] = {"text/html", "text/css", "text/js", "image/jpeg", "image/png", "image/gif"};

        for (int i = 0; i < fileExt.length; i++) {

            String file =  fileExt[i];

            assertThat(expectedContents[i], is(httpResponse.contentType(file)));
        }
    }


    @Test
    public void sendResponse() {
        OutputStream outputStream = new ByteArrayOutputStream();
        HTTPResponse httpResponse = new HTTPResponse(outputStream);

        httpResponse.setErrResponseBody("レスポンスボディ".getBytes());
        try {
            httpResponse.sendResponse(HTTPResponse.message_OK, "OK", "html");
        } catch (IOException e) {
            System.err.println("エラー:" + e.getMessage());
            e.printStackTrace();

        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));

        StringBuilder stringBuilder = new StringBuilder();
        String result;

        try {
            result = bufferedReader.readLine();
            while (result != null) {
                System.out.println(result);
                stringBuilder.append(result);
                result = bufferedReader.readLine();
            }
            System.out.println(stringBuilder);
        } catch (IOException e) {
            System.err.println("エラー" + e.getMessage());
            e.printStackTrace();
        }

        assertThat((new String(stringBuilder)), is("HTTP/1.1 200 OKContent-Type: text/htmlレスポンスボディ"));

    }
}
