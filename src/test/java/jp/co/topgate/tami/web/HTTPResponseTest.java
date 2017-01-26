package jp.co.topgate.tami.web;


import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

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
        String expectedContents[] = {"Content-Type: text/html\n", "Content-Type: text/css\n", "Content-Type: text/js\n", "Content-Type: image/jpeg\n", "Content-Type: image/png\n", "Content-Type: image/gif\n"};

        for (int i = 0; i < fileExt.length; i++) {

            String file =  fileExt[i];

            assertThat(expectedContents[i], is(httpResponse.contentType(file)));
        }
    }
}
