package com.scnu.sihao.orienteering.Map.AnswerQuestion;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import static android.view.Gravity.CENTER_HORIZONTAL;
/**
 * Created by SiHao on 2017/10/26.
 */
public class AnswerQuestionDialog {
private int currentprogress;
    public void showAnswerDialog(AlertDialog.Builder adialog, View vdialog, final ProgressBar pb){
    adialog.setTitle("我是一个自定义Dialog");
    adialog.setView(vdialog);
//使用匿名内部类实现线程并启动
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentprogress=0;   // 设置初始值为0
                while(currentprogress<=pb.getMax()){//设置循环5次
                       if(currentprogress==0){
                           currentprogress++;
                           try {
                               Thread.sleep(1000);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                       }else {
                           Log.i("q1","qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
                           pb.setProgress(currentprogress);
                           currentprogress++;//设置每1S进度加1
                           try {
                               Thread.sleep(1000);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                       }
                }
               // 进度完成时跳转下一页

                // 重置进度
                pb.setProgress(0);
                Log.i("tonext","tonext");
            }
        }).start();
    //设置对话框的大小
        Window dialogWindow2 = adialog.show().getWindow();
        // 再打卡一次上面这个执行不了
        Log.i("q3","qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        WindowManager.LayoutParams lp2 = dialogWindow2.getAttributes();
        dialogWindow2.setGravity(CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp2.x = 3; // 新位置X坐标.
        lp2.y = 5; // 新位置Y坐标
        lp2.width = 680; // 宽度
        lp2.height = 850; // 高度
        lp2.alpha = 0.8f; // 透明度
        dialogWindow2.setAttributes(lp2);
}
}
