package com.scnu.sihao.orienteering.register.View;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.scnu.sihao.orienteering.R;
import com.scnu.sihao.orienteering.register.RegisterUtils.SystemBarTintManager;
import com.scnu.sihao.orienteering.register.RegisterUtils.TitleBuilder2;

import cn.smssdk.SMSSDK;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Register extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SmsYanzheng";
    private EditText mEditTextPhoneNumber,mEditTextCode,mEditTextPassword;
    private Button mButtonGetCode,mButtonLogin ;
    String zhuce_url;
    String forget_url;
    //验证码
    EventHandler eventHandler;
    String strPhoneNumber;
    public static final int zhuceToast=3;
    //标题颜色
    private TextView text2;//注册界面标题字体设置用
    public String order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //获取从login里传来的order以判断执行的是注册还是忘记密码操作
        Intent intent2 = getIntent();
        String value = intent2.getStringExtra("order");
        order=value;
        Log.i("order:",order);
        //短信初始化

        TitleBuilder2 t3 = new TitleBuilder2(this).setTitleText("定向越野欢迎你").setLeftIco(R.drawable.ic_return_left).setLeftIcoListening(leftReturnListener);

        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/slim.ttf");
        text2=(TextView)findViewById(R.id.title_text2);
        text2.setTypeface(typeFace);//设置标题字体
        //标题栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.springgreen);//通知栏所需颜色
        }
        intview();
        //demo
        SMSSDK.initSDK(this, "22f4256b09b02", "6ab3b5c226beb59e68eced87b29e2dc2");

        eventHandler = new EventHandler() {

            /**
             * 在操作之后被触发
             *
             * @param event  参数1
             * @param result 参数2 SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.RESULT_ERROR表示操作失败
             * @param data   事件操作的结果
             */

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = myHandler.obtainMessage(0x00);
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                myHandler.sendMessage(message);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);


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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    private void intview() {
        mEditTextPhoneNumber = (EditText) findViewById(R.id.user_phone_input);
        mEditTextCode = (EditText) findViewById(R.id.code_view);
        mEditTextPassword=(EditText)findViewById(R.id.zhuce_et_password);
        mButtonGetCode = (Button) findViewById(R.id.get_code_button);
        mButtonLogin = (Button) findViewById(R.id.register_button);
        mButtonGetCode.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
        //1224新加
        mEditTextPhoneNumber.setFocusable(true);
        mEditTextPhoneNumber.setFocusableInTouchMode(true);
        mEditTextPhoneNumber.requestFocus();
        mEditTextPhoneNumber.requestFocusFromTouch();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.register_button) {
            String strCode = mEditTextCode.getText().toString();
            if (null != strCode && strCode.length() == 4) {
                Log.d(TAG, mEditTextCode.getText().toString());
                SMSSDK.submitVerificationCode("86", strPhoneNumber, mEditTextCode.getText().toString());
            } else {
                Toast.makeText(this, "密码长度不正确", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.get_code_button) {
            strPhoneNumber = mEditTextPhoneNumber.getText().toString();
            if (null == strPhoneNumber || "".equals(strPhoneNumber) || strPhoneNumber.length() != 11) {
                Toast.makeText(this, "电话号码输入有误", Toast.LENGTH_SHORT).show();
                return;
            }
            SMSSDK.getVerificationCode("86", strPhoneNumber);
            mButtonGetCode.setClickable(false);
            //开启线程去更新button的text
            new Thread() {
                @Override
                public void run() {
                    int totalTime = 60;
                    for (int i = 0; i < totalTime; i++) {
                        Message message = myHandler.obtainMessage(0x01);
                        message.arg1 = totalTime - i;
                        myHandler.sendMessage(message);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myHandler.sendEmptyMessage(0x02);
                }
            }.start();
        }
    }
    @SuppressLint("HandlerLeak")
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.e(TAG, "result : " + result + ", event: " + event + ", data : " + data);
                    if (result == SMSSDK.RESULT_COMPLETE) { //回调  当返回的结果是complete
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码
                            Toast.makeText(Register.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "get verification code successful.");
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码
                            Log.d(TAG, "submit code successful");
                            //Toast.makeText(Register.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
                            if(order.equals("zhuce"))
                                zhuce();
                            else if(order.equals("forget"))
                                forget();

                            //跳转页面

                        } else {
                            Log.d(TAG, data.toString());
                        }
                    } else { //进行操作出错，通过下面的信息区分析错误原因
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            //错误代码：  http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
                            Log.e(TAG, "status: " + status + ", detail: " + des);
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(Register.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x01:
                    mButtonGetCode.setText("重新发送(" + msg.arg1 + ")");
                    break;
                case 0x02:
                    mButtonGetCode.setText("获取");
                    mButtonGetCode.setClickable(true);
                    break;
                case zhuceToast:
                    Toast.makeText(Register.this,"注册失败",Toast.LENGTH_SHORT).show();
                    break;
                case 0x04:
                    Toast.makeText(Register.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                    break;
                case 0x05:
                    Toast.makeText(Register.this,"密码修改失败",Toast.LENGTH_SHORT).show();
                case 0x06:
                    Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    //忘记密码操作
    //http://45.32.72.80/changepassword/?AccountNumber=27548298&oldPassword=abc123&newPassword=123456789&Force=1
    private void forget() {
        String number,password;
        number=mEditTextPhoneNumber.getText().toString().trim();
        password=mEditTextPassword.getText().toString().trim();
        forget_url="http://45.32.72.80/changepassword/?AccountNumber="+number+"&oldPassword=abc123&newPassword="
                +password+"&Force=1";
        Log.i("forgeturl:",forget_url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request =new Request.Builder().url(forget_url).build();
                    Response response =client.newCall(request).execute();
                    String responseData=response.body().string();
                    //parseJSONWithJSONObject(responseData);
                    JSONObject jsonObject=new JSONObject(responseData);
                    String jieguo=jsonObject.getString("Result");
                    Log.i("Result:",jieguo);
                    String B="SUCCEED";
                    if(jieguo.equals("SUCCEED")){         //一定要用equals来对比
                        Log.i("dayin","22222");
                        Intent intent = new Intent(Register.this,
                                Login.class);
                        startActivity(intent);
                        Register.this.finish();
                        myHandler.sendEmptyMessage(0x04);

                    }

                    else {
                        myHandler.sendEmptyMessage(0x05);
                    }



                    /*myHandler.sendMessage(message);
                        Toast.makeText(login_zhuce.this,"fail",Toast.LENGTH_SHORT).show();*/
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }

    //http://45.32.72.80/createaccount/?AccountNumber=333333333&Password=333333333&Name=测试用户5&Sex=1&Birthday=2017-12-29&Weight=99
    private void zhuce() {
        String number,password;
        number=mEditTextPhoneNumber.getText().toString().trim();
        password=mEditTextPassword.getText().toString().trim();
        zhuce_url="http://45.32.72.80/createaccount/?AccountNumber="+number+"&Password="+password+
                "&Name=用户&Sex=1&Birthday=2000-01-01&Weight=00";
        Log.i("url:",zhuce_url);
        //通讯
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request =new Request.Builder().url(zhuce_url).build();
                    Response response =client.newCall(request).execute();
                    String responseData=response.body().string();
                    //parseJSONWithJSONObject(responseData);
                    JSONObject jsonObject=new JSONObject(responseData);
                    String jieguo=jsonObject.getString("Result");
                    Log.i("Result:",jieguo);
                    String B="SUCCEED";
                    if(jieguo.equals("SUCCEED")){         //一定要用equals来对比

                        Log.i("dayin","22222");
                        Intent intent = new Intent(Register.this,
                                Login.class);
                        startActivity(intent);
                        myHandler.sendEmptyMessage(0x06);
                        Register.this.finish();

                    }

                    else {
                        myHandler.sendEmptyMessage(zhuceToast);
                    }



                    /*myHandler.sendMessage(message);
                        Toast.makeText(login_zhuce.this,"fail",Toast.LENGTH_SHORT).show();*/
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private View.OnClickListener leftReturnListener = new View.OnClickListener() {  //用于设置左边图片的点击事件
        @Override
        public void onClick(View view) {
            finish();
        }
    };

};