package com.scnu.sihao.orienteering.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class ChooseMarker{
    public void showMyDialog(AlertDialog.Builder bdialog, final LatLng[] blatlng, final AMap mymap,
                             final Button markerbutton, final Context mcontext){
        bdialog.setTitle("标记点选择");
      /*  //    指定下拉列表的显示数据
         markerchoose = new ArrayList<>();
        for( i=0;i<countmarker;i++) {
            markerchoose.add("标记点"+i);
            Log.i("markerchoose","标记点"+i);
        }*/
        final String[] arrayMarker = {"标记点1","标记点2", "标记点3", "标记点4", "标记点5",
                "标记点6", "标记点7", "标记点8", "标记点9", "标记点10", "标记点11", "标记点12", "标记点13",
                "标记点14","标记点15"};
        //    设置一个下拉的列表对话框
        bdialog.setItems(arrayMarker, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)  //which从0开始
            {  // 若该地图有标记点
                if(blatlng[which]!=(null)) {
                    mymap.moveCamera(CameraUpdateFactory.newLatLngZoom(blatlng[which], 17));
                    markerbutton.setText(arrayMarker[which]);
                }else{
                    Log.i("which","该地图不存在标记点");
                    Toast.makeText(mcontext, "该地图不存在标记点"+which+"!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
        //设置对话框的大小
        Window dialogWindow = bdialog.show().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.x = 5; // 新位置X坐标
        lp.y = 15; // 新位置Y坐标
        lp.width = 600; // 宽度
        lp.height = 1000; // 高度
        lp.alpha = 0.8f; // 透明度
        dialogWindow.setAttributes(lp);
    }
}