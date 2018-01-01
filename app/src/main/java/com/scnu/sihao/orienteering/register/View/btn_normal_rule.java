package com.scnu.sihao.orienteering.register.View;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.scnu.sihao.orienteering.R;
import com.scnu.sihao.orienteering.register.RegisterUtils.SystemBarTintManager;
import com.scnu.sihao.orienteering.register.RegisterUtils.TitleBuilder2;
import com.scnu.sihao.orienteering.register.dialog.MyDialog;

public class btn_normal_rule extends AppCompatActivity {
    private TextView text2;//map界面标题字体设置用
    private Button normal_setup;
    private Button normal_cancel;
    String USERID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn_normal_rule);
        TitleBuilder2 t3=new TitleBuilder2(this).setTitleText("常规赛").setLeftIco(R.drawable.ic_return_left).setLeftIcoListening(leftReturnListener);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/slim.ttf");
        text2=(TextView)findViewById(R.id.title_text2);
        text2.setTypeface(typeFace);//设置标题字体
        //获得fragment1传进来的userid
        Intent intent = getIntent();
        String value = intent.getStringExtra("userid");
        USERID=value;



        normal_cancel=(Button)findViewById(R.id.normal_cancel);
        normal_setup=(Button)findViewById(R.id.normal_setup);
        normal_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog myDialog= new MyDialog(btn_normal_rule.this,USERID,"normal");//Dialog的构造函数，传入userid和类型
                myDialog.show();
            }
        });
        normal_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.springgreen);//通知栏所需颜色
        }
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
    private View.OnClickListener leftReturnListener = new View.OnClickListener() {  //用于设置左边图片的点击事件
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}
