package com.scnu.sihao.orienteering.register.fragment;


        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;

        import android.os.Handler;
        import android.os.Message;
        import android.support.v4.app.Fragment;

        import android.text.TextUtils;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.scnu.sihao.orienteering.Map.View.BeforeShowTrail;
        import com.scnu.sihao.orienteering.R;
        import com.scnu.sihao.orienteering.register.View.MainActivity;
        import com.scnu.sihao.orienteering.register.View.btn_normal_rule;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

        import cn.smssdk.SMSSDK;
        import lecho.lib.hellocharts.gesture.ContainerScrollType;
        import lecho.lib.hellocharts.gesture.ZoomType;
        import lecho.lib.hellocharts.model.Axis;
        import lecho.lib.hellocharts.model.AxisValue;
        import lecho.lib.hellocharts.model.Line;
        import lecho.lib.hellocharts.model.LineChartData;
        import lecho.lib.hellocharts.model.PointValue;
        import lecho.lib.hellocharts.model.ValueShape;
        import lecho.lib.hellocharts.model.Viewport;
        import lecho.lib.hellocharts.view.LineChartView;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.Response;


/**
 * Created by XW on 2017/11/16.
 */
public class fragment3 extends Fragment {
    private TextView set_time, set_length, set_total;
    private ImageView id_pic;
    String personalinfourl;
    private ImageView userpic;
    String USERID;
    //修改的
    //折线图
    private LineChartView lineChart;
    /*//修改前
    String[] date = {"10-22", "11-22"};//X轴的标注
    int[] score = {0, 15};//图表的数据点*/
    //修改后
    String[] date ;//X轴的标注
    int[] score ;//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private Button showMyTrail;

    //http://45.32.72.80/getpersonoverview/?ID=52920040
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment3, container, false);
        USERID=((MainActivity)getActivity()).getuserid();
        // String personalinfourl="http://45.32.72.80/getpersonoverview/?ID=52920040";        //这里用的是测试id的数据

        initview(view);
        if (set_time.getText().toString().equals("000"))
            initdata();
        //set_time.setText("123");
        //网络请求
        iniLinedata();

/*
        //折线图
        if(!TextUtils.isEmpty(date[6])) {
            getAxisXLables();//获取x轴的标注
            getAxisPoints();//获取坐标点
            initLineChart();//初始化
        }
*/
        //跳转到BeforeShowTrail界面控件：
        showMyTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(),BeforeShowTrail.class);
                //启动
                startActivity(intent);

            }
        });
        return view;

    }

    private void iniLinedata() {
        //http://45.32.72.80/getpersondaymile/?PersonID=98966789
        final String getdateurl="http://45.32.72.80/getpersondaymile/?PersonID=52920040";//+USERID;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(getdateurl).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray DATE=jsonObject.getJSONArray("Time");
                    date=new String[DATE.length()];
                    for(int i=0;i<DATE.length();i++)
                    {
                        date[i]=DATE.optString(i);
                    }
                    JSONArray TIME=jsonObject.getJSONArray("Length");
                    score=new int[TIME.length()];
                    for(int i=0;i<TIME.length();i++){
                        score[i]= Integer.parseInt(TIME.optString(i));
                    }
                    String a=date[6];
                    Log.i("验证：",a);


                    Message message = new Message();

                    message.what = 0x01;
                    myHandler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    String times = msg.getData().getString("times");
                    String length = msg.getData().getString("length");
                    String total = msg.getData().getString("total");
                    set_time.setText(times);
                    set_length.setText(total);
                    set_total.setText(length);
                    break;
                case 0x01:
                    if(!TextUtils.isEmpty(date[6])) {
                        getAxisXLables();//获取x轴的标注
                        getAxisPoints();//获取坐标点
                        initLineChart();//初始化
                    }
                    break;


            }
        }
    };


    private void initview(View view) {
        set_time = (TextView) view.findViewById(R.id.set_times);
        set_length = (TextView) view.findViewById(R.id.set_length);
        set_total = (TextView) view.findViewById(R.id.set_total);
        userpic = (ImageView) view.findViewById(R.id.imageView2);
        lineChart = (LineChartView) view.findViewById(R.id.line_chart);
        showMyTrail=view.findViewById(R.id.showMyTrail);
    }

    private void initdata() {

        final String USERID = ((MainActivity) getActivity()).getuserid();        //获得用户ID
        Log.i("33333:", USERID);
        final String personalinfourl = "http://45.32.72.80/getpersonoverview/?ID=52920040";
        Log.i("url:", personalinfourl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("test", personalinfourl);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(personalinfourl).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    //parseJSONWithJSONObject(responseData);
                    JSONObject jsonObject = new JSONObject(responseData);
                    String times = jsonObject.getString("Times");
                    String length = jsonObject.getString("Length");
                    String total = jsonObject.getString("Duration");
                    //+setperosonalinfo(times,length,total);
                    Bundle bundle = new Bundle();
                    bundle.putString("times", times);
                    bundle.putString("length", length);
                    bundle.putString("total", total);
                    Message message = new Message();
                    message.setData(bundle);
                    message.what = 0x00;
                    myHandler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }


    }
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#3CB371"));  //折线的颜色（绿色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setName("日期");
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("里程");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

}

