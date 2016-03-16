package com.example.boria.httpdemo_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.os.Handler;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Boria on 2015/10/5.
 */
public class HttpThread extends Thread{
    private String url;
    private WebView webView;
    private Handler handler;
    private ImageView imageView;

//    public HttpThread(String url, WebView webView,Handler handler) {
//        this.handler = handler;
//        this.url = url;
//        this.webView = webView;
//    }
    public HttpThread(String url, ImageView imageView,Handler handler) {
        this.handler = handler;
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    public void run() {
        try {
            URL Httpurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) Httpurl.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            InputStream in = conn.getInputStream();
            FileOutputStream out = null;
            File downloadFile = null;
            String fileName = String.valueOf(System.currentTimeMillis());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File parent = Environment.getExternalStorageDirectory();
                downloadFile = new File(parent,fileName);
                out = new FileOutputStream(downloadFile);
            }
            byte[] b = new byte[2048];
            int len;
            if (out!=null){
                while ((len=in.read(b))!=-1){
                    out.write(b,0,len);
                }
            }
            final Bitmap bitmap = BitmapFactory.decodeFile(downloadFile.getAbsolutePath());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);

                }
            });








//            final StringBuffer sb = new StringBuffer();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//            String str;
//            while ((str=reader.readLine())!=null) {
//                sb.append(str);
//            }
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    webView.loadData(sb.toString(),"text/html;charset=utf-8",null);
//
//                }
//            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
