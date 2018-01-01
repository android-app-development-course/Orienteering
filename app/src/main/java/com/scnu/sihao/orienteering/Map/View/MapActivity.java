package com.scnu.sihao.orienteering.Map.View;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.scnu.sihao.orienteering.Map.AnswerQuestion.AnswerDialog;
import com.scnu.sihao.orienteering.Map.AnswerQuestion.MesureCal;
import com.scnu.sihao.orienteering.Map.ChildThread.ChildThreadForMap;
import com.scnu.sihao.orienteering.Map.ChooseMarker;
import com.scnu.sihao.orienteering.Map.MapUtils.AddMarkerLine;
import com.scnu.sihao.orienteering.Map.MapUtils.IsGPSNetEnable;
import com.scnu.sihao.orienteering.R;
import com.scnu.sihao.orienteering.Service.GetGTIntentService;
import com.scnu.sihao.orienteering.register.RegisterUtils.SystemBarTintManager;
import com.scnu.sihao.orienteering.register.View.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SiHao on 2017/9/25.
 *
 */
public class MapActivity extends Activity{
    MapView mMapView;
    AMap aMap;
    public UiSettings mUiSettings;
    private  AlertDialog.Builder dialog1;
    private Button markerSelect,back1,compass3Button,buttonMark,mylocationbutton;
    private Button compass2Button,truebutton,falsebutton;
    public View dialogView;
    private MarkerOptions markerOptions_2;
    private boolean isGPSworked,isNetWorked,notHere=true;
    public static boolean canMesureMeter=false;
    // 标记点的位置
    public LatLng[] latLng ;
    // 记录总里程
    private int countmarker=0;
    // 进度条
    private ProgressBar progressbar;
    // 答题对话框
    private  AnswerDialog answerDialog;
    // 队伍ID
    private int TeamID;
    //个人ID
    private int PersonID;
    //比赛类型
    private int Type;
    // 地图ID
    private int MapID;
    // 标记点的下标
    private int currentMarkerIndex;
    //个推的clientID
    private String clientID;
    // 外部类获取
    GetMarkerLngLat getMarkerLngLat;
    ShowConponent showConponent = new ShowConponent();
    SetMyLocation setMyLocation = new SetMyLocation();
    public MapActivity() {}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 获取intent过来的值
        Intent intent =getIntent();
        // 先获取标识
        String biaoshi= intent.getStringExtra("biaoshi");
        // 若是从创建比赛传过来的
        if(biaoshi.equals("A")){
            // 获取type
            String typeString= intent.getStringExtra("type");
            Type=Integer.valueOf(typeString);
            // 获取Mapid
            String mapidString= intent.getStringExtra("mapid");
            MapID=Integer.valueOf(mapidString);
        }else{
            // 若是从加入比赛创建的

            // 获取type
            String typeString= intent.getStringExtra("type");
            Type=Integer.valueOf(typeString);
            // 获取Mapid
            String mapidString= intent.getStringExtra("mapid");
            MapID=Integer.valueOf(mapidString);
            // 获取userid
            String useridString= intent.getStringExtra("userid");
            PersonID=Integer.valueOf(useridString);
            // 获取TeamID
            String teamidString= intent.getStringExtra("teamid");
            TeamID=Integer.valueOf(teamidString);
            // 获取matchid
         /*   String matchidString= intent.getStringExtra("matchid");
            MatchID=Integer.valueOf(matchidString);*/
        }

        // 外部类获取  再传入MAPID和Type给getMarkerLngLat
        getMarkerLngLat = new GetMarkerLngLat(Type,MapID);
        getMarkerLngLat.start();
        // 先传handler过去
        getMarkerLngLat.setMarkerLngLat(handler2);
        // 传Handler给GetGTIntent
        GetGTIntentService.getHandler(handler2);


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏 继承Activity才可以生效
        setContentView(R.layout.map_activity); // 调用相应的XML文件进行界面，控件布局
        // 通知栏颜色更改
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.springgreen);//通知栏所需颜色
        }
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        markerSelect=findViewById(R.id.marker_Select);
        markerSelect.setOnClickListener(listener);
        markerSelect.getBackground().setAlpha(0);
        back1=findViewById(R.id.back_1);
        back1.setOnClickListener(listener);
        back1.getBackground().setAlpha(0);
        compass2Button=findViewById(R.id.compass2_Button);
        compass2Button.setOnClickListener(listener);
        compass3Button=findViewById(R.id.compass3_button);
        compass3Button.setOnClickListener(listener);
        compass3Button.setVisibility(View.INVISIBLE);
        mylocationbutton=findViewById(R.id.button_mylocation);
        mylocationbutton.getBackground().setAlpha(200);//0~255透明度值 数值越小越透明
        mylocationbutton.setOnClickListener(listener);
        //buttonMark=findViewById(R.id.button_mark);
      //  buttonMark.setOnClickListener(listener);
        progressbar=findViewById(R.id.progress_bar);
        // 自定义dialog 的 View
         dialogView = LayoutInflater.from(MapActivity.this)
                .inflate(R.layout.answerdialog,null);
        truebutton=dialogView.findViewById(R.id.true_button);
        truebutton.setOnClickListener(listener);
        falsebutton=dialogView.findViewById(R.id.false_button);
        falsebutton.setOnClickListener(listener);
        progressbar=dialogView.findViewById(R.id.progress_bar);
        init();
        checkGPSNet();
        // 个推SDK初始化    GeTuiService 自定义推送服务  需要的
        PushManager.getInstance().initialize(this.getApplicationContext(), com.scnu.sihao.orienteering.Service.GeTuiService.class);
        // GetGTIntentService为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.scnu.sihao.orienteering.Service.GetGTIntentService.class);
        clientID=PushManager.getInstance().getClientid(MapActivity.this);
        Log.i("clientid111",clientID);
       if(clientID!=null) {
           // 创建子线程去进行网络请求 把CID和TeamID传给服务器
           ChildThreadForMap childThreadForMap1 = new ChildThreadForMap(handler2, 1);
           childThreadForMap1.getTeamID(TeamID);
           childThreadForMap1.getCID(clientID);
           // 启动线程
           childThreadForMap1.start();
       }

        FloatingActionButton markButton=(FloatingActionButton)findViewById(R.id.button_mark);

        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    distance();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("buttonmarkisclick","buttonmarkisclick");
            }
        });

    }
    private void checkGPSNet(){
        IsGPSNetEnable isGPSNetEnable = new IsGPSNetEnable();
        isGPSworked=isGPSNetEnable.isGpsEnabled(this);
        if(isGPSworked){
        }else{
            Toast toast1= Toast.makeText(this,"GPS定位未开启",Toast.LENGTH_SHORT);
            //显示于屏幕底部
            toast1.setGravity(Gravity.BOTTOM, 0, 0);
            toast1.show();
        }
        isNetWorked=isGPSNetEnable.isNetworkAvailable(this);
        if(isNetWorked){
        }else{
            Toast toast2= Toast.makeText(this,"网络数据服务未开启",Toast.LENGTH_LONG);
            //显示于屏幕底部
            toast2.setGravity(Gravity.BOTTOM, 0, 0);
            toast2.show();
        }
    }

    // Handler实现异步操作
    // 成功打第n个点
    @SuppressLint("HandlerLeak")
     Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    // 答对5题后，答题库对话框dismiss
                    answerDialog.dismiss();
                    // 提示 打点成功！
                    Toast.makeText(MapActivity.this,"恭喜您，打点成功！",Toast.LENGTH_SHORT).show();
                    //答题成功 上传打点操作（上传打的标记点的下标）给服务器 服务器再去执行推送操作
                    // 这里创建一个类 去进行子线程的操作
                    ChildThreadForMap childThreadForMap0 = new ChildThreadForMap(handler2,0);
                    // 传递TeamID等
                    childThreadForMap0.getTeamID(TeamID);
                    childThreadForMap0.getXiaBiao(currentMarkerIndex);
                    childThreadForMap0.getPersonID(PersonID);
                    // 启动线程
                    childThreadForMap0.start();
                    break;
                    }
                case 2:{
                    setUpMap();
                    break;
                }
                // 服务器推送 打了点 去更改样式和调用计算里程的函数
                case 3:{
                    // 先获得values 以让后面的判断下标操作
                   String ValuesString= msg.getData().getString("values");
                   // 截取的第一个值 因为可能是个位数也可能是十位数 所以 用一个$标识符作为截取的判断
                    int jiequ=ValuesString.indexOf("$");
                    Log.i("jiequ", String.valueOf(jiequ));
                    String xiabiaoString =ValuesString.substring(0,jiequ);
                   // 执行更改样式操作
                    int xiabiao = Integer.valueOf(xiabiaoString);
                    Log.i("xiabiao", String.valueOf(xiabiao));
                    //改样式 要新建标记点去覆盖在那个位置上
                    MarkerOptions changedMarkerOptions = new MarkerOptions();
                    changedMarkerOptions.position(latLng[xiabiao]);
                    //这个要点击才有 changedMarkerOptions.title("title");
                    changedMarkerOptions.anchor(0.5f,0.5f);
                    changedMarkerOptions.visible(true);//标记可视化
                    changedMarkerOptions.setFlat(true);//设置marker平贴地图效果
                    changedMarkerOptions.alpha((float) 1);//透明度范围[0,1] 1为不透明
                    changedMarkerOptions.draggable(false);//不可拖曳
                    changedMarkerOptions.icon(BitmapDescriptorFactory.fromResource(0x7f070069+xiabiao));
                    ArrayList<MarkerOptions> changedMarkerList= new ArrayList<>();
                    changedMarkerList.add(changedMarkerOptions);
                    aMap.addMarkers(changedMarkerList, true);
                   // 若为第一个点,开始计算总里程
                   if(xiabiao== 0){
                       canMesureMeter=true;
                       // 先测试放在这

                   }
                    Log.i("111222333", String.valueOf(countmarker));
                    Log.i("333222111", String.valueOf(xiabiao));

                    // 若为最后一个点,结束计算总里程
                    if(xiabiao==countmarker-1){

                       Log.i("abcde","132132412414242");
                        canMesureMeter=false;
                        // 调用计算卡路里方法 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036 （长跑）
                        MesureCal mesurecal = new MesureCal();
                        mesurecal.mesureKcal(setMyLocation.totalMeter);
                        Log.i("卡路里为:", String.valueOf(mesurecal.kcal));
                        // 调用计算平均配速方法

                        //子线程去网络请求 上传用户运动总里程
                        ChildThreadForMap childThreadForMap2= new ChildThreadForMap(handler2,2);
                        childThreadForMap2.getTeamID(TeamID);
                        childThreadForMap2.getLength(setMyLocation.totalMeter);
                        childThreadForMap2.getPersonID(PersonID);
                        // 启动线程
                        childThreadForMap2.start();
                        //子线程去网络请求 上传用户运动轨迹点组
                        ChildThreadForMap childThreadForMap3= new ChildThreadForMap(handler2,3);
                        childThreadForMap3.getTeamID(TeamID);
                        childThreadForMap3.getPersonID(PersonID);
                        // 获取当前运动的轨迹点组
                        Gson gson =new Gson();
                        childThreadForMap3.getMovedString(gson.toJson(SetMyLocation.movedArray));
                        // 启动线程
                        childThreadForMap3.start();

                        // 弹出系统自带对话框 提示 完成比赛的各种数据 这里先不做 就一个恭喜完成比赛的提示就好
                        new AlertDialog.Builder(MapActivity.this)
                                .setTitle("比赛完成")
                                .setMessage("恭喜您，比赛完成！")
                                .setPositiveButton("确认",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intentToMain = new Intent(MapActivity.this,MainActivity.class);
                                                startActivity(intentToMain);
                                            }
                                        })
                                .show();
                    }
                    break;
                }
            }
        }
    };

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            aMap.getUiSettings().setLogoBottomMargin(-23);  //将LOGO 和 比例尺 往下移
        }
        /**
         * 初始化标记点的经纬度坐标方法调用
         */
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        showConponent.showScale(mUiSettings);
        showConponent.showCompass(mUiSettings);
        showConponent.showCompass2(compass3Button,MapActivity.this);
        // 往地图上添加marker
        addMarkersToMap();
    }
    private void addMarkersToMap(){
        //添加标记点
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<>();
        //这里不用建立动态数组 因为后面标记点要对latLng进行判断，否则latLng[which]越界了
        latLng =new LatLng[15];
        for (double[] x : getMarkerLngLat.markerLngLat) {
            markerOptions_2 = new MarkerOptions();
            latLng[countmarker] = new LatLng(x[0],x[1]);
            markerOptions_2.position(latLng[countmarker]);
            //这个要点击才有 markerOptions_2.title("title");
            markerOptions_2.anchor(0.5f,0.5f);
            markerOptions_2.visible(true);//标记可视化
            markerOptions_2.setFlat(true);//设置marker平贴地图效果
            markerOptions_2.alpha((float) 1);//透明度范围[0,1] 1为不透明
            markerOptions_2.draggable(false);//不可拖曳
            markerOptions_2.icon(BitmapDescriptorFactory.fromResource(0x7f07008f+countmarker));
            markerOptionlst.add(markerOptions_2);
            //记录标记点个数
            countmarker++;
        }
        aMap.addMarkers(markerOptionlst, true);
        //若是常规赛num为0，绘制点之间的虚线连接
       if(Type==0) {
           Log.i("Type111", String.valueOf(Type));
           AddMarkerLine addMarkerLine = new AddMarkerLine();
           addMarkerLine.addline(aMap, getMarkerLngLat);
       }else{
           // 若是积分赛 不绘制虚线
       }
    }
    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            try {
                switch (btn.getId()) {
                    case R.id.compass2_Button:
                    {       //开启
                        // 注册传感器监听
                       showConponent.accelerometerSensor=showConponent.new MySensorEventListener();
                        showConponent.magneticSensor=showConponent.new MySensorEventListener();
                        showConponent.mSensorManager.registerListener(showConponent.accelerometerSensor,
                                showConponent.accelerometer, Sensor.TYPE_ACCELEROMETER);
                        showConponent.mSensorManager.registerListener(showConponent.magneticSensor, showConponent.magnetic,
                                Sensor.TYPE_MAGNETIC_FIELD);
                        compass2Button.getLeft();
                        compass2Button.setVisibility(View.INVISIBLE);
                        compass3Button.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.marker_Select:
                        //showDialog(SING_CHOICE_DIALOG); //过时了
                        dialog1 = new AlertDialog.Builder(MapActivity.this);
                        ChooseMarker chooseMarker = new ChooseMarker();
                        chooseMarker.showMyDialog(dialog1,latLng,aMap,markerSelect,MapActivity.this);
                        break;
                    case R.id.back_1:
                    {
                        Intent intent_2 = new Intent(MapActivity.this,MainActivity.class);
                        startActivity(intent_2);
                        break;
                    }
                    case R.id.compass3_button:
                    {   //关闭
                        // 解除注册
                        showConponent.mSensorManager.unregisterListener(showConponent.accelerometerSensor);
                        showConponent.mSensorManager.unregisterListener(showConponent.magneticSensor);
                        // 记得把动画效果关闭 不然还是不会消失！！！
                        showConponent.ra.setFillAfter(false);
                        compass2Button.setVisibility(View.VISIBLE);
                        compass3Button.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.button_mylocation: {
                        if (isGPSworked && isNetWorked) {
                            //自定义的定位按钮与功能
                            Log.i("mylocation", "buttonmylocationisworked1");
                            //避免点击多次定位按钮 定位多次 让他回调就好
                            if(setMyLocation.canlocated) {
                                setMyLocation.myDesignLocation(aMap,MapActivity.this);
                            }
                            Log.i("mylocation", "buttonmylocationisworked2");
                            // 设置当前地图显示为当前位置 在这里movecamera 避免不断设为屏幕中心
                            if (setMyLocation.canmovetocentre) {
                                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(setMyLocation.currentLatLng, 17));
                            }
                            break;
                        }else{
                           tosetting();
                        }
                    }
                    case R.id.button_mark:
                    {
                        //调用统计当前位置与标记点坐标的直线距离函数
                    /*    distance();
                        Log.i("buttonmarkisclick","buttonmarkisclick");*/
                        break;
                    }
                    case R.id.true_button:
                    {
                       Log.i("true","2222222222222222222222222222222222222");
                       break;
                    }
                    case R.id.false_button:
                        {
                            Log.i("false","3333333333333333333333333333333333333333333333333333");
                            break;
                        }
                }
            } catch (Exception e) {
            }
        }
    };

    private void distance() throws IOException {
        for(int j=0;j<countmarker;j++) {
            float d = AMapUtils.calculateLineDistance(setMyLocation.currentLatLng, latLng[j]);
            Log.i("d", String.valueOf(d));
            if (d <= 50.0 && d > 0) {
                // 这里还没有答题 要答题成功了才去答题方法中显示打点成功
                // smartOS手机这里出错？？！！
               /* Toast.makeText(getApplicationContext(), "点击成功(还没答题)！",
                        Toast.LENGTH_SHORT).show();*/
               Log.i("click is ok!","click is ok!");
               // 将dialogNumbers重新置为0 重新开始
                AnswerDialog.dialogNumbers=0;
                // 将打标记点的下标赋值给currentMarkerIndex 以便上传这个下标给服务器
                // 即使答题失败 虽然赋值了，但是那边也没调用这个currentMarkerIndex 等答对了才调用所以可以在这里赋值
                currentMarkerIndex=j;
                Log.i("当前打点的标记点为", String.valueOf(currentMarkerIndex));
                //加上答题功能 记得分常规赛和积分赛
              /*  显示系统自带dialog自定义内容的dialog
                answerDialog = new AlertDialog.Builder(MapActivity.this);
                AnswerQuestionDialog answerQuestionDialog = new AnswerQuestionDialog();
                answerQuestionDialog.showAnswerDialog(answerDialog, dialogView, progressbar);*/

                // 显示自定义样式和内容的Dialog
                answerDialog =new AnswerDialog(MapActivity.this,handler2);
                answerDialog.show();

                //若在规定范围内则不用提示不在标记点附近
                notHere=false;
                break;
            }
        }
        if(notHere){
            Toast.makeText(getApplicationContext(), "当前位置不在标记点附近！", Toast.LENGTH_SHORT).show();
        }
        notHere=true;
    }
    private void tosetting(){
        //未开定位数据则设置为false 下次又可以启动定位
        setMyLocation.canlocated=true;
        //弹出转移设置界面对话框
        new AlertDialog.Builder(this).setTitle("网络连接或GPS服务异常")
                .setMessage("是否要跳转到设置界面进行网络连接设置或者PGS服务开启？")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        //确定就切换到设置界面
                        Intent toSetting =  new Intent(Settings.ACTION_SETTINGS);
                        startActivity(toSetting);
                        MapActivity.this.finish();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        setMyLocation.mLocationClient.onDestroy();//销毁定位客户端。
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // TODO Auto-generated method stub
        // 解除注册compass2
        showConponent.mSensorManager.unregisterListener(showConponent.accelerometerSensor);
        showConponent.mSensorManager.unregisterListener(showConponent.magneticSensor);
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        //mLocationClient.stopLocation();//停止定位
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}