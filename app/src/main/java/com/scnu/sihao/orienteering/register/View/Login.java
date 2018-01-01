package com.scnu.sihao.orienteering.register.View;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.scnu.sihao.orienteering.R;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    private Button login_btn;
    private EditText login_et_user,psw;
    private Button login_btn_forget,login_btn_zhuce;
    String user,password,logintest;
    //1224新添加
    private CheckBox chk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_login);
        intview();
        intevent();
    }
    //http://45.32.72.80/getid/?AccountNumber=123456789&Password=123456789
    private void intevent() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String psw1=psw.getText().toString();

                Boolean psw1_=TextUtils.isEmpty(psw1);
                if(psw1_){
                    Toast.makeText(Login.this, "请输入正确的账户密码", Toast.LENGTH_SHORT).show();
                    Log.i("jieguo:","false");
                }
                else {
                    //新加1224
                    if (chk.isChecked())//如果选中了"记住我"的多选框，就将用户名和密码保存，否则不保存
                    {
                        String name = login_et_user.getText().toString();
                        String password = psw.getText().toString();
                        saveToPre(getBaseContext(), name, password);
                    }
                    else
                        deleteToPre(getBaseContext());
                    LOGINTEST();//点击注册的时候进行通信，如果账号密码比对正确，则进入主页面
                    Log.i("jieguo:", "true");
                }
                /*if() {
                    Log.i("test:","121");
                    Toast.makeText(Login.this, "请输入正确的账户密码", Toast.LENGTH_SHORT).show();
                }
                else
                LOGINTEST();//点击注册的时候进行通信，如果账号密码比对正确，则进入主页面*/

            }
        });


    }
    @SuppressLint("HandlerLeak")
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    Toast.makeText(Login.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void LOGINTEST() {
        user=login_et_user.getText().toString().trim();
        password=psw.getText().toString().trim();
        logintest="http://45.32.72.80/getid/?AccountNumber="+user+"&Password="+password;
        Log.i("url:",logintest);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request =new Request.Builder().url(logintest).build();
                    Response response =client.newCall(request).execute();
                    String responseData=response.body().string();
                    Log.i("hahahahah",logintest);
                    //parseJSONWithJSONObject(responseData);
                    JSONObject jsonObject=new JSONObject(responseData);
                    String jieguo=jsonObject.getString("Result");
                    String userid=jsonObject.getString("ID");
                    Log.i("Result:",jieguo);
                    Log.i("userid:",userid);
                    String B="SUCCEED";
                    if(jieguo.equals(B)){         //一定要用equals来对比
                        Intent intent = new Intent(Login.this,
                                MainActivity.class);
                        intent.putExtra("userid", userid);
                        startActivity(intent);
                        Login.this.finish();

                    }
                    else
                        myHandler.sendEmptyMessage(0x00);



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }



    private void intview() {
        login_btn=(Button)findViewById(R.id.login_btn);
        login_btn_forget=(Button)findViewById(R.id.login_forget);
        login_btn_zhuce=(Button)findViewById(R.id.login_zhuce);
        login_et_user=(EditText)findViewById(R.id.login_et_user);
        psw=(EditText)findViewById(R.id.psw);
        //新添加1224
        chk=(CheckBox)findViewById(R.id.chk);
        readFromPre(this, login_et_user, psw);
        if(!TextUtils.isEmpty(login_et_user.getText().toString())){
            chk.setChecked(true);
        }


    }

    public void zhuce(View view) {
        //自写注册界面
        Intent intent2=new Intent(Login.this,Register.class);
        intent2.putExtra("order", "zhuce");
        startActivity(intent2);


    }

    public void forget(View view) {
        Intent intent2=new Intent(Login.this,Register.class);
        intent2.putExtra("order", "forget");
        startActivity(intent2);
    }
    private void parseJSONWithJSONObject(String jsonData) {
        try{
            JSONObject jsonObject=new JSONObject(jsonData);
            String jieguo=jsonObject.getString("Result");
            Log.i("Result:",jieguo);
            if(jieguo=="SUCCEED"){
                Log.i("dayin","22222");
                Intent intent = new Intent(Login.this,
                        MainActivity.class);
                startActivity(intent);
                Login.this.finish();

            }
            else
                Toast.makeText(Login.this,"登陆失败",Toast.LENGTH_SHORT).show();



        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //新添加记住密码
    public static void readFromPre(Context context, EditText tname, EditText tpass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String pass = sharedPreferences.getString("pass","");
        tname.setText(name);
        tpass.setText(pass);
    }
    public static void saveToPre(Context context, String name, String pass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",name);
        editor.putString("pass",pass);
        editor.commit();
    }
    public static void deleteToPre(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("userinfo",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
