package com.scnu.sihao.orienteering.register.dialog;

/**
 * Created by XW on 2017/12/23.
 */


        import android.annotation.SuppressLint;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.text.InputType;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.LayoutInflater;
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

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.util.Calendar;

        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.Response;

/**
 * Created by XW on 2017/12/5.
 */

public class joinDialog extends Dialog implements View.OnClickListener {
    String USERID, MATCHID,MAPID,COMPETITONTYPE,TEAMID,getteamurl,getpasswordurl,spinnerteamid,matchpassword;     //从上衣页面传来的用户id以及其他数据
    private Spinner join_spinner;
    private EditText join_name;
    private EditText join_password;
    String[] getteamid;
    private ArrayAdapter<String> adapter;    //获取队伍的spinner
    private  String[] map;
    private Button join_start,join_exit,join_join;
    String result,reason,newid,newreason,checkreason,checkjrl;
    Boolean ifget;


    public joinDialog(Context context, String userid,String matchid,String mapid, String competitontype) {
        super(context);
        USERID = userid;
        MATCHID=matchid;
        MAPID=mapid;
        COMPETITONTYPE = competitontype;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.joindialog);
        initdata();
        initview();

        //initevent();




    }
    //http://45.32.72.80/getteamlist/?MatchID=14948   //获取队伍
    //http://45.32.72.80/getmatchpassword/?MatchID=98706   //获取密码
    private void initdata() {
        getteamurl="http://45.32.72.80/getteamlist/?MatchID="+MATCHID;
        getpasswordurl="http://45.32.72.80/getmatchpassword/?MatchID="+MATCHID;
        Log.i("地址为：",getteamurl);
        //网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //获取队伍
                    OkHttpClient client=new OkHttpClient();
                    Request request =new Request.Builder().url(getteamurl).build();
                    Response response =client.newCall(request).execute();
                    String responseData=response.body().string();
                    //parseJSONWithJSONObject(responseData);
                    JSONObject jsonObject=new JSONObject(responseData);
                    JSONArray teamid = jsonObject.getJSONArray("ID");
                    map=new String[teamid.length()];

                    for (int i = 0; i < teamid.length(); i++) {
                        map[i]=teamid.optString(i);
                    }
                    String a= String.valueOf(map.length);
                    String b=map[0];
                    Log.i("队伍长度为：",a);
                    Message message = new Message();
                    message.what = 0x00;
                    myHandler.sendMessage(message);

                    Log.i("rl:",getpasswordurl);
                    //获取密码
                    OkHttpClient client1=new OkHttpClient();
                    Request request1 =new Request.Builder().url(getpasswordurl).build();
                    Response response1 =client1.newCall(request1).execute();
                    String responseData1=response1.body().string();
                    JSONObject jsonObject1=new JSONObject(responseData1);
                    matchpassword=jsonObject1.getString("Password");
                    Log.i("匹配的密码为：",matchpassword);
                    Message message1 = new Message();
                    message1.what = 0x01;
                    myHandler.sendMessage(message1);








                    /*if(!TextUtils.isEmpty(teamid)) {
                        dismiss();
                        Intent intent = new Intent();
                        intent.setClass(getContext(), map_activity.class);
                        intent.putExtra("matchid",matchID);
                        intent.putExtra("mapid",MAPID);
                        intent.putExtra("begintime",timebegin);
                        intent.putExtra("endtime",timeend);
                        intent.putExtra("name",name);
                        intent.putExtra("type",type);
                        intent.putExtra("userid",USERID);
                        intent.putExtra("password",password);
                        getContext().startActivity(intent);

                    }
*/


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    @SuppressLint("HandlerLeak")
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,map);
                    //设置下拉列表的风格
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //sp1
                    join_spinner.setAdapter(adapter);
                    join_spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
                    join_spinner.setVisibility(View.VISIBLE);
                    break;
                case 0x01:
                    ifget=true;
                    break;
                case 0x02:
                    Toast.makeText(getContext(),"加入失败",Toast.LENGTH_SHORT).show();
                    break;
                case 0x03:
                    Toast.makeText(getContext(),"加入失败",Toast.LENGTH_SHORT).show();
                    break;
                case 0x04:
                    Toast.makeText(getContext(),"加入队伍成功",Toast.LENGTH_SHORT).show();
                    break;
                case 0x05:
                    Toast.makeText(getContext(),"创建新队伍成功",Toast.LENGTH_SHORT).show();
                    break;
                case 0x06:
                    Toast.makeText(getContext(),"加入成功",Toast.LENGTH_SHORT).show();
                    break;
                case 0x07:
                    Toast.makeText(getContext(),"未加入比赛",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void initevent() {
        if(join_name.getText().toString().equals(null))
            Log.i("选择：","选择为创建队伍");





    }

    private void initview() {
        join_spinner=(Spinner)findViewById(R.id.join_spinner);
        join_name=(EditText)findViewById(R.id.join_name);
        join_password=(EditText)findViewById(R.id.join_password);
        join_start=(Button)findViewById(R.id.join_btn_start);
        join_exit=(Button)findViewById(R.id.join_btn_exit);
        join_join=(Button)findViewById(R.id.btn_join);
        join_start.setOnClickListener(this);
        join_exit.setOnClickListener(this);
        join_join.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_btn_start:       //创建完成按钮
            {
                if(TextUtils.isEmpty(join_name.getText().toString())&&ifget)
                {
                    //进行加入队伍操作
                    String inputpassword=join_password.getText().toString();
                    if(inputpassword.equals(matchpassword))
                    {
                        Log.i("jieguo :","keyi keyi ");
                        //进行网络请求
                        //http://45.32.72.80/jointeam/?TeamID=561228&PersonID=64368208
                        final String buildurl="http://45.32.72.80/jointeam/?TeamID="+spinnerteamid+"&PersonID="+USERID;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    OkHttpClient client=new OkHttpClient();
                                    Request request =new Request.Builder().url(buildurl).build();
                                    Response response =client.newCall(request).execute();
                                    String responseData=response.body().string();
                                    //parseJSONWithJSONObject(responseData);
                                    JSONObject jsonObject=new JSONObject(responseData);
                                    result=jsonObject.getString("Result");
                                    reason=jsonObject.getString("Reason");
                                    Log.i("Result:",result);
                                    Log.i("reason:",reason);

                                    if(result.equals("SUCCEED")) {
                                        dismiss();
                                        Message message1 = new Message();
                                        message1.what = 0x04;
                                        myHandler.sendMessage(message1);
                                        /*Intent intent = new Intent();
                                        intent.setClass(getContext(), map_activity.class);   //接入你的mapactivity
                                        intent.putExtra("matchid",MATCHID);
                                        intent.putExtra("mapid",MAPID);
                                        intent.putExtra("type",COMPETITONTYPE);
                                        intent.putExtra("userid",USERID);
                                        intent.putExtra("teamid",spinnerteamid);
                                        getContext().startActivity(intent);*/

                                    }
                                    else {
                                        Message message1 = new Message();
                                        message1.what = 0x02;
                                        myHandler.sendMessage(message1);
                                    }




                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();



                    }
                    else
                        Toast.makeText(getContext(),"输入口令错误",Toast.LENGTH_SHORT).show();
                }
                else if(!TextUtils.isEmpty(join_name.getText().toString())&&ifget)
                {
                    String newname=join_name.getText().toString();
                    String inputpassword=join_password.getText().toString();
                    if(inputpassword.equals(matchpassword)){
                        //进行新建队伍操作

                        //http://45.32.72.80/createteam/?CreatorID=27548298&MatchID=14948
                        final String newcompetitionurl="http://45.32.72.80/createteam/?CreatorID="+USERID+"&MatchID="+MATCHID;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    OkHttpClient client=new OkHttpClient();
                                    Request request =new Request.Builder().url(newcompetitionurl).build();
                                    Response response =client.newCall(request).execute();
                                    String responseData=response.body().string();
                                    //parseJSONWithJSONObject(responseData);
                                    JSONObject jsonObject=new JSONObject(responseData);
                                    //newid=jsonObject.getString("ID");
                                    String newresult=jsonObject.getString("Result");
                                    newreason=jsonObject.getString("Reason");
                                    //Log.i("Result:",newid);
                                    //Log.i("reason:",reason);
                                    Log.i("55555555:",newreason);
                                    if(newresult.equals("SUCCEED")) {
                                        newid=jsonObject.getString("ID");
                                        Message message1 = new Message();
                                        message1.what = 0x05;
                                        myHandler.sendMessage(message1);
                                        dismiss();
                                        /*Intent intent = new Intent();
                                        intent.setClass(getContext(), MapActivity.class);   //接入你的mapactivity
                                        intent.putExtra("matchid",MATCHID);
                                        intent.putExtra("mapid",MAPID);
                                        intent.putExtra("type",COMPETITONTYPE);
                                        intent.putExtra("userid",USERID);
                                        intent.putExtra("teamid",newid);
                                        getContext().startActivity(intent);*/

                                    }
                                    else {
                                        Message message1 = new Message();
                                        message1.what = 0x03;
                                        myHandler.sendMessage(message1);
                                    }




                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                }




                break;
            }
            case R.id.btn_join:
            {
                //判断
                //http://45.32.72.80/ispersoninteam/?PersonID=38213675&TeamID=510098
                checkjrl="http://45.32.72.80/ispersoninteam/?PersonID="+USERID+"&TeamID="+spinnerteamid;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            OkHttpClient client=new OkHttpClient();
                            Request request =new Request.Builder().url(checkjrl).build();
                            Response response =client.newCall(request).execute();
                            String responseData=response.body().string();
                            JSONObject jsonObject=new JSONObject(responseData);
                            String checkedresult=jsonObject.getString("Result");
                            checkreason=jsonObject.getString("Reason");
                            if(checkedresult.equals("SUCCEED")) {
                                Message message1 = new Message();
                                message1.what = 0x06;
                                myHandler.sendMessage(message1);
                                dismiss();
                                Intent intent = new Intent();
                                intent.setClass(getContext(), MapActivity.class);   //接入你的mapactivity
                                intent.putExtra("biaoshi","B");
                                intent.putExtra("matchid",MATCHID);
                                intent.putExtra("mapid",MAPID);
                                intent.putExtra("type",COMPETITONTYPE);
                                intent.putExtra("userid",USERID);
                                intent.putExtra("teamid",spinnerteamid);
                                getContext().startActivity(intent);

                            }
                            else {
                                Message message1 = new Message();
                                message1.what = 0x07;
                                myHandler.sendMessage(message1);
                            }




                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

                /*//caozuo
                if(USERID.equals("98966789"))
                {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), map_activity.class);   //接入你的mapactivity
                    intent.putExtra("matchid",MATCHID);
                    intent.putExtra("mapid",MAPID);
                    intent.putExtra("type",COMPETITONTYPE);
                    intent.putExtra("userid",USERID);
                    intent.putExtra("teamid",spinnerteamid);
                    getContext().startActivity(intent);
                    Log.i("比赛id：",MATCHID);
                    Log.i("地图id：",MAPID);
                    Log.i("比赛类型",COMPETITONTYPE);
                    Log.i("用户id:",USERID);
                    Log.i("队伍id:",spinnerteamid);

                }
                else
                    Toast.makeText(getContext(),"你还未加入本次比赛",Toast.LENGTH_SHORT).show();*/


                break;
            }
            case R.id.join_btn_exit:
            {
                //caozuo
                dismiss();
                break;

            }

        }
    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            spinnerteamid=map[arg2];
            Log.i("测试为：",spinnerteamid);

            //Log.i("地图id为：",MAPID);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }




}