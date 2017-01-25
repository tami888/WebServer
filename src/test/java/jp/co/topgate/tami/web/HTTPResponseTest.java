package jp.co.topgate.tami.web;


import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by honmatakumi on 2017/01/24.
 */
public class HTTPResponseTest {

    private OutputStream outputStream = new ByteArrayOutputStream();
    private HTTPResponse httpResponse = new HTTPResponse(outputStream);

    @Test
    public void testContentType(){
        String fileExt[] = {"html", "css", "js", "jpeg", "png", "gif"};
        String expectedContents[] = {"Content-Type: text/html", "Content-Type: text/css", "Content-Type: text/javascript", "Content-Type: image/jpeg", "Content-Type: image/png", "Content-Type: image/gif"};

        for (int i = 0; i < fileExt.length; i++) {

            String file =  fileExt[i];

            assertEquals(expectedContents[i], httpResponse.contentType(file));
        }
    }
}
