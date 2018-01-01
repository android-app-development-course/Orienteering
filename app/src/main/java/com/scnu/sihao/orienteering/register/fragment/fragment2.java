package com.scnu.sihao.orienteering.register.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scnu.sihao.orienteering.R;
import com.scnu.sihao.orienteering.register.View.MainActivity;


/**
 * Created by XW on 2017/11/16.
 *
 */

public class fragment2 extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment2, container, false);
        final String USERID=((MainActivity)getActivity()).getuserid();   //获得用户ID
        Log.i("2222222:",USERID);
        return view;

    }


}
