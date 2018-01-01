package com.scnu.sihao.orienteering.Map.ChildThread;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;

import com.scnu.sihao.orienteering.Utils.OKHttp;
import com.scnu.sihao.orienteering.Utils.UploadPointUtil;

/**
 * Created by SiHao on 2017/12/17.
 *
 */

public class ChildThreadForMap extends Thread {
    private Handler myHandler;
    OKHttp okHttpForMap = new OKHttp();
    private int myTeamID;
    private int xiabiao;
    private int myPersonID;
    private int myOption;
    private String myCid;
    private double myLength;
    private String myMvoedString;
    public ChildThreadForMap( Handler handler,int option ){
        this.myOption=option;
        this.myHandler=handler;
    }

    public void getTeamID(int TeamID){
        this.myTeamID=TeamID;
    }
    public void getXiaBiao(int xiabiao){
        this.xiabiao=xiabiao;
    }
    public void getPersonID(int PersonID){
        this.myPersonID=PersonID;
    }
    public void getCID(String cid){
        this.myCid=cid;
    }
    public void getLength(double length){
        this.myLength=length;
    }
    public void getMovedString(String movedString){
        this.myMvoedString=movedString;
    }

    public void run() {
        switch (myOption) {
            // 当执行的是打点网络请求
            case 0: {
                okHttpForMap.getHandler(handlerForChildThreadMap);
                try {
                    okHttpForMap.doGet("uploadpoint/?TeamID=" + myTeamID + "&Point=" + xiabiao);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            // 当执行的是界面初始化将CID和teamID上传
            case 1:{
                try {
                    okHttpForMap.doGet("uploadcid/?TeamID="+myTeamID+"&CID="+myCid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2:{
                try {
                    okHttpForMap.doGet("uploadrecodinglength/?Length="+myLength+"&TeamID="+myTeamID+"&PersonID="+myPersonID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            // 去进行上传运动轨迹操作  是通过HttpURLConnector和multi-form-data形式
            case 3:{
                UploadPointUtil uploadPointUtil =new UploadPointUtil();
                uploadPointUtil.uploadPoint("http://45.32.72.80/uploadrecodingpoint/",myPersonID,myTeamID,myMvoedString);
                break;
            }
        }
    }

    // 再建一个handler去请求获取数据
    @SuppressLint("HandlerLeak")
    Handler handlerForChildThreadMap = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
             /*           不用回传 而是服务器去推送

                            2 请求后 服务器去推送 然后用户再根据推送去进行判断操作
                        也不用 下面这个*/
                    if(myHandler!=null) {
                       // myHandler.sendEmptyMessage(XX);
                    }
                    break;
                }

            }
        }
    };
}
