package com.scnu.sihao.orienteering.Utils;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SiHao on 2017/11/4.
 *
 */

public class OKHttp {
    public String str;
    private Handler okhttpHandler;
   private String baseUrl="http://45.32.72.80/";
    //1 创建OKHttpClient对象 全局声明即可
    OkHttpClient okHttpClient = new OkHttpClient();

    public void getHandler(Handler handler){
        this.okhttpHandler=handler;
    }
    // OKHttp的get方法   通过string 参数传递进行封装
    public void doGet(String string) throws IOException {
        //2 创建Request.Builder
        Request.Builder builder = new Request.Builder();
        // 设置为get方式 设置请求的网址
        Request request = builder.get().url(baseUrl+string).build();
        call(request);
        Log.i("do get is run",baseUrl+string);
    }
    private void call(Request request) {
        //3 request封装为call
        Call call =  okHttpClient.newCall(request);
        //4 直接执行call  可以通过response来获取他的返回值 获取信息
        //Response response = call.execute();
        //4 异步执行call
        call.enqueue(new Callback(){
                         // 错误返回的方法
                         @Override
                         public void onFailure(Call call, IOException e) {
                             Log.i("OkHttpOnfailure",String.valueOf(e));
                         }
                         // 正确回应返回的方法
                         @Override
                         public void onResponse(Call call, Response response) throws IOException {
                             Log.i("onResponse1","onResponse");
                             if(response.body()!=null) {
                                 str = response.body().string();
                                 Log.i("onResponse2", str);
                                 String str3 = str;
                                 Log.i("HttpOnResponse", str3);
                                 if(okhttpHandler!=null) {
                                     okhttpHandler.sendEmptyMessage(0);
                                 }
                             }else{
                                 Log.i("onResponse3", "onResponse but responseBody is null!");

                             }
                         }
                     }
        );
    }
}
