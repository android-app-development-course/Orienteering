package com.scnu.sihao.orienteering.Map.MapUtils;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.scnu.sihao.orienteering.Map.View.GetMarkerLngLat;

public class AddMarkerLine {
    // 绘制线条
    Polyline polyline;
    public void addline(AMap mymap,GetMarkerLngLat getlnglat){
        polyline = mymap.addPolyline(new PolylineOptions().
                addAll(getlnglat.getLatLngArraysForAddLine()).width(8).color(Color.argb(200, 230, 63, 47)));//这里是0-255
        //设置是否画虚线，默认为false，画实线。
        polyline.setDottedLine(true);
    }
}
