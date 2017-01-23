package jp.co.topgate.tami.web;



import java.io.File;

import java.io.IOException;


public class Handler {

    public static void handleGET(HTTPRequest httpRequest, HTTPResponse httpResponse) throws IOException {

        System.out.println("GETハンドルに移行しました");
        System.out.println("requestURIは"+ httpRequest.getRequestURI());



        String userDir;
        userDir = System.getProperties().getProperty("user.dir");
        File file =new File(userDir + "/src/main/resource"+ httpRequest.getRequestURI() ); //リクエストURI
        System.out.println("fileは" + file);



        if (file.exists()) {
            httpResponse.setStatusLine("HTTP/1.1 200 OK");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("レスポンスを送ります");
            httpResponse.response(file);
        } else {
            httpResponse.setStatusLine("HTTP/1.1 404 Not Found");
            System.out.println(httpResponse.getStatusLine());
            file =new File(userDir + "/src/main/resource/error.html");
            httpResponse.responseF(file);
        }



//        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            byte line[] = new byte[500];
//            int i;
//            while (( i = fileInputStream.read(line,0,500)) != -1 ) {
//                httpResponse.getOutputStream().write(line,0,i);
//            }
//            httpResponse.getOutputStream().flush();
//            fileInputStream.close();
//        }catch(IOException e){
//            System.out.println("\n** send_File() IOException **");
//            e.printStackTrace();
//        }




//         String uri;
//         uri =  httpRequest.getRequestURI();
//
//         httpResponse.sendBody(uri);


    }



}
