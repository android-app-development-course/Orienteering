package com.scnu.sihao.orienteering.Map.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.scnu.sihao.orienteering.R;

/**
 *  显示当前位置的简单高德地图
 */
public class SimpleMap extends AppCompatActivity {
    MapView mMapView0;
    AMap aMap0;
    public UiSettings mUiSettings;
    MyLocationStyle myLocationStyle;
    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    //声明当前经纬度
    private double currentLat0, currentLon0;
    private AMapLocationClientOption mLocationOption0;
    LatLng currentLatLng0;
    private boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_map);
        mMapView0 = findViewById(R.id.map0);
        mMapView0.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap0 == null) {
            aMap0 = mMapView0.getMap();
            mUiSettings = aMap0.getUiSettings();
            aMap0.getUiSettings().setLogoBottomMargin(-23);  //将LOGO 和 比例尺 往下移
        }
        //初始化定位 监听
        setMyLocation();
        setUpMap();
        mylocation();
    }
    private void setMyLocation(){
//初始化定位
        mLocationClient = new AMapLocationClient(SimpleMap.this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }

    /**
     * 设置自定义定位参数
     */
    private void mylocation(){
/*        mLocationOption0 = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption0.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption0.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption0.setOnceLocation(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption0.setMockEnable(false);*/
        /*设置定位间隔,单位毫秒,默认为2000ms  定位距离拉长 这样距离过滤可以更大 避免定位抖动，获取里程时数据太大
         *  但是太大 距离过滤太大 又会导致定位偏移大，里程数据也会变大
         */
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption0);
        //启动定位
        mLocationClient.startLocation();
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 显示比例尺
        mUiSettings.setScaleControlsEnabled(true);
        // 显示指南针
        mUiSettings.setCompassEnabled(true);
        // 显示定位按钮    直接用SDK的定位

        // 初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒
        myLocationStyle.interval(2500);

        //设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数 这里还是要箭头的因为不加陀螺仪了
       // BitmapDescriptor bitmaplocation0 = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(SimpleMap.this.getResources(), R.drawable.location_3));
        //myLocationStyle.myLocationIcon(bitmaplocation0);
        // 为什么绘制出来的有问题？
        //myLocationStyle.strokeColor(Color.parseColor("#6E6E6E") );//设置定位蓝点精度圆圈的边框颜色的方法int color
        //myLocationStyle.radiusFillColor(Color.parseColor("#7D26CD"));//设置定位蓝点精度圆圈的填充颜色的方法。int color
        // myLocationStyle.strokeWidth(100);//设置定位蓝点精度圈的边框宽度的方法
        aMap0.setMyLocationStyle(myLocationStyle);
        aMap0.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
        aMap0.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，默认不显示。

    }

    /**
     * 声明定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            // 从这里开始就会持续回调
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    currentLat0 = amapLocation.getLatitude();//获取纬度
                    currentLon0 = amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    currentLatLng0 = new LatLng(currentLat0, currentLon0);
                    // 第一次移至中心 第二次就不要
                    if(flag) {
                        // 待完善 将movecamera放到点击定位按钮 以及第一次打开Activity 中
                        aMap0.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng0, 16));
                        Log.i("currentLatLng0", String.valueOf(currentLatLng0));
                        flag=false;
                    }
                }
            }
        }
    };
}