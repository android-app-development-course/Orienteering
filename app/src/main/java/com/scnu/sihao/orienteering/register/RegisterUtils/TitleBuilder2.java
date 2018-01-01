package com.scnu.sihao.orienteering.register.RegisterUtils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scnu.sihao.orienteering.R;

import static java.lang.System.exit;

/**
 * Created by SiHao on 2017/12/23.
 *
 */

public class TitleBuilder2 {
    private View titleView2;
    private RelativeLayout titleBar2;
    private TextView text2;
    private ImageView leftIco2;
    private ImageView rightIco2;

    public TitleBuilder2(Activity context){
        titleView2 = context.findViewById(R.id.title_bar2);
        text2 = (TextView)titleView2.findViewById(R.id.title_text2);
        titleBar2 = (RelativeLayout)titleView2.findViewById(R.id.title_bar2);
        leftIco2 = (ImageView)titleView2.findViewById(R.id.title_leftIco2);
        rightIco2 = (ImageView)titleView2.findViewById(R.id.title_rightIco2);

    }

    public TitleBuilder2 setTitleText(String titleText){
        if(!TextUtils.isEmpty(titleText)){
            text2.setText(titleText);
        }
        return this;
    }


    /**
     * 用于设置标题栏左边要显示的图片
     * */
    public TitleBuilder2 setLeftIco(int resId){
        leftIco2.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        leftIco2.setImageResource(resId);
        return this;
    }

    /**
     * 用于设置标题栏右边要显示的图片
     * */
    public TitleBuilder2 setRightIco(int resId){
        rightIco2.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        rightIco2.setImageResource(resId);
        return this;
    }

    /**
     * 用于设置标题栏左边图片的单击事件
     * */
    public TitleBuilder2 setLeftIcoListening(View.OnClickListener listener){
        if(leftIco2.getVisibility() == View.VISIBLE){
            leftIco2.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 用于设置标题栏右边图片的单击事件
     * */
    public TitleBuilder2 setRightIcoListening(View.OnClickListener listener){
        if(rightIco2.getVisibility() == View.VISIBLE){
            rightIco2.setOnClickListener(listener);
        }
        exit(0);
        return this;
    }
}
