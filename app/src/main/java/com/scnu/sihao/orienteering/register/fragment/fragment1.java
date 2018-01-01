package com.scnu.sihao.orienteering.register.fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scnu.sihao.orienteering.R;
import com.scnu.sihao.orienteering.register.View.MainActivity;
import com.scnu.sihao.orienteering.register.View.btn_goal_rule;
import com.scnu.sihao.orienteering.register.View.btn_normal_rule;
import com.scnu.sihao.orienteering.register.dialog.joinDialog;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by XW on 2017/11/16.
 *
 */

public class fragment1 extends Fragment {
    //首页控件
    private TextView fragment1text;
    private Button btn_formal,btn_gool;   //设置两个比赛按钮的
    private ImageView search_icon;         //搜索图片按钮
    private EditText editor;              //输入框
    private Spinner spinner;                 //下拉框
    //list
    private ListView listView;
    private String getcompetitionurl;         //获取比赛的url
    //修改的
    int listnum=0;  //list的条数
    //private int[] listpic={R.drawable.list1,R.drawable.list2,R.drawable.list3,R.drawable.list4,R.drawable.list5,R.drawable.list6,R.drawable.list7,R.drawable.list8};
/*
    private  int[] icons={R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
*/

    private List<String> imgUrls = new ArrayList<String>();
    private List<String> matids = new ArrayList<String>();
    private List<String> mapids = new ArrayList<String>();
    private List<String> competitiontypes = new ArrayList<String>();
    private List<String> nums = new ArrayList<String>();
    private List<String> begintimes = new ArrayList<String>();
    private List<String> endtimes = new ArrayList<String>();
    private List<String> names = new ArrayList<String>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final String USERID=((MainActivity)getActivity()).getuserid();    //获得用户ID
        View view = inflater.inflate(R.layout.fragment1, container, false);
        initview(view);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        initcompetiton();

        btn_formal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(),btn_normal_rule.class);
                intent.putExtra("userid", USERID);
                //启动
                startActivity(intent);

            }
        });
        btn_gool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Userid:",USERID);
                Intent intent =new Intent(getContext(),btn_goal_rule.class);
                intent.putExtra("userid", USERID);
                //启动
                startActivity(intent);

            }
        });
        //LISTVIEW设置点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                String uid=USERID;
                String name=((TextView) view.findViewById(R.id.item_tv)).getText().toString();
                String num=((TextView) view.findViewById(R.id.num_people)).getText().toString();   //获取并显示原来的标题
                String begintime=((TextView) view.findViewById(R.id.time_begin)).getText().toString();   //获取并显示原来的内容
                String endtime=((TextView) view.findViewById(R.id.time_over)).getText().toString();   //获取并显示原来的内容
                String mapid=((TextView) view.findViewById(R.id.mapid)).getText().toString();
                String competitontype=((TextView) view.findViewById(R.id.competittype)).getText().toString();
                String matid=((TextView) view.findViewById(R.id.matchid)).getText().toString();

                Log.i("matchid:",matid);   //需要
                Log.i("userid:",uid);      //需要
                Log.i("mapid:",mapid);
                Log.i("competitontype",competitontype);
                //需要个人id，比赛id，地图id，比赛类型
                // (Context context, String userid,String matchid,String mapid, String competitontype)
                joinDialog joinDialog= new joinDialog(getContext(),USERID,matid,mapid,competitontype);  ////Dialog的构造函数，传入userid和类型
                joinDialog.show();

            }
        });

        return view;
    }



    //网络请求获得数据
    private void initcompetiton() {
        getcompetitionurl="http://45.32.72.80/getmatchlist/?filter=";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request =new Request.Builder().url(getcompetitionurl).build();
                    Response response =client.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject jsonObject=new JSONObject(responseData);
                    //请求人数
                    JSONArray num = jsonObject.getJSONArray("Number");
                    listnum=num.length();
                    for (int i = 0; i < num.length(); i++) {
                        nums.add(num.optString(i));

                    }
                    //请求比赛名称
                    JSONArray name = jsonObject.getJSONArray("Name");
                    for (int i = 0; i < name.length(); i++) {
                        names.add(name.optString(i));
                    }
                    //请求地图id
                    JSONArray mapid=jsonObject.getJSONArray("mapName");

                    for(int i=0;i<mapid.length();i++){
                        if(mapid.optString(i).equals("华师"))
                            mapids.add("6663");
                        else if(mapid.optString(i).equals("地图2"))
                            mapids.add("4774");
                        else
                            mapids.add("3895");
                    }
                    //请求比赛类型     /
                    JSONArray competitiontype=jsonObject.getJSONArray("Type");
                    for(int i=0;i<competitiontype.length();i++){
                        if(competitiontype.optString(i).equals("false"))
                            competitiontypes.add("0");
                        else
                            competitiontypes.add("1");
                    }
                    //请求获得图片url
                    JSONArray pic=jsonObject.getJSONArray("ImgUrl");

                    for(int i=0;i<pic.length();i++)
                    {
                        imgUrls.add(pic.optString(i));

                    }
                    //请求比赛id
                    JSONArray matid=jsonObject.getJSONArray("ID");

                    for(int i=0;i<matid.length();i++){
                        matids.add(matid.optString(i));
                    }

                    //请求比赛开始时间
                    JSONArray Begintime = jsonObject.getJSONArray("beginTime");
                    for (int i = 0; i < Begintime.length(); i++) {
                        begintimes.add(Begintime.optString(i));
                    }
                    //请求比赛结束时间
                    JSONArray Endtime = jsonObject.getJSONArray("endTime");
                    for (int i = 0; i < Endtime.length(); i++) {
                        endtimes.add(Endtime.optString(i));
                    }
                    Log.i("条数:",Integer.toString(listnum));
                    Message message=new Message();
                    message.what=0x00;
                    myHandler.sendMessage(message);







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

                    listView.setAdapter(new BaseAdapter() {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            ViewHolder holder = null;
                            if (convertView == null) {
                                holder = new ViewHolder();
                                convertView = getLayoutInflater().inflate(R.layout.list_item,
                                        null);
                                holder.imageView = (ImageView) convertView
                                        .findViewById(R.id.imageview);
                                holder.mTextView=(TextView) convertView.findViewById(R.id.item_tv);
                                holder.num_people=(TextView) convertView.findViewById(R.id.num_people);      //等泽宇把接口弄好之后，将数据存储在数组中并加入即可
                                holder.time_begin=(TextView) convertView.findViewById(R.id.time_begin);
                                holder.time_over=(TextView) convertView.findViewById(R.id.time_over);
                                holder.matchid=(TextView) convertView.findViewById(R.id.matchid);
                                holder.mapid=(TextView) convertView.findViewById(R.id.mapid);
                                holder.competitontype=(TextView) convertView.findViewById(R.id.competittype);

                                convertView.setTag(holder);
                            } else {
                                holder = (ViewHolder) convertView.getTag();
                            }

                            ImageLoader.getInstance().displayImage(imgUrls.get(position),
                                    holder.imageView);
                            holder.mTextView.setText(names.get(position));
                            holder.num_people.setText(nums.get(position));
                            holder.time_begin.setText(begintimes.get(position));
                            holder.time_over.setText(endtimes.get(position));
                            holder.matchid.setText(matids.get(position));
                            holder.mapid.setText(mapids.get(position));
                            holder.competitontype.setText(competitiontypes.get(position));
                            return convertView;
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }

                        @Override
                        public Object getItem(int position) {
                            return imgUrls.get(position);
                        }

                        @Override
                        public int getCount() {
                            return imgUrls.size();
                        }
                    });
                    setListViewHeightBasedOnChildren(listView); //重设置listview的长度，一定要设置！不然只会显示一条信息*/



                    break;

            }
        }
    };
    private void initview(View view) {
        //顶部文字获取焦点 不然首页的scrollview不能默认定位到顶部；
        fragment1text=(TextView) view.findViewById(R.id.fragment1text) ;
        fragment1text.setFocusable(true);
        fragment1text.setFocusableInTouchMode(true);
        fragment1text.requestFocus();
        //首页控件
        btn_formal=(Button)view.findViewById(R.id.fragment_btn_formal);
        btn_gool =(Button) view.findViewById(R.id.fragment_btn_gools);
        search_icon=(ImageView) view.findViewById(R.id.search_icon);
        editor =(EditText) view.findViewById(R.id.editor);
        spinner=(Spinner) view.findViewById(R.id.spinner);
        //list
        listView=(ListView) view.findViewById(R.id.lv);
        matids.clear();
        mapids.clear();
        competitiontypes.clear();
        imgUrls.clear();
        nums.clear();
        begintimes.clear();
        endtimes.clear();
        names.clear();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter  并计算所有条目的长度，不然listview只会显示一条。
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    class ViewHolder {
        ImageView imageView;
        TextView mTextView;
        TextView num_people;
        TextView time_begin;
        TextView time_over;
        TextView matchid;
        TextView mapid;
        TextView competitontype;
    }



}
