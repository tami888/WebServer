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
    public void testCreateContentTypeHeader() {
        String fileExt[] = {"html", "css", "js", "jpeg", "png", "gif"};
        String expectedContents[] = {"Content-Type: text/html\n", "Content-Type: text/css\n", "Content-Type: text/js\n", "Content-Type: image/jpeg\n", "Content-Type: image/png\n", "Content-Type: image/gif\n"};

        for (int i = 0; i < fileExt.length; i++) {

            String file = fileExt[i];

            assertThat(expectedContents[i], is(httpResponse.createContentTypeHeader(file)));
        }
    }

    @Test
    public void testCreateContentType() {
        String fileExt[] = {"html", "css", "js", "jpeg", "png", "gif"};
        String expectedContents[] = {"text/html", "text/css", "text/js", "image/jpeg", "image/png", "image/gif"};

        for (int i = 0; i < fileExt.length; i++) {

            String file = fileExt[i];

            assertThat(expectedContents[i], is(httpResponse.createContentType(file)));
        }
    }

    @Test
    public void sendResponse() {
        OutputStream outputStream = new ByteArrayOutputStream();
        HTTPResponse httpResponse = new HTTPResponse(outputStream);

        httpResponse.setResponseBody("レスポンスボディ".getBytes());
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
