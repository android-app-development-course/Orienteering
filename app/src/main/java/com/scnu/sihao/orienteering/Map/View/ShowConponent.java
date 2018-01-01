package com.scnu.sihao.orienteering.Map.View;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;

import com.amap.api.maps.UiSettings;
/**
 * Created by SiHao on 2017/10/23.
 *
 */
public class ShowConponent {
   private Button innerButton3;
   public SensorManager mSensorManager;
   public Sensor accelerometer; // 加速度传感器
   public Sensor magnetic; // 地磁场传感器
   public float[] accelerometerValues = new float[3];
   public float[] magneticFieldValues = new float[3];
   public float[] calValues = new float[3];
   public RotateAnimation ra;
   MySensorEventListener accelerometerSensor;
   MySensorEventListener magneticSensor;
   // 记录指南针图片转过的角度
   public float currentDegree = 0f;
   //显示比例尺
   void showScale(UiSettings uiSettings1){
      uiSettings1.setScaleControlsEnabled(true);
   }
   //显示指南针
   void showCompass(UiSettings uiSettings2) {
      uiSettings2.setCompassEnabled(true);
   }
   // 显示陀螺仪
   public void showCompass2(Button compass3button,Context mcontext){
     // 获取Button给内部类
      innerButton3=compass3button;
      //初始化陀螺仪传感器，注册回调函数
      // 实例化传感器管理者
      mSensorManager = (SensorManager) mcontext.getSystemService(Context.SENSOR_SERVICE);
      // 初始化加速度传感器
      accelerometer = mSensorManager
              .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      // 初始化地磁场传感器
      magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
   }
   class MySensorEventListener implements SensorEventListener {
      //实现陀螺仪状态变化回调函数
      @Override
      public void onSensorChanged(SensorEvent event) {
         // TODO Auto-generated method stub
         if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values;

         }
         if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = event.values;
         }
         calculateOrientation();
         // 获取绕Z轴转过的角度
         float degree = calValues[0];
         // 创建旋转动画（反向转过degree度）
         ra = new RotateAnimation(currentDegree, -degree,
                 Animation.RELATIVE_TO_SELF, 0.5f,
                 Animation.RELATIVE_TO_SELF, 0.5f);
         // 设置动画的持续时间
         ra.setDuration(200);
         // 设置动画结束后的保留状态
         ra.setFillAfter(true);
         // 启动动画
         innerButton3.startAnimation(ra);
         currentDegree = -degree;
         Log.i("compass3", String.valueOf(currentDegree));
      }
      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {
         // TODO Auto-generated method stub
      }
   }
   // 计算方向
   private void calculateOrientation() {
      float[] R = new float[9];
      SensorManager.getRotationMatrix(R, null, accelerometerValues,
              magneticFieldValues);
      SensorManager.getOrientation(R, calValues);
      calValues[0] = (float) Math.toDegrees(calValues[0]);
   }
}

