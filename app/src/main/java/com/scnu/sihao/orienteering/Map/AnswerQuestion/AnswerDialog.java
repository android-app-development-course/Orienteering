package com.scnu.sihao.orienteering.Map.AnswerQuestion;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scnu.sihao.orienteering.R;

public class AnswerDialog extends Dialog{
    private ProgressBar progressBar;
    private TextView questionBody, rightNumber;
    private int currentRightNumber=0;
    private Button trueButton, falseButton;
    private boolean userAnswer;
    GetAQData getAQData;
    // 初始化为0 进行dialog的切换判断
    public static int dialogNumbers=0;
    //进行终止dialog循环判断
    private boolean endTheDialog=false;
    ChildThreadForAQ childThreadForAQ;
    private Handler myHandler2;

    public AnswerDialog(Context context,Handler handler2) {
        super(context);
        myHandler2=handler2;
    }



    @SuppressLint("HandlerLeak")
    Handler handlerforAQ = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    // 去更新UI不能在子线程中更新！要在主线程 这里可以用Handler消息处理 也可以用RXJAVA响应式编程框架
                    // 主线程收到子线程传来的消息调用此方法 进行UI更新
                    Log.i("dialogNumbers", String.valueOf(dialogNumbers));
                    questionBody.setText(getAQData.questionArray.get(dialogNumbers));
                    if(dialogNumbers<13) {
                        if(!endTheDialog) {
                            setProgress();
                        }
                    }
                    break;
                }
                case 1:{
                    // 为1时才去更新第一个问题
                    questionBody.setText(getAQData.questionArray.get(dialogNumbers));
                    // 为1时才去进行进度的更新
                    setProgress();
                    break;
                }
            }
        }
    };


    private void setProgress() {
        childThreadForAQ = new ChildThreadForAQ(progressBar,handlerforAQ);
        childThreadForAQ.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_answer_dialog);
        progressBar = findViewById(R.id.myProgress_bar);
        questionBody = findViewById(R.id.question_body);
        rightNumber = findViewById(R.id.right_Number);
        trueButton = findViewById(R.id.myTrue_button);
        falseButton = findViewById(R.id.myWrong_button);
        trueButton.setOnClickListener(listener);
        falseButton.setOnClickListener(listener);
        // questionBody.setText(getAQData.questionArray.get(dialogNumbers));放在下面通过网络请求异步
        getAQData= new GetAQData(handlerforAQ);
        getAQData.start();
    }

    // 按钮点击事件

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            try {
                switch (btn.getId()) {
                    case R.id.myTrue_button: {
                        // 结束当前progressbar的子线程
                            childThreadForAQ.interrupt();
                            childThreadForAQ.myExit=true;
                        // 记录用户的点击操作
                        userAnswer = true;
                        if(String.valueOf(userAnswer).equals(getAQData.answerArray.get(dialogNumbers))){
                            currentRightNumber++;
                            rightNumber.setText(String.valueOf(currentRightNumber));
                            if(currentRightNumber==5){
                                // 结束循环Dialog
                                endTheDialog=true;
                                // 发送消息给主线程 通知它去更新UI 这里的0为标识 可以让主线程根据不同的标识进行不同的操作
                                myHandler2.sendEmptyMessage(1);
                                // 重置currentRightNumber为0
                                currentRightNumber=0;
                                // 重置dialogNumbers 让他从头开始
                                dialogNumbers=0;
                            }
                        }
                        break;
                    }
                    case R.id.myWrong_button: {
                        // 结束当前progressbar的子线程
                        childThreadForAQ.interrupt();
                        childThreadForAQ.myExit=true;
                        // 记录用户的点击操作
                        userAnswer = false;
                        if(String.valueOf(userAnswer).equals(getAQData.answerArray.get(dialogNumbers))){
                            currentRightNumber++;
                            rightNumber.setText(String.valueOf(currentRightNumber));
                            if(currentRightNumber==5){
                                // 结束循环Dialog
                                endTheDialog=true;
                                // 发送消息给主线程 通知它去更新UI 这里的1为标识 可以让主线程根据不同的标识进行不同的操作
                                myHandler2.sendEmptyMessage(1);
                                // 重置currentRightNumber为0
                                currentRightNumber=0;
                                // 重置dialogNumbers 让他从头开始
                                dialogNumbers=0;
                            }
                        }
                        break;
                    }
                }
            } catch (Exception e) {
            }
        }
    };
}
