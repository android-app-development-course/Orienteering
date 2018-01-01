package com.scnu.sihao.orienteering.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by SiHao on 2017/12/21.
 *
 */

public class UploadPointUtil {
    String line;
    public  String uploadPoint(String urlStr,int PersonID,int TeamID,String Point){
        String res="";
        HttpURLConnection conn=null;
        String BOUNDARY = UUID.randomUUID().toString().replace("-", "");
        try{
            URL url=new URL(urlStr);
            conn=(HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");

            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out=new DataOutputStream(conn.getOutputStream());

            String fileStr="";
            fileStr+="\r\n"+"--"+BOUNDARY+"\r\n"+"Content-Disposition: form-data; name=\""
                    +"teamid"+"\""+ "\r\n\r\n";
            fileStr+=TeamID;
            fileStr+="\r\n"+"--"+BOUNDARY+"\r\n"+"Content-Disposition: form-data; name=\""
                    +"personid"+"\""+ "\r\n\r\n";
            fileStr+=PersonID;
            fileStr+="\r\n"+"--"+BOUNDARY+"\r\n"+"Content-Disposition: form-data; name=\""
                    +"text"+"\""+ "\r\n\r\n";
            fileStr+=Point;
            out.write(fileStr.getBytes());
            byte[] endData=("\r\n--"+BOUNDARY+"--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 获取服务器数据
            StringBuffer strBuf=new StringBuffer();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while((line=reader.readLine())!=null){
                strBuf.append(line).append("\n");
            }
            res=strBuf.toString();
            System.out.println(res);
            reader.close();
        }catch(Exception e){
            Log.w("warn",e.toString());
            e.printStackTrace();
        }finally {
            if(conn!=null){
                conn.disconnect();
                conn=null;
            }
        }
        return res;
    }
}
