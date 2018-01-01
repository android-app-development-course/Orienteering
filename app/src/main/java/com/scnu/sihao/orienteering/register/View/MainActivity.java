package com.scnu.sihao.orienteering.register.View;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.scnu.sihao.orienteering.Map.View.SimpleMap;
import com.scnu.sihao.orienteering.R;
import com.scnu.sihao.orienteering.register.RegisterUtils.SystemBarTintManager;
import com.scnu.sihao.orienteering.register.RegisterUtils.TitleBuilder;
import com.scnu.sihao.orienteering.register.fragment.fragment1;
import com.scnu.sihao.orienteering.register.fragment.fragment2;
import com.scnu.sihao.orienteering.register.fragment.fragment3;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends FragmentActivity {

    private ViewPager mVpContent;       //定义要显示的窗口
    private BottomBarLayout mBottomBarLayout;  //定义导航栏
    private List<Fragment> mFragmentList = new ArrayList<>();   //设置页面数组   设置里面不一定为Tabfragment就能放其他页面
    private RotateAnimation mRotateAnimation;         //旋转特效
    private Handler mHandler = new Handler();      //线程管理
    private TextView text;//主标题字体
    public String USERID;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);           //设置显示页面为定义好的xml文件
        //获取login中传进来的userid
        Intent intent2 = getIntent();
        String value = intent2.getStringExtra("userid");
        USERID=value;
        Log.i("id:",USERID);
        setuserid(value);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/slim.ttf");
        //new TitleBuilder(this).setTitleText("主页").setRightIco(R.drawable.ic_righticon1).setLeftIco(R.drawable.ic_lefticon1);
        TitleBuilder t1=new TitleBuilder(this).setTitleText("Orienteering");
        text = (TextView)findViewById(R.id.title_text);
        text.setTypeface(typeFace);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.springgreen);//通知栏所需颜色
        }

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,SimpleMap.class);
                //启动
                startActivity(intent);

            }
        });
        initView();                  //初始化试图
        initData();                  //数据初始化
        initListener();              //监听器初始化
    }

    private void initView() {         //视图初始化函数
        mVpContent = (ViewPager) findViewById(R.id.vp_content);      //显示窗口绑定控件
        mBottomBarLayout = (BottomBarLayout) findViewById(R.id.bbl);  //导航栏绑定控件
    }

    private void initData() {                 //数据初始化
        /*Bundle bundle = new Bundle();
        bundle.putString("userID",USERID);*/



        fragment1 homeFragment = new fragment1();         //无论什么界面只要是父类为Fragment都可以放入数组中，即可显示
        mFragmentList.add(homeFragment);


        fragment2 sortFragment= new fragment2();
        mFragmentList.add(sortFragment);


        fragment3 mineFragment= new fragment3();
        mFragmentList.add(mineFragment);
        /*fragment4 mineFragment=new fragment4();
        mFragmentList.add(mineFragment);*/


    }

    private void initListener() {           //初始化监听器的实现
        mVpContent.setAdapter(new MyAdapter(getSupportFragmentManager()));   //为viewpager设置管理器
        mBottomBarLayout.setViewPager(mVpContent);   //为导航栏设置显示窗口
        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final BottomBarItem bottomBarItem, int position) {   //先前定义过的每个item的final值
                if (position == 0)    //点击首页触发的事件
                {
                    //如果是第一个，即首页
                    if (mBottomBarLayout.getCurrentItem() == position){
                        //如果是在原来位置上点击,更换首页图标并播放旋转动画

                        bottomBarItem.setIconSelectedResourceId(R.mipmap.tab_loading);//更换成加载图标
                        bottomBarItem.setStatus(true);

                        //播放旋转动画
                        if (mRotateAnimation == null) {
                            mRotateAnimation = new RotateAnimation(0, 360,
                                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                    0.5f);
                            mRotateAnimation.setDuration(800);
                            mRotateAnimation.setRepeatCount(-1);
                        }
                        ImageView bottomImageView = bottomBarItem.getImageView();
                        bottomImageView.setAnimation(mRotateAnimation);
                        bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画

                        //模拟数据刷新完毕
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bottomBarItem.setIconSelectedResourceId(R.drawable.ic_home_selected);//更换成首页原来图标
                                bottomBarItem.setStatus(true);//刷新图标
                                cancelTabLoading(bottomBarItem);
                            }
                        },3000);
                        return;
                    }
                }

                //如果点击了其他条目
                BottomBarItem bottomItem = mBottomBarLayout.getBottomItem(0);
                bottomItem.setIconSelectedResourceId(R.drawable.ic_home_selected);//更换为原来的图标

                cancelTabLoading(bottomItem);//停止旋转动画
            }
        });

        //mBottomBarLayout.setUnread(0,20);//设置第一个页签的未读数为20
        //mBottomBarLayout.setUnread(1,101);//设置第二个页签的未读书
        //mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        //mBottomBarLayout.setMsg(3,"NEW");//设置第四个页签显示NEW提示文字
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

    /**停止首页页签的旋转动画函数*/
    private void cancelTabLoading(BottomBarItem bottomItem) {
        Animation animation = bottomItem.getImageView().getAnimation();
        if (animation != null){
            animation.cancel();
        }
    }

    class MyAdapter extends FragmentStatePagerAdapter {   //页面管理器

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
    public String getuserid() {
        return USERID;
    }

    public void setuserid(String id) {
        this.USERID = id;
    }



}
