package com.scnu.sihao.orienteering.Map.AnswerQuestion;

import android.util.Log;

/**
 * Created by SiHao on 2017/10/25.
 *
 */

public class MesureCal {
    public double kcal;
    private float kg;
public void mesureKcal(double juli){
    //体重获取 是直接获取服务器的数据 解析 还是也是得通过他的类 属性获取
    // 若用户信息的体重为空
/*    if(kg==null){
        // 弹出一个框 让用户填写体重 再保存到服务器
    }else {
    } */
    kg = 60;
    //跑步热量（kcal）＝体重（kg）×距离（公里）×1.036 （长跑）
    kcal = kg * juli * 1.036;
    }

}

