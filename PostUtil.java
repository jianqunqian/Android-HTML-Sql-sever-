package com.makeqianjianqun.artfere.trivial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 19212 on 2019/3/18.
 */

public class PostUtil {
    public static String sendPost(String url,String params){
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try{
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept","*/*");
            conn.setRequestProperty("connection","Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0(compatible;MSIE 6.0;Window NT 5.1;sv1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(params);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = in.readLine()) != null){
                result +="\n"+line;
            }
        }
        catch (Exception e){
            System.out.println("发送POST请求出现异常" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
