package com.scnu.sihao.orienteering.Service;

/**
 * Created by SiHao on 2017/12/18.
 *
 */
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.scnu.sihao.orienteering.Entity.GTmakePointBean;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class GetGTIntentService extends GTIntentService {
    private static Handler myHandler;
    public GetGTIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }
    //处理透传消息
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        byte[] payload = msg.getPayload();
        if(payload==null){
            Log.e(TAG, "receiver payload = null");
        }else{
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);
            Gson gson = new Gson();
            // 记得到时将Bean类的test改为makePoints
            GTmakePointBean gTmakePointBean=gson.fromJson(data,GTmakePointBean.class);
            String values=gTmakePointBean.getmakePoint();
            Log.i("values",values);
            try {
                if (values != null) {
                    // 在这里sentMessage去传值并通知执行
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("values",values);
                    message.setData(bundle);
                    myHandler.sendMessage(message);//发送message信息
                    message.what=3;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }

    public static void getHandler(Handler handler){
        myHandler=handler;
    }
}
