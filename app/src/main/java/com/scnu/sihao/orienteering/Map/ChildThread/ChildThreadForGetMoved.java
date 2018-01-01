package com.scnu.sihao.orienteering.Map.ChildThread;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.scnu.sihao.orienteering.Entity.GetMovedLatLngBean;
import com.scnu.sihao.orienteering.Utils.OKHttp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiHao on 2017/12/7.
 *
 */

public class ChildThreadForGetMoved extends Thread{
    private int myPersonID;
    private int myMatchID;
    private List<Double> latitude=new ArrayList<>();
    private List<Double> longitude=new ArrayList<>();
    private Handler myHandlerForGetMoved;
    OKHttp okHttpForMovedLngLat = new OKHttp();
    // 移动轨迹List点组
    public static List<LatLng> getMovedArray = new ArrayList<>();

    public ChildThreadForGetMoved(Handler handler){
        this.myHandlerForGetMoved= handler;
    }
    public void getPersonID(int personID){
        this.myPersonID=personID;
    }
    public void getMyMatchID(int matchID){
        this.myMatchID=matchID;
    }
    // 创建子线程去网络请求获取移动轨迹点组
    public void run() {
    try{
        // 先给okhttp传一个handler
        okHttpForMovedLngLat.getHandler(handlerForOkHttpGetMoved);
        okHttpForMovedLngLat.doGet("getrecodingpoint/?MatchID="+myMatchID+"&PersonID="+myPersonID);
       //这里要okhttp获取了数据  才去赋值 通过下方的handler

    }catch (Exception e) {
        e.printStackTrace();
    }
    }

    @SuppressLint("HandlerLeak")
    Handler handlerForOkHttpGetMoved = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    String str= okHttpForMovedLngLat.str;
                    //解析json字符串
                    Gson gson = new Gson();
                    GetMovedLatLngBean[] getMovedLatLngBean= gson.fromJson(str,GetMovedLatLngBean[].class);
                    // 遍历
                    for(int i=0;i<getMovedLatLngBean.length;i++){
                        latitude.add(getMovedLatLngBean[i].latitude);
                        longitude.add(getMovedLatLngBean[i].longitude);
                        Log.i("latitude111", String.valueOf(latitude.get(i)));
                        Log.i("longitude111", String.valueOf(longitude.get(i)));
                        getMovedArray.add(new LatLng(latitude.get(i),longitude.get(i)));
                    }

                    // 还要把getMovedArray传递过去给Activity
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    // 取中间值 给视野移至中心
                    int movedArrayLength = getMovedArray.size();
                    int movedArrayCentre = movedArrayLength / 2;
                    Log.i("movedArrayCentre", String.valueOf(movedArrayCentre));
                    // 将中间下标传过去
                    bundle.putInt("values", movedArrayCentre);
                    message.setData(bundle);
                    myHandlerForGetMoved.sendMessage(message);//发送message信息
                    message.what=0;
                    break;
                }

            }
        }
    };

}


