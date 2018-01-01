package com.scnu.sihao.orienteering.register.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.scnu.sihao.orienteering.Map.View.MapActivity;
import com.scnu.sihao.orienteering.R;

import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XW on 2017/12/5.
 *
 */

public class MyDialog extends Dialog implements View.OnClickListener {
    //spinner适配器
    private static final String[] map={"华师","地图1","地图2"};
    private ArrayAdapter<String> adapter;    //spinner1

    private EditText start_comname, start_password;
    private Spinner start_map;
    private EditText start_timebegin, start_timeend;
    private Button start_btn_begin, start_btn_exit;
    String USERID, COMPETITIONTYPE;     //从上衣页面传来的用户id以及比赛类型
    String MAPID;  //地图id
    String name,password,timebegin,timeend,type,compileurl,teamid,matchID;

    /*public interface MyDialogListener{
        public void refresh
    }*/
    public MyDialog(Context context, String userid, String competitontype) {
        super(context);
        USERID = userid;
        COMPETITIONTYPE = competitontype;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_dialog);

        initview();
        initevent();
        start_btn_begin.setOnClickListener(this);
        start_btn_exit.setOnClickListener(this);
        //适配器
        //spinner1
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,map);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //sp1
        start_map.setAdapter(adapter);
        start_map.setOnItemSelectedListener(new SpinnerSelectedListener());
        start_map.setVisibility(View.VISIBLE);


    }

    private void initevent() {
        start_timebegin.setInputType(InputType.TYPE_NULL);
        start_timeend.setInputType(InputType.TYPE_NULL);

        start_timebegin.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showbeginDatePickerDialog();
                }
            }
        });

        start_timebegin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showbeginDatePickerDialog();
            }
        });
        start_timeend.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showendDatePickerDialog();
                }
            }
        });

        start_timeend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showendDatePickerDialog();
            }
        });


    }

    private void initview() {
        start_comname = (EditText) findViewById(R.id.start_comname);
        start_password = (EditText) findViewById(R.id.start_password);
        start_map = (Spinner) findViewById(R.id.start_map);
        start_timebegin = (EditText) findViewById(R.id.start_timebegin);
        start_timeend = (EditText) findViewById(R.id.start_timeend);
        start_btn_begin = (Button) findViewById(R.id.start_btn_begin);
        start_btn_exit = (Button) findViewById(R.id.start_btn_exit);
    }

    private void showbeginDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                start_timebegin.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showendDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                start_timeend.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn_begin:       //创建完成按钮
            {
                //caozuo
                //地图ID MapID，开始时间beginTime，结束时间endTime，名字Name，类型Type（0为竞速，1为积分），
                // 创建者ID CreatorID，密码Password（不填则为无密码）
                name=start_comname.getText().toString().trim();
                password=start_password.getText().toString().trim();
                timebegin=start_timebegin.getText().toString().trim();
                timeend=start_timeend.getText().toString().trim();
                if(COMPETITIONTYPE.equals("normal"))
                    type="0";
                else type="1";
                /*Log.i("Mapid:",MAPID);   //succeed
                Log.i("begintime:",timebegin);
                Log.i("endtime",timeend);   //succeed
                Log.i("name:",name);    //succeed
                Log.i("type",type);//要转化
                Log.i("userid:",USERID);       //succeed
                Log.i("password:",password);   //succeed*/
                //http://45.32.72.80/creatematch/?MapID=3895&beginTime=2017-12-25 12:00:00&endTime=2017-12-25 14:00:00&Name=测试1&Password=&CreatorID=27548298&Type=0
                compileurl="http://45.32.72.80/creatematch/?MapID="+MAPID+"&beginTime="+timebegin+
                        " 12:00:00&endTime="+timeend+" 14:00:00&Name="+name+"&Password="+password+"&CreatorID="
                        +USERID+"&Type="+type;
                //Log.i("7997979:",compileurl);
                //网络请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            OkHttpClient client=new OkHttpClient();
                            Request request =new Request.Builder().url(compileurl).build();
                            Response response =client.newCall(request).execute();
                            String responseData=response.body().string();
                            //parseJSONWithJSONObject(responseData);
                            JSONObject jsonObject=new JSONObject(responseData);
                             teamid=jsonObject.getString("TeamID");
                             matchID=jsonObject.getString("ID");
                            Log.i("Result:",teamid);
                            Log.i("userid:",matchID);
                            if(!TextUtils.isEmpty(teamid)) {
                                dismiss();

                                Intent intent = new Intent();
                                intent.setClass(getContext(), MapActivity.class);
                                intent.putExtra("mapid",MAPID);
                                //intent.putExtra("begintime",timebegin);
                               // intent.putExtra("endtime",timeend);
                                //intent.putExtra("name",name);
                                intent.putExtra("type",type);
                                //intent.putExtra("userid",USERID);
                                //intent.putExtra("password",password);
                                intent.putExtra("matchid",matchID);
                                // 设置标识 这样区分是从MyDialog（创建比赛）还是加入比赛的 以便MapActivity去获取不同的数据
                                intent.putExtra("biaoshi","A");
                                getContext().startActivity(intent);

                            }



                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();





                /*Intent intent = new Intent();
                intent.setClass(getContext(), compile_start.class);
                intent.putExtra("mapid",MAPID);
                intent.putExtra("begintime",timebegin);
                intent.putExtra("endtime",timeend);
                intent.putExtra("name",name);
                intent.putExtra("type",type);
                intent.putExtra("userid",USERID);
                intent.putExtra("password",password);
                getContext().startActivity(intent);*/

                break;
            }
            case R.id.start_btn_exit:
            {
                //caozuo
                dismiss();
                break;

            }

        }
    }

    //sp1  "华师","地图1","地图2
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            String str=map[arg2];
            if(str.equals("华师"))
                MAPID="6663";
            else if(str.equals("地图1"))
                MAPID="3895";
            else if(str.equals("地图2"))
                MAPID="4774";
           //Log.i("地图id为：",MAPID);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}
