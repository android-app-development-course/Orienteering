package com.scnu.sihao.orienteering.Map.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.scnu.sihao.orienteering.Map.ChildThread.ChildThreadForGetMoved;
import com.scnu.sihao.orienteering.R;

import java.util.ArrayList;
import java.util.List;

public class ShowTrailActivity extends Activity {
    MapView mMapView2;
    AMap aMap2;
    public UiSettings mUiSettings;
    // 获取传过来的
    private int personID=52920040;
    private int matchID=14948;
    // 移动轨迹List点组
    public static List<LatLng> getMovedArrayTest = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trail);
        mMapView2 = findViewById(R.id.map2);
        mMapView2.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap2 == null) {
            aMap2 = mMapView2.getMap();
            mUiSettings = aMap2.getUiSettings();
            aMap2.getUiSettings().setLogoBottomMargin(-23);  //将LOGO 和 比例尺 往下移
        }
        testMovedArray();
        setUpMap();
    }


    private void testMovedArray(){
        getMovedArrayTest.add(new LatLng(23.1403, 113.34658023003472));
        getMovedArrayTest.add(new LatLng(23.1401, 113.34688023003472));
        getMovedArrayTest.add(new LatLng(23.1400, 113.34688023003472));
        getMovedArrayTest.add(new LatLng(23.1398, 113.34698023003472));
        getMovedArrayTest.add(new LatLng(23.1396, 113.34708023003472));
        getMovedArrayTest.add(new LatLng(23.1395, 113.34698023003472));
        getMovedArrayTest.add(new LatLng(23.1395, 113.34702023003472));
        getMovedArrayTest.add(new LatLng(23.1395, 113.34683023003472));
        getMovedArrayTest.add(new LatLng(23.1394, 113.34671023003472));


        getMovedArrayTest.add(new LatLng(23.1394, 113.34691023003471));
        getMovedArrayTest.add(new LatLng(23.1393, 113.34701023003471));
        getMovedArrayTest.add(new LatLng(23.1392, 113.3472023003471));
        getMovedArrayTest.add(new LatLng(23.1391, 113.34711023003471));
        getMovedArrayTest.add(new LatLng(23.1388, 113.34731023003471));
        getMovedArrayTest.add(new LatLng(23.1385, 113.34759023003471));
        getMovedArrayTest.add(new LatLng(23.1382, 113.34790023003471));



        getMovedArrayTest.add(new LatLng(23.1383, 113.34795023003472));
        getMovedArrayTest.add(new LatLng(23.1385, 113.34815023003472));
        getMovedArrayTest.add(new LatLng(23.1388, 113.34830023003472));
        getMovedArrayTest.add(new LatLng(23.1389, 113.34868023003472));
        getMovedArrayTest.add(new LatLng(23.1390, 113.34883023003472));
        getMovedArrayTest.add(new LatLng(23.1391, 113.34876023003472));
        getMovedArrayTest.add(new LatLng(23.1392, 113.34892023003472));
        getMovedArrayTest.add(new LatLng(23.1393, 113.34908023003472));



        getMovedArrayTest.add(new LatLng(23.1394, 113.348853023003472));
        getMovedArrayTest.add(new LatLng(23.1395, 113.34895023003472));
        getMovedArrayTest.add(new LatLng(23.1398, 113.34902023003472));
        getMovedArrayTest.add(new LatLng(23.1399, 113.3488423003472));
        getMovedArrayTest.add(new LatLng(23.1400, 113.34820023003472));


        getMovedArrayTest.add(new LatLng(23.1408, 113.34848023003482));
        getMovedArrayTest.add(new LatLng(23.1410, 113.34840023003482));
        getMovedArrayTest.add(new LatLng(23.1412, 113.34835023003482));
        getMovedArrayTest.add(new LatLng(23.1416, 113.34838023003482));
        getMovedArrayTest.add(new LatLng(23.1418, 113.34842023003482));


        getMovedArrayTest.add(new LatLng(23.1421, 113.34818023003481));

    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 显示比例尺
        mUiSettings.setScaleControlsEnabled(true);
        // 显示指南针
        mUiSettings.setCompassEnabled(true);
        // 将当前视野移至中间下标处
        aMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(getMovedArrayTest.get(getMovedArrayTest.size()/2), 17));
        addLine();
    }

    private void addLine () {
        // 绘制线条
        Polyline polyline;
        polyline = aMap2.addPolyline(new PolylineOptions().
                addAll(getMovedArrayTest).width(8).color(Color.argb(200, 230, 63, 47)));//这里是0-255

        //设置是否画虚线，默认为false，画实线
        polyline.setDottedLine(false);
    }
}