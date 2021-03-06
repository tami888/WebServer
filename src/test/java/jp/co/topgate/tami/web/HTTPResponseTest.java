package jp.co.topgate.tami.web;


import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class HTTPResponseTest {

    private OutputStream outputStream = new ByteArrayOutputStream();
    private HTTPResponse httpResponse = new HTTPResponse(outputStream);


    @Test
    public void createContentTypeHeaderでcontentTypeのヘッダーを取得する() {
        String fileExt[] = {"html", "css", "js", "jpeg", "png", "gif"};
        String expectedContents[] = {"Content-Type: text/html\n", "Content-Type: text/css\n", "Content-Type: text/js\n", "Content-Type: image/jpeg\n", "Content-Type: image/png\n", "Content-Type: image/gif\n"};

        for (int i = 0; i < fileExt.length; i++) {

            String file = fileExt[i];

            assertThat(expectedContents[i], is(httpResponse.createContentTypeHeader(file)));
        }
    }

    @Test
    public void createContentTypeで与えられたファイル拡張子のcontentTypeを判別する() {
        String fileExt[] = {"html", "css", "js", "jpeg", "png", "gif"};
        String expectedContents[] = {"text/html", "text/css", "text/js", "image/jpeg", "image/png", "image/gif"};

        for (int i = 0; i < fileExt.length; i++) {

            String file = fileExt[i];

            assertThat(expectedContents[i], is(httpResponse.createContentType(file)));
        }
    }

    @Test
    public void sendResponseで与えられたレスポンスボディを送る() {
        OutputStream outputStream = new ByteArrayOutputStream();
        HTTPResponse httpResponse = new HTTPResponse(outputStream);

        httpResponse.setResponseBody("レスポンスボディ".getBytes());
        try {
            httpResponse.sendResponse(HTTPResponse.MESSAGE_OK, "html");
        } catch (IOException e) {
            e.printStackTrace();

        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));

        StringBuilder stringBuilder = new StringBuilder();
        String result;

        try {
            result = bufferedReader.readLine();
            while (result != null) {
                stringBuilder.append(result);
                result = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat((new String(stringBuilder)), is("HTTP/1.1 200 OKContent-Type: text/htmlレスポンスボディ"));

    }

    @Test
    public void createReasonPhraseでstatusCodeからreasonPhraseを取得する() {
        int statusCode[] = {200, 400, 404, 500};
        String actual[] = {"OK", "Bad Request", "Not Found", "Internal Server Error"};

        for (int i = 0; i < statusCode.length; i++) {

            int code = statusCode[i];

            assertThat(actual[i], is(httpResponse.createReasonPhrase(code)));
        }
    }

    @Test
    public void makeResponseBodyでファイルが存在するときのレスポンスを取得する() throws IOException {
        String resource[] = {"src/test/resources/index.html", "src/test/resources/next.html", "src/test/resources/すし.html", "src/test/resources/dog.gif", "src/test/resources/index.css"};

        for (int i = 0; i < resource.length; i++) {
            File requestResource = new File(resource[i]);
            FileInputStream fis = new FileInputStream(requestResource);
            BufferedInputStream bi = new BufferedInputStream(fis);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int tmp = 0;
            while ((tmp = bi.read()) != -1) {
                byteArrayOutputStream.write(tmp);

            }
            bi.close();
            byte[] actual = byteArrayOutputStream.toByteArray();
            httpResponse.makeResponseBody(requestResource);
            byte[] expected = httpResponse.getResponseBody();

            assertThat(actual, is(expected));
        }
    }


    @Test
    public void makeResponseBodyでファイルが存在しなかった場合エラーページを取得する() throws IOException {
        File requestResource = new File("");
        httpResponse.makeResponseBody(requestResource);
        byte[] expected = httpResponse.getResponseBody();

        byte[] actual = ErrorPage.makeErrorMessage();

        assertThat(actual, is(expected));
    }

}
