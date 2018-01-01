package com.scnu.sihao.orienteering.Map.View;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.scnu.sihao.orienteering.Entity.GetMarkerLatLngBean0;
import com.scnu.sihao.orienteering.Utils.OKHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiHao on 2017/10/23.
 *
 */

public class GetMarkerLngLat extends Thread{
    List<LatLng> latLngArrays = new ArrayList<>();
    public double[][] markerLngLat;
    private Handler myHandler3;
    OKHttp okHttpForMarker = new OKHttp();
    // 获取那边传过来的地图ID
    private int myMapID;
    private int myType;
    public GetMarkerLngLat(int type,int mapid){
        this.myType=type;
        this.myMapID=mapid;
    }
    public void run(){
        Log.i("try is run1","try is run");
        try {
            Log.i("try is run2","try is run");
            okHttpForMarker.getHandler(handlerForGetMarker);
            okHttpForMarker.doGet("getmappoints/?ID="+myMapID+"&Type="+myType);
            Log.i("do the get","do the get");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化标记点的经纬度坐标方法
     */
    void setMarkerLngLat(Handler handler){
        this.myHandler3=handler;
        Log.i("setMarkerLngLat is run ","setMarkerLngLat is run");
        // 先自己调用赋值
        //getMapID();
       //自测试获取标记点坐标等属性
       // setLatLngs();
    }
    // 让那边给该方法传值，现在自己测试就不用写形参先
/*    public void getMapID(){
        MapID=6663;
        Type=0;
    }*/

    // 再建一个handler去请求获取数据
    @SuppressLint("HandlerLeak")
    Handler handlerForGetMarker = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    String str=okHttpForMarker.str;
                    // Bean解析
                    Gson gson = new Gson();
                    GetMarkerLatLngBean0 getMarkerLngLatBean0= gson.fromJson(str, GetMarkerLatLngBean0.class);
                    markerLngLat=getMarkerLngLatBean0.getPoint();

                    if(myHandler3!=null) {
                        myHandler3.sendEmptyMessage(2);
                    }
                    break;
                }

            }
        }
    };


    public List<LatLng> getLatLngArraysForAddLine(){
        // 遍历二维数组 不断将第一维赋予x1[] 对一维数组x1进行遍历（这里相当于遍历的是二维数组的第二维
        //用于给addLine去加虚线
        for (double[] x1 : markerLngLat) {
            latLngArrays.add(new LatLng(x1[0],x1[1]));
        }
        return latLngArrays;
    }

    private void setLatLngs(){

        markerLngLat =new double[6][2];
        //西三
        markerLngLat[0][0]= 23.14030;
        markerLngLat[0][1]= 113.34658023003472;
        //桃李园
        markerLngLat[1][0]= 23.13940;
        markerLngLat[1][1]= 113.34661023003472;
        //一课
        markerLngLat[2][0]= 23.13830;
        markerLngLat[2][1]= 113.34788023003472;
        // 行政楼前
        markerLngLat[3][0]= 23.13940;
        markerLngLat[3][1]= 113.34913023003472;
        //田径场
        markerLngLat[4][0]= 23.140800;
        markerLngLat[4][1]= 113.34848023003482;
        //星河楼
        markerLngLat[5][0]= 23.142100;
        markerLngLat[5][1]= 113.34818023003482;
    }
      /*  //
        markerLngLat[0][0]= 23.138000;
        markerLngLat[0][1]= 113.34968023003862;
        //
        markerLngLat[1][0]= 23.138000;
        markerLngLat[1][1]= 113.34668023003482;
        //
        markerLngLat[2][0]= 23.14000;
        markerLngLat[2][1]= 113.34668023003472;
        //
        markerLngLat[3][0]= 23.14000;
        markerLngLat[3][1]= 113.34968023003472;*/


        //星河楼
 /*       markerLngLat[0][0]= 23.142100;
        markerLngLat[0][1]= 113.34818023003482;*/
        //田径场
     /*   markerLngLat[1][0]= 23.140800;
        markerLngLat[1][1]= 113.34848023003482;*/
    /*    //一课
        markerLngLat[2][0]= 23.13830;
        markerLngLat[2][1]= 113.34788023003472;*/
        //西三
/*        markerLngLat[2][0]= 23.14030;
        markerLngLat[2][1]= 113.34658023003472;*/
        // 沁园
       /* markerLngLat[2][0]= 23.13910;
        markerLngLat[2][1]= 113.35350023003472;*/
        //手球馆
      /*  markerLngLat[3][0]= 23.14050;
        markerLngLat[3][1]= 113.34968023003472;*/
        // 行政楼前
   /*     markerLngLat[3][0]= 23.13940;
        markerLngLat[3][1]= 113.34913023003472;*/
        // 文化广场
   /*     markerLngLat[3][0]= 23.13960;
        markerLngLat[3][1]= 113.35078023003472;*/
        // 陶园
  /*      markerLngLat[3][0]= 23.13860;
        markerLngLat[3][1]= 113.35080023003472;*/

  // 测试地图1 星河楼-> 田径场->行政楼前->西三

}
