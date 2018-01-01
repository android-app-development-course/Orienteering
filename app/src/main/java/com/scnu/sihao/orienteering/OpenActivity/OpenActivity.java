package com.scnu.sihao.orienteering.OpenActivity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.scnu.sihao.orienteering.register.View.Login;
import com.scnu.sihao.orienteering.R;

public class OpenActivity extends AppCompatActivity {
    private final int DISPLAY_LENGHT = 1500; //延迟1秒 到时再加时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_open);
        //用子线程做延时操作
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent mainIntent = new Intent(OpenActivity.this,Login.class);
                OpenActivity.this.startActivity(mainIntent);
                OpenActivity.this.finish();
            }

        },DISPLAY_LENGHT);//延时1.5秒
    }
}

