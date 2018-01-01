package com.scnu.sihao.orienteering.Map.AnswerQuestion;


import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by SiHao on 2017/12/9.
 *
 */
public class ChildThreadForAQ extends Thread {
   private ProgressBar myProgressbar;
   public int myCurrentprogress;
   public boolean myExit;
   private Handler myHandler;
    public ChildThreadForAQ(ProgressBar progressbar, Handler handler){
        this.myProgressbar = progressbar;

        this.myHandler=handler;
    }
    public void run() {
        myCurrentprogress = 0;   // 设置初始值为0
        myExit=false;

            while (myCurrentprogress < myProgressbar.getMax()) {   //设置循环5次
                while (!myExit) {
                if (myCurrentprogress == 0) {
                    myCurrentprogress++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    myProgressbar.setProgress(myCurrentprogress);
                    myCurrentprogress++;//设置每1S进度加1
                    //退出内循环
                    if(myCurrentprogress==5){
                        myExit=true;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(myExit){
                    myCurrentprogress=5;
            }
        }
        // 进度完成时跳转下一页
        AnswerDialog.dialogNumbers++;
        // 重置进度
        myProgressbar.setProgress(0);
        // 发送消息给主线程 通知它去更新UI 这里的0为标识 可以让主线程根据不同的标识进行不同的操作
        myHandler.sendEmptyMessage(0);
    }
}
