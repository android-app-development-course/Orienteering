package com.scnu.sihao.orienteering.Map.AnswerQuestion;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.scnu.sihao.orienteering.Entity.GetAQDataBean;
import com.scnu.sihao.orienteering.Utils.OKHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiHao on 2017/12/8.
 *
 */

public class GetAQData extends Thread{
    public List<String> questionArray= new ArrayList<>();
    public List<String> answerArray= new ArrayList<>();
    String [] testQuestion=new String[15];
    String [] testAnswer=new String[15];
    private Handler myHandler2;
    OKHttp okHttpForAQ = new OKHttp();

    public GetAQData(Handler handler){
        this.myHandler2=handler;
        //setAQData();
    }

    //将自己测试数据变成网络请求数据  建立子线程 去进行网络请求

    public void run(){
        try {
            Log.i("try is run","try is run");
            okHttpForAQ.getHandler(handlerForResponse);
            okHttpForAQ.doGet("getquestion/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 再建一个handler去请求获取数据
    @SuppressLint("HandlerLeak")
    Handler handlerForResponse = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    String str=okHttpForAQ.str;
                    // Bean解析
                    Gson gson = new Gson();
                    GetAQDataBean getAQDataBean = gson.fromJson(str, GetAQDataBean.class);
                    List<Boolean> answers = getAQDataBean.getAnswer();
                    List<String> questions=getAQDataBean.getQuestion();
                    for(int i=0;i<14;i++){
                        answerArray.add(String.valueOf(answers.get(i)));
                        questionArray.add(questions.get(i));
                    }
                    myHandler2.sendEmptyMessage(1);
                    break;
                }

            }
        }
    };

    private void setAQData(){
        //将自己测试数据变成网络请求数据
        //setTestArray();

        for(int i=0;i<14;i++){
            questionArray.add(testQuestion[i]);
            answerArray.add(testAnswer[i]);
        }
    }

    private void setTestArray(){

        testQuestion[0]=" 世界杯足球比赛是4年举办一次吗？";
        testQuestion[1]=" 世界上最大的宫殿是克里姆林宫";
        testQuestion[2]=" 世界上最长的山脉是安第斯山";
        testQuestion[3]=" 消防队救火收费";
        testQuestion[4]=" 号称六一居士的是欧阳修";
        testQuestion[5]=" 七大洲中面积最小的是北极洲";
        testQuestion[6]=" 植物的叶子呈绿色，是由于叶子反射绿光？";
        testQuestion[7]=" 老花眼属于远视眼的一种？";
        testQuestion[8]=" 风骚指的是《国风》和《离骚》？";
        testQuestion[9]=" 世界足球之王贝利的球衣号是12号？";
        testQuestion[10]=" 鱼类也有耳朵？";
        testQuestion[11]=" 悲怆交响曲又称贝多芬第六交响曲？";
        testQuestion[12]=" “打蛇打七寸”的七寸是指蛇的 心脏？";
        testQuestion[13]=" 开屏的孔雀是雌孔雀？";
        testQuestion[14]=" 质能方程的提出者是爱因斯坦？";

        testAnswer[0]="true";
        testAnswer[1]="false";
        testAnswer[2]="true";
        testAnswer[3]="false";
        testAnswer[4]="true";
        testAnswer[5]="false";
        testAnswer[6]="true";
        testAnswer[7]="false";
        testAnswer[8]="true";
        testAnswer[9]="false";
        testAnswer[10]="true";
        testAnswer[11]="false";
        testAnswer[12]="true";
        testAnswer[13]="false";
        testAnswer[14]="true";
    }
}
