package com.scnu.sihao.orienteering.Entity;

import android.util.Log;

import java.util.List;

/**
 * Created by SiHao on 2017/12/16.
 *
 */

public class GetMarkerLngLatBean {

    private List<Integer> Score;
    private List<List<Double>> Point;

    public List<Integer> getScore() {
        return Score;
    }

    public void setScore(List<Integer> Score) {
        this.Score = Score;
    }

    public List<List<Double>> getPoint() {
        return Point;
    }

    public void setPoint(List<List<Double>> Point) {
        this.Point = Point;
    }
}
