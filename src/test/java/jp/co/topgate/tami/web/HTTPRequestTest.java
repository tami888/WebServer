package jp.co.topgate.tami.web;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class HTTPRequestTest {

    String socketContents = "GET /index.html HTTP/1.1\n" + "Host: localhost:8080\n" + "Connection: keep-alive\n"
            + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36\n"
            + "Accept: */*\n" + "Referer: http://localhost:8080/\n" + "Accept-Encoding: gzip, deflate, sdch, br\n"
            + "Accept-Language: ja,en-US;q=0.8,en;q=0.6\n";


    String socketContents2 = "GET /index.js HTTP/1.1\n" + "Host: localhost:8080\n" + "Connection: keep-alive\n"
            + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36\n"
            + "Accept: */*\n" + "Referer: http://localhost:8080/\n" + "Accept-Encoding: gzip, deflate, sdch, br\n"
            + "Accept-Language: ja,en-US;q=0.8,en;q=0.6\n";




    String socketContentsArray[] = { socketContents, socketContents2};

    /**
     * getRequestMethodメソッドをテストするメソッド
     */
    @Test
    public void getRequestMethodでinputStreamからリクエストメソッドを取得() throws Exception {
        for (String socketContents : socketContentsArray) {
            InputStream inputStream = new ByteArrayInputStream(socketContents.getBytes());
            HTTPRequest httpRequest = new HTTPRequest(inputStream);

            assertEquals("GET", httpRequest.getRequestMethod());
        }
    }

    /**
     * getRequestURIメソッドをテストするメソッド
     */
    @Test
    public void getRequestURIでパスが与えたときリクエストURIを取得() throws Exception {

        String expectedRequestURI = "/index.html";
        String expectedRequestURI2 = "/index.js";

        String expectedRequestURIArray[] = { expectedRequestURI, expectedRequestURI2};

        for (int i = 0; i < socketContentsArray.length; i++) {
            InputStream inputStream = new ByteArrayInputStream(socketContentsArray[i].getBytes());
            HTTPRequest httpRequest = new HTTPRequest(inputStream);

            String requestURI = httpRequest.getRequestURI();

            assertEquals(expectedRequestURIArray[i], requestURI);
        }
    }

    @Test
    public void getRequestResourceでURIからリクエストリソースを取得() throws Exception {

        String expected[] = {"/index.html", "/index.js"};

        IntStream.range(0, expected.length).forEach(i -> {
            InputStream inputStream = new ByteArrayInputStream(socketContentsArray[i].getBytes());
            HTTPRequest httpRequest = null;
            try {
                httpRequest = new HTTPRequest(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            assertEquals(expected[i], httpRequest.getRequestURI());

        });

    }

    @Test
    public void getRequestResourceExtensionでファイル拡張子を取得() {

        String expected[] = {"html", "js"};

        IntStream.range(0, expected.length).forEach(i -> {
            InputStream inputStream = new ByteArrayInputStream(socketContentsArray[i].getBytes());
            HTTPRequest httpRequest = null;
            try {
                httpRequest = new HTTPRequest(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }

            File file = httpRequest.getRequestResource();


            assertEquals(expected[i], httpRequest.getRequestResourceExtension(file));

        });


    }

}
