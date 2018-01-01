package com.scnu.sihao.orienteering.Map.View;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.scnu.sihao.orienteering.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiHao on 2017/10/23.
 *
 */
public class SetMyLocation {
    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    //声明当前经纬度
    private double currentLat,currentLon;
    // 当前位置 上一个位置
    LatLng currentLatLng,oldLatLng;
    boolean canlocated=true;
    private Marker forcelocation;
    private MarkerOptions markerOptions_1;
    boolean canmovetocentre=false;
    private AMapLocationClientOption mLocationOption;
    // 约束3S内移动的距离
    private double meter;
    public double totalMeter;
    // 根据移动的距离加入到MovedArray中 去绘制运动轨迹
    private double countMoved=0;
    private AMap thismap;
    private Context thiscontext;

    private static boolean canMesureMeter=false;
    // 将运动轨迹加入到List中
    public static List<LatLng> movedArray = new ArrayList<>();
    /**
     * 设置自定义定位按钮并自己实现定位功能
     */
    void myDesignLocation(AMap mymap, Context mcontext){
        thismap=mymap;
        thiscontext=mcontext;
        //初始化定位 监听
        setMyLocation();
        //各项定位功能实现
        mylocation();
    }
    private void setMyLocation(){
//初始化定位
        mLocationClient = new AMapLocationClient(thiscontext);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
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
                    currentLat = amapLocation.getLatitude();//获取纬度
                    currentLon = amapLocation.getLongitude();//获取经度
                    currentLatLng = new LatLng(currentLat,currentLon);

                    amapLocation.getAccuracy();//获取精度信息
                    Log.i("mylocation","currentLat : "+currentLat+" currentLon : "+currentLon);
                    //避免点击多次定位按钮 定位多次
                    canlocated=false;
                    //避免点一次定位 就绘制一个点 初始化了图片 在else中将图片位置换了就好
                    if (forcelocation == null) {
                        markerOptions_1 = new MarkerOptions();
                        markerOptions_1.position(currentLatLng);
                        markerOptions_1.visible(true);
                        //设置图标
                        BitmapDescriptor bitmaplocation3 = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(thiscontext.getResources(),
                                R.drawable.location_3));
                        markerOptions_1.icon(bitmaplocation3);
                        forcelocation = thismap.addMarker(markerOptions_1);
                        //移至中心 并将canmovetocentre设置为true
                        canmovetocentre = true;
                        thismap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
                        // 第一次定位 将current 赋值给old
                        oldLatLng=currentLatLng;
                    } else {

                        // 统计当前位置的移动距离
                        mesureOldNew(oldLatLng, currentLatLng);
                        Log.i("111111111111111111", String.valueOf(meter));
                        // 设置定位距离过滤为2.5m以上 减少绘制次数
                        if(meter>=2.5) {
                            forcelocation.setPosition(currentLatLng);
                        }
                    }
                    // 若成功打了第一个点为true  若成功打了最后一个点为false
                    if(MapActivity.canMesureMeter){
                        //调用计算总里程的方法
                        mesureMeter(oldLatLng,currentLatLng);

                        // 调用统计移动距离方法
                        addMovedArray();

                        //约束每移动15m 才去加入当前位置到运动轨迹点组
                        if(countMoved>=15.0){
                            countMoved=0;
                            movedArray.add(currentLatLng);
                            //Log.i("movedArray", String.valueOf(getMovedLngLat.movedArray));

                        }
                    }// 若成功打了最后一个点为false
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    /**
     * 设置自定义定位参数
     */
    private void mylocation(){
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        /*设置定位间隔,单位毫秒,默认为2000ms  定位距离拉长 这样距离过滤可以更大 避免定位抖动，获取里程时数据太大
         *  但是太大 距离过滤太大 又会导致定位偏移大，里程数据也会变大
         */
        mLocationOption.setInterval(3000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    private void mesureOldNew(LatLng old, LatLng current) {
        // 若旧的经纬度和新的不一样
        if (old != current) {
            meter = AMapUtils.calculateLineDistance(old, current);
        }
    }
    /**
     *  计算总里程
     */
    private void mesureMeter(LatLng oldone, LatLng currentone){
        mesureOldNew(oldone,currentone);
        /**针对步行 跑步 设定的3S距离限制 可以避免定位次数太多，细微抖动累积导致总里程计算偏差大
         *  但是太大 距离过滤太大 又会导致定位偏移大，里程数据也会变大
         **/
        if(meter<=21.0&&meter>3.0) {
            totalMeter = totalMeter + meter;
            Log.i("totalMeter", String.valueOf(totalMeter));
        }
    }
    /**
     *  将当前位置的latlng加入到MovedArray中
     *  设置距离器 countMoved 初始值为0
     *然后每次定位都执行一次countMoved =countMoved + meter（每次定位的新旧定位距离差）
     *再判断一次if(countMoved>=15.0){ 再将countMoved置为0 去将当前坐标LatLng赋予MovedArray}
     */

    private void addMovedArray(){
        countMoved=meter+countMoved;
    }
}