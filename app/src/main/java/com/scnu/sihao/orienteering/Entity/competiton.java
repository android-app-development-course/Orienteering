package com.scnu.sihao.orienteering.Entity;

/**
 * Created by XW on 2017/12/22.
 */

public class competiton {
    private String name,num_people,begintime,endtime;

    public competiton(String name,String num_people,String begintime,String endtime){
        this.name=name;
        this.num_people=num_people;
        this.begintime=begintime;
        this.endtime=endtime;
    }
    public String getNum_people() {
        return num_people;
    }
    public String getName(){
        return name;
    }
    public String getBegintime(){
        return begintime;
    }
    public String getEndtime(){
        return endtime;
    }
}
