package jp.co.topgate.tami.web;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class HTTPRequestTest {

    String socketContents = "GET /index.html HTTP/1.1\n" + "Host: localhost:8080\n" + "Connection: keep-alive\n"
            + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36\n"
            + "Accept: */*\n" + "Referer: http://localhost:8080/\n" + "Accept-Encoding: gzip, deflate, sdch, br\n"
            + "Accept-Language: ja,en-US;q=0.8,en;q=0.6\n";


    String socketContents2 = "GET /.sample/sample.html HTTP/1.1\n" + "Host: localhost:8080\n" + "Connection: keep-alive\n"
            + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36\n"
            + "Accept: */*\n" + "Referer: http://localhost:8080/\n" + "Accept-Encoding: gzip, deflate, sdch, br\n"
            + "Accept-Language: ja,en-US;q=0.8,en;q=0.6\n";




    String socketContentsArray[] = { socketContents, socketContents2};

    /**
     * getRequestMethodメソッドをテストするメソッド
     */
    @Test
    public void getRequestMethodでinputStreamからリクエストメソッドを取得() throws Exception {
        for (int i = 0; i < socketContentsArray.length; i++) {
            InputStream inputStream = new ByteArrayInputStream(socketContentsArray[i].getBytes());
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
        String expectedRequestURI2 = "/.sample/sample.html";

        String expectedRequestURIArray[] = { expectedRequestURI, expectedRequestURI2};

        for (int i = 0; i < socketContentsArray.length; i++) {
            InputStream inputStream = new ByteArrayInputStream(socketContentsArray[i].getBytes());
            HTTPRequest httpRequest = new HTTPRequest(inputStream);

            String requestURI = httpRequest.getRequestURI();
            System.out.println("リクエストURIは" + requestURI);

            assertEquals( expectedRequestURIArray[i], requestURI);
        }

    }
}
